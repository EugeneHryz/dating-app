package com.example.datingapp.system;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.example.datingapp.event.EventBus;
import com.example.datingapp.lifecycle.Service;
import com.example.datingapp.lifecycle.exception.ServiceException;
import com.example.datingapp.system.event.NetworkConnectivityEvent;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NetworkManager implements Service {

    private static final String TAG = NetworkManager.class.getName();

    private final EventBus eventBus;
    private final Application application;
    private final ConnectivityManager connectivityManager;

    private NetworkStateReceiver networkStateReceiver;

    private final AtomicBoolean used = new AtomicBoolean(false);

    @Inject
    public NetworkManager(EventBus eventBus, Application application) {
        this.eventBus = eventBus;
        this.application = application;

        connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void startService() throws ServiceException {
        if (used.getAndSet(true)) return;

        networkStateReceiver = new NetworkStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        if (SDK_INT >= 23) filter.addAction(ACTION_DEVICE_IDLE_MODE_CHANGED);
        application.registerReceiver(networkStateReceiver, filter);
    }

    @Override
    public void stopService() throws ServiceException {
        if (networkStateReceiver != null) {
            application.unregisterReceiver(networkStateReceiver);
        }
    }

    private boolean isConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            return nwInfo != null && nwInfo.isConnected();
        }
    }

    private void broadcastNetworkEvent() {
        eventBus.broadcast(new NetworkConnectivityEvent(isConnected()));
    }

    public class NetworkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastNetworkEvent();
        }
    }
}
