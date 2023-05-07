package com.example.datingapp.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.client.event.NotAuthenticatedEvent;
import com.example.datingapp.event.Event;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventListener;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel implements EventListener {

    private final EventBus eventBus;

    enum State {
        NOT_AUTHENTICATED
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();

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
        if (e instanceof NotAuthenticatedEvent) {
            state.postValue(State.NOT_AUTHENTICATED);
        }
    }

    public MutableLiveData<State> getState() {
        return state;
    }
}
