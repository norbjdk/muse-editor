package com.muse.editor.develop.app;

import com.muse.editor.develop.core.EventBus;
import com.muse.editor.develop.core.project.ProjectManager;
import com.muse.editor.develop.core.user.CurrentUserService;
import com.muse.editor.develop.core.user.TokenStorage;
import com.muse.editor.develop.core.user.User;
import com.muse.editor.develop.core.user.UserManager;
import com.muse.editor.develop.model.event.user.LoginSuccessEvent;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppManager {
    private final static AppManager instance = new AppManager();
    private final BooleanProperty isConnected = new SimpleBooleanProperty(false);

    private final UserManager    userManager    = UserManager.getInstance();
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private final CurrentUserService currentUserService = CurrentUserService.getCurrentUserService();

    private Stage stage;

    public static AppManager getInstance() {
        return instance;
    }

    private AppManager() {
        runServerMonitor();
    }

    public void init(Stage stage) {
        this.stage = stage;

        if (TokenStorage.isLoggedIn()) {
            User user = CurrentUserService.getCurrentUser();

            if (user != null) {
                System.out.println("User restored: " + user.getUsername());

                EventBus.getInstance().publish(new LoginSuccessEvent(user));
            }
        }
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

                Platform.runLater(() -> isConnected.set(true));

                connection.disconnect();
            } catch (Exception e) {
                Platform.runLater(() -> isConnected.set(false));
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    /**
     * ============ USER =============
     */

    public User getCurrentUser() throws IOException {
        return userManager.getCurrentUser();
    }

    public User getUserByUsername(String username) throws IOException {
        return userManager.getUserByUsername(username);
    }

    public boolean IsUserLoggedIn() {
        return currentUserService.isLoggedIn();
    }

    public void logoutUser() {
        currentUserService.logout();
    }

    public BooleanProperty isConnected() {
        return isConnected;
    }
}
