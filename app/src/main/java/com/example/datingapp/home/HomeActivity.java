package com.example.datingapp.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;

import javax.inject.Inject;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getName();

    private HomeViewModel viewModel;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = setupToolbar();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer_desc,
                R.string.close_drawer_desc) {
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

//        showInitialFragment(new ContactListFragment());
    }

    private Toolbar setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        return toolbar;
    }
}