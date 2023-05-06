package com.example.datingapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.RequestCode;
import com.example.datingapp.fragment.BaseFragment;
import com.example.datingapp.signup.SignUpActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

public class LogInFragment extends BaseFragment implements TextWatcher {

    private static final String TAG = LogInFragment.class.getName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StartupViewModel viewModel;

    private TextInputLayout loginInputLayout;
    private TextInputEditText loginInput;

    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordInput;

    private Button logInButton;
    private Button signUpButton;

    @Override
    public String getUniqueTag() {
        return TAG;
    }

    @Override
    protected void injectFragment(ActivityComponent activityComponent) {
        activityComponent.inject(this);
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(StartupViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_in_fragment, container, false);

        loginInputLayout = view.findViewById(R.id.login_entry_input_layout);
        loginInput = view.findViewById(R.id.login_entry);
        loginInput.addTextChangedListener(this);

        passwordInputLayout = view.findViewById(R.id.password_entry_input_layout);
        passwordInput = view.findViewById(R.id.password_entry);
        passwordInput.addTextChangedListener(this);

        logInButton = view.findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(v -> signIn());

        signUpButton = view.findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(v -> startSignUpActivityForResult());

        viewModel.getState().observe(getViewLifecycleOwner(), this::handleStateChange);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (passwordInputLayout.getError() != null) {
            passwordInputLayout.setError(null);
        }
        if (loginInputLayout.getError() != null) {
            loginInputLayout.setError(null);
        }
    }

    private void startSignUpActivityForResult() {
        Intent intent = new Intent(requireActivity(), SignUpActivity.class);
        requireActivity().startActivityForResult(intent, RequestCode.SETUP_ACCOUNT);
    }

    private void signIn() {
        String login = loginInput.getText().toString();
        String password = passwordInput.getText().toString();

        viewModel.signIn(login, password);
    }

    private void handleStateChange(StartupViewModel.State state) {
        if (state == StartupViewModel.State.NON_EXISTING_USER) {
            passwordInput.getText().clear();
            loginInput.getText().clear();
            logInButton.setEnabled(true);
            loginInputLayout.setEnabled(true);
            passwordInputLayout.setEnabled(true);
            loginInputLayout.setError(getString(R.string.non_existing_user));

        } else if (state == StartupViewModel.State.INVALID_PASSWORD) {
            passwordInput.getText().clear();
            logInButton.setEnabled(true);
            loginInputLayout.setEnabled(true);
            passwordInputLayout.setEnabled(true);
            passwordInputLayout.setError(getString(R.string.invalid_password));

        } else if (state == StartupViewModel.State.SIGNING_IN) {
            logInButton.setEnabled(false);
            loginInputLayout.setEnabled(false);
            passwordInputLayout.setEnabled(false);
        }
    }
}
