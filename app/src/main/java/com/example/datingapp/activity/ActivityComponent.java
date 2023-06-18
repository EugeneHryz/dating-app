package com.example.datingapp.activity;

import com.example.datingapp.ApplicationComponent;
import com.example.datingapp.chat.ConversationActivity;
import com.example.datingapp.home.ChatsFragment;
import com.example.datingapp.home.HomeActivity;
import com.example.datingapp.login.LogInFragment;
import com.example.datingapp.login.StartupActivity;
import com.example.datingapp.searchpeople.PeopleFilterDialogFragment;
import com.example.datingapp.searchpeople.PeopleNearbyFragment;
import com.example.datingapp.searchpeople.SearchPeopleActivity;
import com.example.datingapp.signup.SignUpActivity;
import com.example.datingapp.signup.SignUpFragment;
import com.example.datingapp.splash.SplashScreenActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = { ApplicationComponent.class })
public interface ActivityComponent {

    void inject(SplashScreenActivity activity);

    void inject(StartupActivity activity);

    void inject(SignUpActivity activity);

    void inject(HomeActivity activity);

    void inject(SearchPeopleActivity activity);

    void inject(ConversationActivity activity);


    void inject(SignUpFragment fragment);

    void inject(LogInFragment fragment);

    void inject(PeopleNearbyFragment fragment);

    void inject(PeopleFilterDialogFragment fragment);

    void inject(ChatsFragment fragment);
}
