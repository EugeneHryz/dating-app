package com.example.datingapp.client;

import android.text.TextUtils;

import com.example.datingapp.client.event.NotAuthenticatedEvent;
import com.example.datingapp.client.token.TokenService;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.system.TimeManager;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpDate;

public class RequestInterceptor implements Interceptor {

    private static final String DATE_RESPONSE_HEADER = "Date";

    private final TokenService tokenService;
    private final EventBus eventBus;
    private final TimeManager timeManager;

    @Inject
    public RequestInterceptor(TokenService tokenService, EventBus eventBus, TimeManager timeManager) {
        this.tokenService = tokenService;
        this.eventBus = eventBus;
        this.timeManager = timeManager;
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

        if (!timeManager.getHasServerTime()) {
            Headers headers = response.headers();
            updateTime(headers);
        }
        if (response.code() == Constants.HTTP_FORBIDDEN) {
            eventBus.broadcast(new NotAuthenticatedEvent());
        }
        return response;
    }

    private void updateTime(Headers headers) {
        String timeHeaderValue = headers.get(DATE_RESPONSE_HEADER);

        if (!TextUtils.isEmpty(timeHeaderValue)) {
            Date parsedTime = HttpDate.parse(timeHeaderValue);
            System.out.println("Received server time: " + parsedTime);
            if (parsedTime != null) {
                timeManager.updateServerTime(parsedTime.getTime());
            }
        }
    }
}
