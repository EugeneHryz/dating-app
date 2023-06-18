package com.example.datingapp.client.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("/api/user/getUsersByLocation")
    Call<List<UserNearbyDto>> findUsersWithinRadius(@Query("radius") int radius,
                                                    @Query("country") String countryName);
}
