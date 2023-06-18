package com.example.datingapp.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.MessengerApplication;
import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;
import com.example.datingapp.activity.RequestCode;
import com.example.datingapp.client.auth.AuthenticatedUser;
import com.example.datingapp.lifecycle.exception.ServiceException;
import com.example.datingapp.messaging.MessageMapper;
import com.example.datingapp.searchpeople.SearchPeopleActivity;
import com.example.datingapp.service.LocationUpdateService;
import com.example.datingapp.service.MessagingService;
import com.example.datingapp.system.NetworkManager;
import com.example.datingapp.system.TimeManager;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

public class HomeActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = HomeActivity.class.getName();

    private HomeViewModel viewModel;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton searchPeopleButton;

    private boolean requestedEnableLocation;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    MessageMapper messageMapper;
    @Inject
    TimeManager timeManager;
    @Inject
    NetworkManager networkManager;

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        drawerLayout = findViewById(R.id.drawer_layout);
        searchPeopleButton = findViewById(R.id.open_search_activity);
        Toolbar toolbar = setupToolbar();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer_desc,
                R.string.close_drawer_desc) {
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        searchPeopleButton.setOnClickListener(v -> startSearchPeopleActivity());

        showInitialFragment(new ChatsFragment());
        startMessagingService();
        startNetworkManagerService();
        getLocationPermission();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        if (!requestedEnableLocation) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "about to check location settings");
                checkLocationSettings();
            }
        }
        requestedEnableLocation = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RequestCode.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "User granted the permission");
                checkLocationSettings();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.RESOLVE_LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                startLocationService();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        stopLocationService();
        stopMessagingService();
        stopNetworkManagerService();
    }

    private void startMessagingService() {
        Intent intent = new Intent(this, MessagingService.class);
        MessengerApplication application = (MessengerApplication) getApplication();
        AuthenticatedUser user = application.getAuthenticatedUser();
        intent.putExtra(MessagingService.USER_TOPIC_KEY, user.getUsername());

        startService(intent);
    }

    private void startNetworkManagerService() {
        try {
            networkManager.startService();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            checkLocationSettings();
            Log.d(TAG, "Permission already granted");
        } else {
            Log.d(TAG, "About to request permission");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    RequestCode.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationService() {
        Intent intent = new Intent(this, LocationUpdateService.class);
        startService(intent);
    }

    private void stopLocationService() {
        Intent intent = new Intent(this, LocationUpdateService.class);
        stopService(intent);
    }

    private void stopMessagingService() {
        Intent intent = new Intent(this, MessagingService.class);
        stopService(intent);
    }

    private void stopNetworkManagerService() {
        try {
            networkManager.stopService();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder settingsRequestBuilder = new LocationSettingsRequest.Builder();
        settingsRequestBuilder.addLocationRequest(createLocationRequest());
        settingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(
                settingsRequestBuilder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            startLocationService();
        });
        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                try {
                    resolvableApiException.startResolutionForResult(HomeActivity.this,
                            RequestCode.RESOLVE_LOCATION_SETTINGS_REQUEST);

                    requestedEnableLocation = true;
                } catch (IntentSender.SendIntentException ex) {
                    Log.e(TAG, "Unable to request settings change", ex);
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest request  = LocationRequest.create();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
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

    private void startSearchPeopleActivity() {
        Intent intent = new Intent(this, SearchPeopleActivity.class);
        startActivity(intent);
    }
}