package com.example.datingapp.client.auth;

public class AuthenticatedUser {

    private Long id;
    private String email;
    private String username;
    private String token;

    public AuthenticatedUser() {
    }

    public AuthenticatedUser(Long id, String email, String username, String token) {
        this.email = email;
        this.username = username;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
