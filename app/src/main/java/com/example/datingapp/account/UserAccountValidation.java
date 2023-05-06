package com.example.datingapp.account;

public class UserAccountValidation {

    private static final int USER_LOGIN_MIN_LENGTH = 4;
    private static final int USER_PASSWORD_MIN_LENGTH = 8;

    public static boolean validateLogin(String login) {
        return login != null && login.length() >= USER_LOGIN_MIN_LENGTH;
    }

    public static boolean validatePassword(String password) {
        return password != null && password.length() >= USER_PASSWORD_MIN_LENGTH;
    }
}
