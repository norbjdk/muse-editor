package com.muse.editor.app;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppManager {
    private final static AppManager instance = new AppManager();
    private final BooleanProperty isConnected = new SimpleBooleanProperty(false);

    private Stage stage;

    public static AppManager getInstance() {
        return instance;
    }

    private AppManager() {
        runServerMonitor();
    }

    public void init(Stage stage) {
        this.stage = stage;
    }

    private void runServerMonitor() {
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        final String urlToCheck = "http://localhost:8080";

        scheduler.scheduleAtFixedRate(() -> {
            try {
                URL url = new URL(urlToCheck);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(5000);
                connection.connect();

                Platform.runLater(() -> isConnected.set(true));

                connection.disconnect();
            } catch (Exception e) {
                Platform.runLater(() -> isConnected.set(false));
            }
        }, 0, 10, TimeUnit.SECONDS);
    }


    public BooleanProperty isConnected() {
        return isConnected;
    }
}
