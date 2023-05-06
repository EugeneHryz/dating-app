package com.example.datingapp.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.lifecycle.LifecycleManager;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class StartupViewModel extends ViewModel {

    private static final String TAG = StartupViewModel.class.getName();

    private final Executor ioExecutor;
    private final LifecycleManager lifecycleManager;

    enum State {
        INVALID_PASSWORD,
        NON_EXISTING_USER,
        SIGNING_IN,
        LOGGED_IN,
        LOGGED_OUT
    }

    private final MutableLiveData<State> state = new MutableLiveData<>(State.LOGGED_OUT);

    @Inject
    public StartupViewModel(@IoExecutor Executor ioExecutor,
                            LifecycleManager lifecycleManager) {
        this.ioExecutor = ioExecutor;
        this.lifecycleManager = lifecycleManager;
    }

    void signIn(String login, String password) {
        // TODO: make authentication request to the server
        if (!login.equals("zhenya")) {
            state.postValue(State.NON_EXISTING_USER);
        } else if (!password.equals("qwertyui")) {
            state.postValue(State.INVALID_PASSWORD);
        } else {
            state.postValue(State.LOGGED_IN);
        }
    }

    public MutableLiveData<State> getState() {
        return state;
    }
}
