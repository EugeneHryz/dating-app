package com.example.datingapp.lifecycle.event;

import com.example.datingapp.event.Event;
import com.example.datingapp.lifecycle.LifecycleManager;

public class LifecycleStateEvent extends Event {

    private final LifecycleManager.State state;

    public LifecycleStateEvent(LifecycleManager.State state) {
        this.state = state;
    }

    public LifecycleManager.State getState() {
        return state;
    }
}
