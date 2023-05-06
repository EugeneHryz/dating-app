package com.example.datingapp.home;

import androidx.lifecycle.ViewModel;

import com.example.datingapp.event.Event;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventListener;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel implements EventListener {

    private final EventBus eventBus;

    @Inject
    public HomeViewModel(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.addListener(this);
    }

    @Override
    protected void onCleared() {
        eventBus.removeListener(this);
    }

    @Override
    public void onEventOccurred(Event e) {
    }
}
