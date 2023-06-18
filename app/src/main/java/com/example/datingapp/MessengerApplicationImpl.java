package com.example.datingapp;

import android.app.Application;

import com.example.datingapp.client.auth.AuthenticatedUser;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

public class MessengerApplicationImpl extends Application implements MessengerApplication {

    public static final String SERVER_ADDRESS = "192.168.43.99:8080";

    private ApplicationComponent appComponent;

    private AuthenticatedUser user;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(this))
                .build();

        EmojiManager.install(new IosEmojiProvider());
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return appComponent;
    }

    @Override
    public AuthenticatedUser getAuthenticatedUser() {
        return user;
    }

    @Override
    public void setAuthenticatedUser(AuthenticatedUser user) {
        this.user = user;
    }
}
