package com.example.datingapp;

import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.client.ClientModule;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventModule;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.io.IoExecutorModule;
import com.example.datingapp.lifecycle.LifecycleManager;
import com.example.datingapp.lifecycle.LifecycleModule;
import com.example.datingapp.service.LocationUpdateService;
import com.example.datingapp.system.AndroidSystemModule;
import com.example.datingapp.viewmodel.ViewModelModule;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {  EventModule.class,
                        LifecycleModule.class,
                        IoExecutorModule.class,
                        AndroidSystemModule.class,
                        ViewModelModule.class,
                        ClientModule.class,
                        AppModule.class })
public interface ApplicationComponent {

    ViewModelProvider.Factory viewModelFactory();

    LifecycleManager lifecycleManager();

    EventBus eventBus();

    @IoExecutor
    Executor ioExecutor();

    void inject(MessengerApplicationImpl app);

    void inject(LocationUpdateService service);
}
