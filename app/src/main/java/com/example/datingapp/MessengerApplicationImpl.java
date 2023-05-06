package com.example.datingapp;

import android.app.Application;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

public class MessengerApplicationImpl extends Application implements MessengerApplication {

    private ApplicationComponent appComponent;

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
}
