package com.example.datingapp.client;

import com.example.datingapp.MessengerApplicationImpl;
import com.example.datingapp.client.auth.AuthenticationService;
import com.example.datingapp.client.chat.ChatService;
import com.example.datingapp.client.dictionary.DictionaryService;
import com.example.datingapp.client.geolocation.GeolocationService;
import com.example.datingapp.client.token.TokenService;
import com.example.datingapp.client.token.TokenServiceImpl;
import com.example.datingapp.client.user.UserService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

@Module
public class ClientModule {

    private static final String WEBSOCKET_ENDPOINT = "ws://" + MessengerApplicationImpl.SERVER_ADDRESS +
            "/api/chats/websocket";

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(RequestInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofitService(RetrofitClientFactory clientFactory) {
        return clientFactory.createRetrofitClient();
    }

    @Singleton
    @Provides
    public StompClient provideStompClient(OkHttpClient okHttpClient) {
        return Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_ENDPOINT,
                null, okHttpClient)
                .withClientHeartbeat(10000)
                .withServerHeartbeat(0);
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

    @Provides
    public DictionaryService provideDictionaryService(Retrofit retrofit) {
        return retrofit.create(DictionaryService.class);
    }

    @Provides
    public ChatService provideChatService(Retrofit retrofit) {
        return retrofit.create(ChatService.class);
    }

    @Provides
    public UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }
}
