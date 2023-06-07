package com.example.datingapp.client;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientFactory {

    private static final String API_BASE_URL = "http://192.168.43.99:8080/";

    private final RequestInterceptor interceptor;

    @Inject
    public RetrofitClientFactory(RequestInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Retrofit createRetrofitClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        return builder
                .client(httpClient)
                .build();
    }
}
