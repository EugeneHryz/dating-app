package com.example.datingapp.event;

@FunctionalInterface
public interface EventListener {

    void onEventOccurred(Event e);
}
