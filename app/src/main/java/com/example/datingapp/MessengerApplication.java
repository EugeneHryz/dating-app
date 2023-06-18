package com.example.datingapp;

import com.example.datingapp.client.auth.AuthenticatedUser;

public interface MessengerApplication {

    ApplicationComponent getApplicationComponent();

    AuthenticatedUser getAuthenticatedUser();

    void setAuthenticatedUser(AuthenticatedUser user);
}
