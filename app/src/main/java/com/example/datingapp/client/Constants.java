package com.example.datingapp.client;

public class Constants {

    // http response statuses
    public static final int HTTP_SUCCESS = 200;
    public static final int HTTP_CONFLICT = 409;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_FORBIDDEN = 403;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private Constants() {
    }
}
