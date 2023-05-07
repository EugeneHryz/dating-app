package com.example.datingapp.client;

import com.example.datingapp.client.event.NotAuthenticatedEvent;
import com.example.datingapp.client.token.TokenService;
import com.example.datingapp.event.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {

    private final TokenService tokenService;
    private final EventBus eventBus;

    @Inject
    public RequestInterceptor(TokenService tokenService, EventBus eventBus) {
        this.tokenService = tokenService;
        this.eventBus = eventBus;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String authToken = tokenService.getToken();
        if (authToken != null) {
            Request.Builder requestBuilder = request.newBuilder()
                    .addHeader(Constants.AUTHORIZATION_HEADER,
                            Constants.BEARER_TOKEN_PREFIX + authToken);
            request = requestBuilder.build();
        }

        Response response = chain.proceed(request);
        if (response.code() == Constants.HTTP_FORBIDDEN) {
            eventBus.broadcast(new NotAuthenticatedEvent());
        }
        return response;
    }
}
