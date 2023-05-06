package com.example.datingapp.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.datingapp.ApplicationComponent;
import com.example.datingapp.MessengerApplication;
import com.example.datingapp.R;
import com.example.datingapp.fragment.BaseFragment;

public abstract class BaseActivity extends AppCompatActivity {

    protected ActivityComponent activityComponent;

    protected abstract void injectActivity(ActivityComponent component);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ApplicationComponent appComponent = ((MessengerApplication) getApplication())
                .getApplicationComponent();
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(appComponent)
                .build();
        injectActivity(activityComponent);

        super.onCreate(savedInstanceState);
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

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
