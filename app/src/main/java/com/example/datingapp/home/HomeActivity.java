package com.example.datingapp.home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;
import com.example.datingapp.login.StartupActivity;
import com.example.datingapp.geolocation.GeolocationService;
import com.example.datingapp.geolocation.LocationRequestDto;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;


import java.sql.SQLOutput;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String TAG = HomeActivity.class.getName();

    private HomeViewModel viewModel;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private Location mLastLocation;
    private boolean mLocationPermissionGranted;
    private LocationRequest mLocationRequest;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = setupToolbar();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer_desc,
                R.string.close_drawer_desc) {
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //Тут крч в методе задаешь параметры запроса на определение локации
        createLocationRequest();
        //Этот мужик срабатывает когда запрос полетел с кайфом и тут обрабатывается результат
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location previousLastLocation = mLastLocation;
                for (Location location : locationResult.getLocations()) {
                    if (location != null && (mLastLocation == null ||
                            location.distanceTo(mLastLocation) > 10)) {
                        mLastLocation = location;
                        String str = String.valueOf(mLastLocation.getLongitude()) + "PIMPIM" + String.valueOf(mLastLocation.getLatitude());
                        Toast toast = Toast.makeText(getApplicationContext(),
                                str, Toast.LENGTH_SHORT);
                        toast.show();
                        //Тут когда бэк буит готов раскоментить пэрашу и должно будет работать
//                        sendRequestToServer(mLastLocation);
                    }
                }
//                if(previousLastLocation == null || (previousLastLocation != null && (previousLastLocation.getLatitude() != mLastLocation.getLatitude() ||
//                        previousLastLocation.getLongitude() != mLastLocation.getLongitude()) && (previousLastLocation.distanceTo(mLastLocation) > 5))) {
//                   тут вывод пиздануть(запрос на сервер) можно а не внутри цикла, тут просто больше условий
//                }
            }
        };
        getLocationPermission();

        viewModel.getState().observe(this, this::handleStateChange);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Тут важный момент, крч когда чел покидает активность и улетает в настройки с целью включить геолокацию,
        // после того как он вернется надо опять проверять включил ли он ее, если делать без сервиса то все по кайфе будет работать,
        // а если с сервисом то у тебя запустится их несолько, в этом проблем в общем
//        startLocationUpdates();
        //Можно использовать этот кусок вместо startLocationUpdates, но там тоже какая-то беда была, уже не помню
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                System.out.println("Onresume location updates");
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback,
                        Looper.getMainLooper());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Service was stopped");
//        stopService(new Intent(HomeActivity.this, LocationUpdateService.class));
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // геолокация не включена на устройстве, сообщаем об этом пользователю
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Геолокация выключена")
                        .setMessage("Хотите включить геолокацию?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else{
                System.out.println("StartServiceFromActivity");
                //Запуск сервиса вот тут я делал
//                startService(new Intent(HomeActivity.this, LocationUpdateService.class));
                System.out.println("UPDATING LOCATION...");
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    Looper.getMainLooper());
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        if (mLocationPermissionGranted) {
            startLocationUpdates();
        }
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

    private void handleStateChange(HomeViewModel.State state) {
        if (state == HomeViewModel.State.NOT_AUTHENTICATED) {
            startStartupActivity();
        }
    }

    private void startStartupActivity() {
        Intent intent = new Intent(this, StartupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void sendRequestToServer(Location location) {
        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());
        LocationRequestDto locationRequestDto = new LocationRequestDto(latitude, longitude);
//        geolocationService.updateLocation(locationRequestDto).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d(TAG, "Failed to perform an update location request", t);
//            }
//        });
    }
}