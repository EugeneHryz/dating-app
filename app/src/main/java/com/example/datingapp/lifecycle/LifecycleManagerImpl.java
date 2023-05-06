package com.example.datingapp.lifecycle;

import com.example.datingapp.event.EventBus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import javax.inject.Inject;

public class LifecycleManagerImpl implements LifecycleManager {

    private static final Logger logger = Logger.getLogger(LifecycleManagerImpl.class.getName());

    private final EventBus eventBus;
    private final List<Service> services;
    private final List<ExecutorService> executorServices;

    private final CountDownLatch dbLatch = new CountDownLatch(1);
    private final CountDownLatch servicesLatch = new CountDownLatch(1);
    private final CountDownLatch shutdownLatch = new CountDownLatch(1);

    private volatile State state = State.STARTING;

    @Inject
    public LifecycleManagerImpl(EventBus eventBus) {
        this.eventBus = eventBus;

        services = new CopyOnWriteArrayList<>();
        executorServices = new CopyOnWriteArrayList<>();
    }

    @Override
    public void registerService(Service service) {
        services.add(service);
    }

    @Override
    public void registerForShutdown(ExecutorService executorService) {
        executorServices.add(executorService);
    }

    @Override
    public void stopServices() {

    }

    @Override
    public void waitForDatabase() throws InterruptedException {
        dbLatch.await();
    }

    @Override
    public void waitForStartup() throws InterruptedException {
        servicesLatch.await();
    }

    @Override
    public void waitForShutdown() throws InterruptedException {
        shutdownLatch.await();
    }
}
