package com.example.datingapp.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.datingapp.MessengerApplication;
import com.example.datingapp.event.Event;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventListener;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.messaging.IncomingMessageHandler;
import com.example.datingapp.system.event.NetworkConnectivityEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

import ua.naiksoftware.stomp.StompClient;

public class MessagingService extends Service implements EventListener {

    private static final String TAG = MessagingService.class.getName();

    public static final String USER_TOPIC_KEY = "user_topic_key";

    @Inject
    StompClient stompClient;
    @Inject
    @IoExecutor
    Executor ioExecutor;
    @Inject
    IncomingMessageHandler messageHandler;
    @Inject
    EventBus eventBus;

    private final Lock lock = new ReentrantLock();
    private boolean connected;
    private String userTopic;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();

        MessengerApplication app = (MessengerApplication) getApplication();
        app.getApplicationComponent().inject(this);
        eventBus.addListener(this);

        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;
                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    connected = false;
                    Log.d(TAG, "Stomp connection closed");
                    break;
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userTopic = intent.getStringExtra(USER_TOPIC_KEY);

        connectAndSubscribeForTopic();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    @Override
    public void onEventOccurred(Event e) {
        if (e instanceof NetworkConnectivityEvent) {
            NetworkConnectivityEvent event = (NetworkConnectivityEvent) e;
            if (event.isConnected()) {
                connectAndSubscribeForTopic();
            } else {
                disconnect();
            }
        }
    }

    public class LocalBinder extends Binder {

        @SuppressLint("CheckResult")
        public void sendSimpleMessage(String message) {
            ioExecutor.execute(() -> {
                Log.d(TAG, "Sending a message to /app/messages\nMessage: " + message);

                stompClient.send("/app/messages", message).subscribe(() -> {
                    Log.d(TAG, "Sent message to /app/messages");
                }, e -> {
                    Log.e(TAG, e.toString());
                });
            });
        }

        @SuppressLint("CheckResult")
        public void markMessageAsRead(String message) {
            ioExecutor.execute(() -> {
                Log.d(TAG, "Sending a message to /app/messages/read\nMessage: " + message);

                stompClient.send("/app/messages/read", message).subscribe(() -> {
                    Log.d(TAG, "Sent message to /app/messages/read");
                }, e -> {
                    Log.e(TAG, e.toString());
                });
            });
        }
    }

    private void disconnect() {

        ioExecutor.execute(() -> {
            lock.lock();
            try {
                if (connected) {
                    stompClient.disconnect();
                    connected = false;
                }
            } finally {
                lock.unlock();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void connectAndSubscribeForTopic() {
        ioExecutor.execute(() -> {
            lock.lock();
            try {
                if (connected) {
                    return;
                }
                Log.d(TAG, "connecting to stomp endpoint...");
                stompClient.connect();
                connected = true;

                Log.d(TAG, "registering topic listener...");
                stompClient.topic("/users/" + userTopic).subscribe(stompMessage -> {
                    messageHandler.handleIncomingMessage(stompMessage.getPayload());
                    Log.d(TAG, "Received message from /users/" + userTopic + "\n" + stompMessage.getPayload());
                }, e -> {
                    Log.e(TAG, "Error when subscribing to topic " + e.toString());
                });
            } finally {
                lock.unlock();
            }
        });
    }
}
