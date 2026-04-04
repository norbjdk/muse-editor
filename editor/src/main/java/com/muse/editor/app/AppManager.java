package com.muse.editor.app;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppManager {
    private final static AppManager instance = new AppManager();
    private final BooleanProperty isConnected = new SimpleBooleanProperty(false);

    public static AppManager getInstance() {
        return instance;
    }

    private AppManager() {
        runServerMonitor();
    }

    private void runServerMonitor() {
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        final String urlToCheck = "http://localhost:8080";

        scheduler.scheduleAtFixedRate(() -> {
            try {
                URL url = new URL(urlToCheck);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(5000);
                connection.connect();

                int code = connection.getResponseCode();
                System.out.println("[" + new java.util.Date() + "] Status: " + code);
                isConnected.set(true);
                connection.disconnect();
            } catch (Exception e) {
                isConnected.set(false);
                System.out.println("[" + new java.util.Date() + "] Serwer nie odpowiada: " + e.getMessage());
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public BooleanProperty isConnected() {
        return isConnected;
    }
}
