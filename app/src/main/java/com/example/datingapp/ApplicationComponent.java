package com.example.datingapp;

import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.client.ClientModule;
import com.example.datingapp.common.TextFormatter;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventModule;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.io.IoExecutorModule;
import com.example.datingapp.lifecycle.LifecycleManager;
import com.example.datingapp.lifecycle.LifecycleModule;
import com.example.datingapp.messaging.MessageMapper;
import com.example.datingapp.messaging.MessagingModule;
import com.example.datingapp.service.LocationUpdateService;
import com.example.datingapp.service.MessagingService;
import com.example.datingapp.system.AndroidSystemModule;
import com.example.datingapp.system.NetworkManager;
import com.example.datingapp.system.TimeManager;
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
                        MessagingModule.class,
                        AppModule.class })
public interface ApplicationComponent {

    ViewModelProvider.Factory viewModelFactory();

    LifecycleManager lifecycleManager();

    EventBus eventBus();

    @IoExecutor
    Executor ioExecutor();

    TextFormatter textFormatter();

    MessageMapper messageMapper();

    TimeManager timeManager();

    NetworkManager networkManager();

    void inject(MessengerApplicationImpl app);

    void inject(LocationUpdateService service);

    void inject(MessagingService service);
}
