package com.example.datingapp.signup;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.MessengerApplication;
import com.example.datingapp.client.Constants;
import com.example.datingapp.client.auth.AuthenticatedUser;
import com.example.datingapp.client.auth.AuthenticationService;
import com.example.datingapp.client.auth.RegisterRequest;
import com.example.datingapp.client.token.TokenService;
import com.example.datingapp.io.IoExecutor;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = SignUpViewModel.class.getName();

    private final Executor ioExecutor;
    private final AuthenticationService authService;
    private final TokenService tokenService;
    private final MessengerApplication application;

    enum State {
        CREATING,
        CREATED,
        USERNAME_ALREADY_EXISTS,
        NETWORK_ERROR
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();

    @Inject
    public SignUpViewModel(@IoExecutor Executor executor,
                           AuthenticationService authService,
                           TokenService tokenService,
                           Application application) {
        ioExecutor = executor;
        this.authService = authService;
        this.tokenService = tokenService;
        this.application = (MessengerApplication) application;
    }

    public void createAccount(String login, String password) {
        if (login == null || password == null) {
            throw new AssertionError("Nickname and password aren't set");
        }
        ioExecutor.execute(() -> {
            state.postValue(State.CREATING);

            tokenService.deleteToken();
            RegisterRequest registerRequest = new RegisterRequest(login, password, null);
            authService.register(registerRequest).enqueue(new Callback<AuthenticatedUser>() {
                @Override
                public void onResponse(Call<AuthenticatedUser> call, Response<AuthenticatedUser> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        AuthenticatedUser user = response.body();
                        if (user != null) {
                            tokenService.updateToken(user.getToken());
                            application.setAuthenticatedUser(user);
                            state.postValue(State.CREATED);
                        }
                    } else if (response.code() == Constants.HTTP_CONFLICT) {
                        state.postValue(State.USERNAME_ALREADY_EXISTS);
                    } else {
                        Log.d(TAG, "Unable to create account. Status code: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<AuthenticatedUser> call, Throwable t) {
                    Log.d(TAG, "Failed to create account", t);
                    state.postValue(State.NETWORK_ERROR);
                }
            });
        });
    }

    public MutableLiveData<State> getState() {
        return state;
    }
}
