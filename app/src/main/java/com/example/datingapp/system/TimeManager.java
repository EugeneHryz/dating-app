package com.example.datingapp.system;

import android.os.SystemClock;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimeManager {

    private long lastServerTime;
    private long lastElapsedTime;

    private boolean hasServerTime;

    @Inject
    public TimeManager() {
    }

    public synchronized long getApproximateServerTime() {
        if (!hasServerTime) {
            throw new IllegalStateException("Server time wasn't initialized");
        }
        return lastServerTime + (SystemClock.elapsedRealtime() - lastElapsedTime);
    }

    public synchronized void updateServerTime(long serverTime) {
        lastServerTime = serverTime;
        lastElapsedTime = SystemClock.elapsedRealtime();
        hasServerTime = true;
    }

    public boolean getHasServerTime() {
        return hasServerTime;
    }
}
