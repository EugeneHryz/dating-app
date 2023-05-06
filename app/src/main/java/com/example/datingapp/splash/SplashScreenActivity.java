package com.example.datingapp.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;
import com.example.datingapp.login.StartupActivity;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen_activity);
        getWindow().setExitTransition(new Fade());

        new Handler().postDelayed(() -> {
            startNextActivity(StartupActivity.class);
            supportFinishAfterTransition();
        }, 1000);
    }

    private void startNextActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}