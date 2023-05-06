package com.example.datingapp.event;

public interface EventBus {

    void addListener(EventListener listener);

    void removeListener(EventListener listener);

    void broadcast(Event event);
}
