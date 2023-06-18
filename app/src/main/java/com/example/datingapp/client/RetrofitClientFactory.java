package com.example.datingapp.client;

import com.example.datingapp.MessengerApplicationImpl;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientFactory {

    private static final String API_BASE_URL = "http://" + MessengerApplicationImpl.SERVER_ADDRESS + "/";

    private final OkHttpClient okHttpClient;

    @Inject
    public RetrofitClientFactory(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public Retrofit createRetrofitClient() {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        return builder
                .client(okHttpClient)
                .build();
    }
}
