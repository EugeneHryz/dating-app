package com.example.datingapp.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.datingapp.MessengerApplication;
import com.example.datingapp.R;
import com.example.datingapp.client.Constants;
import com.example.datingapp.client.geolocation.GeolocationService;
import com.example.datingapp.client.geolocation.LocationRequestDto;
import com.example.datingapp.home.HomeActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.time.Instant;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationUpdateService extends Service {

    private static final String TAG = LocationUpdateService.class.getName();

    private static final String NOTIFICATION_CHANNEL_ID = "com.example.datingapp.LOCATION_NOTIFICATION_CHANNEL";
    private static final String NOTIFICATION_CHANNEL_NAME = "Location Service Channel";
    private static final int NOTIFICATION_ID = 10;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Inject
    GeolocationService geolocationService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        MessengerApplication app = (MessengerApplication) getApplication();
        app.getApplicationComponent().inject(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Log.d(TAG, "Coordinates: " + location.getLatitude() + " " + location.getLongitude());
                        sendLocationUpdate(location);
                    }
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        prepareForegroundNotification();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
                    Looper.getMainLooper());
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void sendLocationUpdate(Location location) {
        LocationRequestDto locationRequest = mapToLocationRequestDto(location);

        geolocationService.updateLocation(locationRequest).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != Constants.HTTP_SUCCESS) {
                    StringBuilder messageBuilder = new StringBuilder();
                    messageBuilder.append(getString(R.string.unable_to_update_location))
                            .append(" HTTP status: ")
                            .append(response.code());

                    Toast.makeText(LocationUpdateService.this,
                                    messageBuilder.toString(), Toast.LENGTH_LONG)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                StringBuilder messageBuilder = new StringBuilder();
                messageBuilder.append(getString(R.string.unable_to_get_response))
                        .append(" Error: ")
                        .append(t.getMessage());

                Toast.makeText(LocationUpdateService.this,
                                messageBuilder.toString(), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private LocationRequestDto mapToLocationRequestDto(Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        Instant timestamp = Instant.ofEpochMilli(location.getTime());

        return new LocationRequestDto(latitude, longitude, timestamp);
    }

    private void prepareForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }
}