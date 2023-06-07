package com.example.datingapp.client.geolocation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GeolocationService {

    @POST("/api/geolocation")
    Call<Void> updateLocation(@Body LocationRequestDto locationRequestDto);
}
