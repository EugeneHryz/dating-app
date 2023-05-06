package com.example.datingapp.signup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.io.IoExecutor;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = SignUpViewModel.class.getName();

    private final Executor ioExecutor;

    enum State {
        CREATING,
        CREATED,
        LOGIN_ALREADY_EXISTS,
        FAILED
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();

    @Inject
    public SignUpViewModel(@IoExecutor Executor executor) {
        ioExecutor = executor;
    }

    public void createAccount(String login, String password) {
        if (login == null || password == null) {
            throw new AssertionError("Nickname and password aren't set");
        }
        ioExecutor.execute(() -> {
            if (login.equals("zhenya")) {
                state.postValue(State.LOGIN_ALREADY_EXISTS);
            } else {
                state.postValue(State.CREATED);
            }
        });
    }

    public MutableLiveData<State> getState() {
        return state;
    }
}
