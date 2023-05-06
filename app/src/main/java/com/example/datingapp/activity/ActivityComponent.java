package com.example.datingapp.activity;

import com.example.datingapp.ApplicationComponent;
import com.example.datingapp.home.HomeActivity;
import com.example.datingapp.login.LogInFragment;
import com.example.datingapp.login.StartupActivity;
import com.example.datingapp.signup.SignUpFragment;
import com.example.datingapp.signup.SignUpActivity;
import com.example.datingapp.splash.SplashScreenActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = { ApplicationComponent.class })
public interface ActivityComponent {

    void inject(SplashScreenActivity activity);

    void inject(StartupActivity activity);

    void inject(SignUpActivity activity);

    void inject(HomeActivity activity);


    void inject(SignUpFragment fragment);

    void inject(LogInFragment fragment);
}
