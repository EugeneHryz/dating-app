package com.example.datingapp.lifecycle;

import java.util.concurrent.ExecutorService;

public interface LifecycleManager {

    enum StartResult {
        ALREADY_RUNNING,
        DB_ERROR,
        SERVICE_ERROR,
        SUCCESS
    }

    enum State {
        STARTING,
        STARTING_SERVICES,
        STARTED,
        STOPPING
    }

    void registerService(Service service);

    void registerForShutdown(ExecutorService executorService);

    void stopServices();

    void waitForDatabase() throws InterruptedException;

    void waitForStartup() throws InterruptedException;

    void waitForShutdown() throws InterruptedException;
}
