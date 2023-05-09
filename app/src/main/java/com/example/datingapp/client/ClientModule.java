package com.example.datingapp.client;

import com.example.datingapp.client.auth.AuthenticationService;
import com.example.datingapp.client.geolocation.GeolocationService;
import com.example.datingapp.client.token.TokenService;
import com.example.datingapp.client.token.TokenServiceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ClientModule {

    @Provides
    @Singleton
    public Retrofit provideRetrofitService(RetrofitClientFactory clientFactory) {
        return clientFactory.createRetrofitClient();
    }

    @Provides
    public TokenService provideSessionService(TokenServiceImpl tokenService) {
        return tokenService;
    }

    @Provides
    public AuthenticationService provideAuthenticationService(Retrofit retrofit) {
        return retrofit.create(AuthenticationService.class);
    }

    @Provides
    public GeolocationService provideGeolocationService(Retrofit retrofit) {
        return retrofit.create(GeolocationService.class);
    }
}
