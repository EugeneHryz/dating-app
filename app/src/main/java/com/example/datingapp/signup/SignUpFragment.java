package com.example.datingapp.signup;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.R;
import com.example.datingapp.account.UserAccountValidation;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.fragment.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpFragment extends BaseFragment {

    private static final String TAG = SignUpFragment.class.getName();

    protected SignUpViewModel viewModel;

    private TextInputLayout loginInputLayout;
    private TextInputEditText loginInput;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordInput;
    private TextInputLayout confirmPasswordInputLayout;
    private TextInputEditText confirmPasswordInput;

    private boolean loginIsValid;
    private boolean passwordIsValid;
    private boolean confirmPasswordIsValid;

    private Button nextButton;

    @Override
    public String getUniqueTag() {
        return TAG;
    }

    @Override
    protected void injectFragment(ActivityComponent activityComponent) {
        activityComponent.inject(this);
        viewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);

        loginInputLayout = view.findViewById(R.id.login_entry_input_layout);
        loginInput = view.findViewById(R.id.login_entry);
        passwordInputLayout = view.findViewById(R.id.password_entry_input_layout);
        passwordInput = view.findViewById(R.id.password_entry);
        confirmPasswordInputLayout = view.findViewById(R.id.confirm_password_input_layout);
        confirmPasswordInput = view.findViewById(R.id.confirm_password);

        loginInput.addTextChangedListener(new LoginTextWatcher());
        passwordInput.addTextChangedListener(new PasswordTextWatcher());
        confirmPasswordInput.addTextChangedListener(new ConfirmPasswordTextWatcher());

        nextButton = view.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(v -> setPassword());

        viewModel.getState().observe(getViewLifecycleOwner(), this::handleStateChange);
        return view;
    }

    private class LoginTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String login = loginInput.getText().toString();

            loginIsValid = UserAccountValidation.validateLogin(login);
            loginInputLayout.setError(loginIsValid ? null : getString(R.string.login_too_short));
            updateNextButtonState();
        }
    }

    private class PasswordTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = passwordInput.getText().toString();

            passwordIsValid = UserAccountValidation.validatePassword(password);
            passwordInputLayout.setError(passwordIsValid ? null : getString(R.string.password_too_short));
            updateNextButtonState();
        }
    }

    private class ConfirmPasswordTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String confirmPassword = confirmPasswordInput.getText().toString();
            String password = passwordInput.getText().toString();

            confirmPasswordIsValid = confirmPassword.equals(password);
            confirmPasswordInputLayout.setError(confirmPasswordIsValid ? null : getString(R.string.passwords_not_equal));
            updateNextButtonState();
        }
    }

    private void updateNextButtonState() {
        boolean buttonEnabled = loginIsValid && passwordIsValid && confirmPasswordIsValid;
        nextButton.setEnabled(buttonEnabled);
    }

    private void setPassword() {
        String login = loginInput.getText().toString();
        String password = passwordInput.getText().toString();

        viewModel.createAccount(login, password);
    }

    private void handleStateChange(SignUpViewModel.State state) {
        boolean enabledInputs = (state != SignUpViewModel.State.CREATING);

        if (state == SignUpViewModel.State.CREATING || state == SignUpViewModel.State.FAILED) {
            loginInputLayout.setEnabled(enabledInputs);
            passwordInputLayout.setEnabled(enabledInputs);
            confirmPasswordInputLayout.setEnabled(enabledInputs);
            nextButton.setEnabled(enabledInputs);
        }
        if (state == SignUpViewModel.State.LOGIN_ALREADY_EXISTS) {
            loginInputLayout.setError(getString(R.string.login_already_exists));
        }
    }
}