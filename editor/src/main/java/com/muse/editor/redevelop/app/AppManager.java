package com.muse.editor.redevelop.app;

import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.view.ChangeViewEvent;
import com.muse.editor.redevelop.gui.manager.ViewManager;
import com.muse.editor.redevelop.gui.model.Viewable;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppManager {
    private final static int MONITOR_DELAY = 8;

    private final ViewManager viewManager = ViewManager.getInstance();

    public enum ServerStatus {
        RUNNING(true),
        DOWN(false);

        private final boolean value;

        ServerStatus(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }
    }
    private static final AppManager instance = new AppManager();

    private final ObjectProperty<ServerStatus> serverStatus = new SimpleObjectProperty<>();

    public static AppManager getInstance() {
        return instance;
    }

    private AppManager() {
        runServerMonitor();
        setupEventListener();
    }

    public void init() {
        Platform.runLater(() -> {
            EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.HOME));
        });
    }

    private void setupEventListener() {
        EventBus.getInstance().subscribe(ChangeViewEvent.class, event -> {
            if (event.getViewName() != null) {
                viewManager.changeView(event.getViewName());
            }
        });
    }

    private void runServerMonitor() {
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        final String urlToCheck = "http://127.0.0.1:8080";

        scheduler.scheduleAtFixedRate(() -> {
            try {
                URL url = new URL(urlToCheck);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(5000);
                connection.connect();

                Platform.runLater(() -> serverStatus.set(ServerStatus.RUNNING));

                connection.disconnect();
            } catch (Exception e) {
                Platform.runLater(() -> serverStatus.set(ServerStatus.DOWN));
            }
        }, 0, MONITOR_DELAY, TimeUnit.SECONDS);
    }
}
