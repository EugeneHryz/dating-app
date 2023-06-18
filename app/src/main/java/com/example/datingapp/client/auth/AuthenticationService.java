package com.example.datingapp.client.auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("/api/auth/register")
    Call<AuthenticatedUser> register(@Body RegisterRequest registerRequest);

    @POST("/api/auth/login")
    Call<AuthenticatedUser> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/logout")
    Call<Void> logout();
}
