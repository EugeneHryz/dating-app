package com.example.datingapp.system.event;

import com.example.datingapp.event.Event;

public class NetworkConnectivityEvent extends Event {

    private final boolean connected;

    public NetworkConnectivityEvent(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
