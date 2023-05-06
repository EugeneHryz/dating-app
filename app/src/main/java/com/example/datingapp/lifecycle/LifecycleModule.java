package com.example.datingapp.lifecycle;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LifecycleModule {

    @Singleton
    @Provides
    LifecycleManager provideLifecycleManager(LifecycleManagerImpl lifecycleManager) {
        return lifecycleManager;
    }
}
