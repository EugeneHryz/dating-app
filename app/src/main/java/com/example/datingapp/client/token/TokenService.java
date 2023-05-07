package com.example.datingapp.client.token;

public interface TokenService {

    String getToken();

    void updateToken(String token);

    void deleteToken();
}
