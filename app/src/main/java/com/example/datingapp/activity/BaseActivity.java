package com.example.datingapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.datingapp.ApplicationComponent;
import com.example.datingapp.MessengerApplication;
import com.example.datingapp.R;
import com.example.datingapp.client.event.NotAuthenticatedEvent;
import com.example.datingapp.event.Event;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventListener;
import com.example.datingapp.fragment.BaseFragment;
import com.example.datingapp.login.StartupActivity;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity implements EventListener {

    protected ActivityComponent activityComponent;

    @Inject
    EventBus eventBus;

    protected abstract void injectActivity(ActivityComponent component);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ApplicationComponent appComponent = ((MessengerApplication) getApplication())
                .getApplicationComponent();
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(appComponent)
                .build();
        injectActivity(activityComponent);

        eventBus.addListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onEventOccurred(Event e) {
        if (e instanceof NotAuthenticatedEvent) {
            MessengerApplication app = (MessengerApplication) getApplication();
            app.setAuthenticatedUser(null);
            startStartupActivity();
        }
    }

    @Override
    protected void onDestroy() {
        eventBus.removeListener(this);
        super.onDestroy();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    private void startStartupActivity() {
        Intent intent = new Intent(this, StartupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void showInitialFragment(BaseFragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment, fragment.getUniqueTag());
        ft.commit();
    }

    protected void showNextFragment(BaseFragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_from_right, R.anim.fade_out, R.anim.fade_in, R.anim.slide_to_right);
        ft.replace(R.id.fragment_container, fragment, fragment.getUniqueTag());
        ft.addToBackStack(null).commit();
    }
}
