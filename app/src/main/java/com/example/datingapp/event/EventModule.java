package com.example.datingapp.event;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EventModule {

    @Singleton
    @Provides
    public EventBus provideEventBus(EventBusImpl eventBus) {
        return eventBus;
    }
}
