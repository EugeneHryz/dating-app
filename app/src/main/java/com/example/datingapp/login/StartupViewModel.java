package com.example.datingapp.login;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.MessengerApplication;
import com.example.datingapp.client.Constants;
import com.example.datingapp.client.auth.AuthenticatedUser;
import com.example.datingapp.client.auth.AuthenticationService;
import com.example.datingapp.client.auth.LoginRequest;
import com.example.datingapp.client.token.TokenService;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.lifecycle.LifecycleManager;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartupViewModel extends ViewModel {

    private static final String TAG = StartupViewModel.class.getName();

    private final Executor ioExecutor;
    private final LifecycleManager lifecycleManager;
    private final AuthenticationService authService;
    private final TokenService tokenService;
    private final MessengerApplication application;

    enum State {
        NETWORK_ERROR,
        LOG_IN_FAILED,
        LOGGING_IN,
        LOGGED_IN,
        LOGGED_OUT
    }

    private final MutableLiveData<State> state = new MutableLiveData<>(State.LOGGED_OUT);

    @Inject
    public StartupViewModel(@IoExecutor Executor ioExecutor,
                            LifecycleManager lifecycleManager,
                            AuthenticationService authService,
                            TokenService tokenService,
                            Application application) {
        this.ioExecutor = ioExecutor;
        this.lifecycleManager = lifecycleManager;
        this.authService = authService;
        this.tokenService = tokenService;
        this.application = (MessengerApplication) application;
    }

    void signIn(String login, String password) {
        ioExecutor.execute(() -> {

            state.postValue(State.LOGGING_IN);

            tokenService.deleteToken();
            LoginRequest loginRequest = new LoginRequest(login, password);
            authService.login(loginRequest).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<AuthenticatedUser> call, Response<AuthenticatedUser> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        AuthenticatedUser user = response.body();
                        if (user != null) {
                            tokenService.updateToken(user.getToken());
                            application.setAuthenticatedUser(user);
                            state.postValue(State.LOGGED_IN);
                        }
                    } else if (response.code() == Constants.HTTP_FORBIDDEN) {
                        Log.d(TAG, "Username not found or provided password is invalid");
                        state.postValue(State.LOG_IN_FAILED);
                    } else {
                        Log.d(TAG, "Unable to login. Status code: " + response.code());
                        state.postValue(State.LOG_IN_FAILED);
                    }
                }

                @Override
                public void onFailure(Call<AuthenticatedUser> call, Throwable t) {
                    Log.d(TAG, "Failed to perform a login request", t);
                    state.postValue(State.NETWORK_ERROR);
                }
            });
        });
    }

    public MutableLiveData<State> getState() {
        return state;
    }
}
