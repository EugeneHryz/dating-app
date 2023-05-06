package com.example.datingapp.signup;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;

import javax.inject.Inject;

public class SignUpActivity extends BaseActivity {

    private static final String TAG = SignUpActivity.class.getName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private SignUpViewModel viewModel;

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(SignUpViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        viewModel.getState().observe(this, this::handleStateChange);
        showInitialFragment(new SignUpFragment());
    }

    private void handleStateChange(SignUpViewModel.State state) {
        if (state == SignUpViewModel.State.CREATED) {
            setResult(RESULT_OK);
            finish();
        }
    }
}