package com.example.datingapp.geolocation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GeolocationService {
    @POST("/geolocation/update")
    Call<Void> updateLocation(@Body LocationRequestDto locationRequestDto);
}
