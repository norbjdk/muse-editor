package com.muse.editor.app;

import com.muse.editor.core.collection.CollectionService;
import com.muse.editor.core.edit.CursorModel;
import com.muse.editor.core.edit.EditorService;
import com.muse.editor.core.edit.EditorState;
import com.muse.editor.core.edit.ScoreManager;
import com.muse.editor.core.io.FileService;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.core.project.ProjectService;
import com.muse.editor.core.user.*;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.user.LoggedInEvent;
import com.muse.editor.event.user.LoggedOutEvent;
import com.muse.editor.event.view.ChangeViewEvent;
import com.muse.editor.event.view.ShowLoginWindow;
import com.muse.editor.event.view.ShowMainWindow;
import com.muse.editor.gui.manager.ViewManager;
import com.muse.editor.gui.model.Viewable;
import com.muse.editor.util.Debug;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class AppManager {
    private static AppManager instance;
    private final Stage primaryStage;

    public enum ServerStatus {
        RUNNING,
        DOWN
    }

    private final ObjectProperty<ServerStatus> serverStatus = new SimpleObjectProperty<>();

    private final ExecutorService loginExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        t.setName("Login-Worker-Thread");
        return t;
    });

    private final ScheduledExecutorService monitorScheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        t.setName("Server-Monitor-Thread");
        return t;
    });

    private AppManager(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        AppConfig.load();

        setupEventListeners();

        monitorServer();
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(LoggedInEvent.class, loggedInEvent -> {
            Platform.runLater(() -> {
                Debug.pass("User successfully logged in. Switching to MainWindow.");

                ClientService.getInstance().connect();

                showMainViewPlaceholder();
            });
        });

        EventBus.getInstance().subscribe(LoggedOutEvent.class, loggedOutEvent -> {
            Platform.runLater(() -> {
                Debug.check("User logged out. Redirecting to LoginWindow.");
                showLoginView();
            });
        });
    }

    public static synchronized void init(final Stage primaryStage) {
        if (instance != null) {
            throw new IllegalStateException("AppManager already initialized");
        }
        if (primaryStage == null) {
            throw new IllegalArgumentException("Stage can not be null");
        }
        instance = new AppManager(primaryStage);
    }

    public void startApp() {
        initServices();

        if (!TokenStorage.isLoggedIn()) {
            Debug.check("There is no user token in storage. Redirecting to login.");
            showLoginView();
            return;
        }

        Debug.check("Found " + TokenStorage.getToken() + " token in storage. Verifying session...");

        loginExecutor.submit(autoLogin());
    }

    private void showLoginView() {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Login to MUSE Editor");
        primaryStage.setWidth(AppConfig.getLoginWindowWidth());
        primaryStage.setHeight(AppConfig.getLoginWindowHeight());
        primaryStage.setMaximized(false);

        EventBus.getInstance().publish(new ShowLoginWindow());

        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    private void showMainViewPlaceholder() {
        primaryStage.setResizable(true);
        primaryStage.setTitle("MUSE Editor");
        primaryStage.setWidth(AppConfig.getMainWindowMinWidth());
        primaryStage.setHeight(AppConfig.getMainWindowMinHeight());
        primaryStage.setMaximized(true);

        EventBus.getInstance().publish(new ShowMainWindow());

        primaryStage.show();
        primaryStage.centerOnScreen();

        Platform.runLater(() -> {
            EventBus.getInstance().publish(new ChangeViewEvent(Viewable.Name.HOME));
        });
    }

    private void initServices() {
        UserManager.getInstance();
        AuthService.getInstance();
        ClientService.getInstance();
        FileService.getInstance();
        ViewManager.getInstance();
        ProjectManager.getInstance();
        ProjectService.getInstance();
        CollectionService.getInstance();
        CursorModel.getInstance();
        EditorState.getInstance();
        ScoreManager.getInstance();
        EditorService.getInstance();
        Debug.pass("All core services pre-initialized successfully.");
    }

    private Runnable autoLogin() {
        return () -> {
            try {
                final User user = UserService.getInstance().getCurrentUser();

                if (user != null) {
                    Platform.runLater(() -> {
                        UserManager.getInstance().setCurrentUser(user);
                        EventBus.getInstance().publish(new LoggedInEvent());
                    });
                } else {
                    Debug.fail("Session token invalid. Redirecting to login screen.");
                    Platform.runLater(() -> {
                        TokenStorage.clear();
                        showLoginView();
                    });
                }
            } catch (Exception e) {
                Debug.fail("Network error during auto-login: " + e.getMessage());
                Platform.runLater(this::showLoginView);
            }
        };
    }

    private void monitorServer() {
        Debug.pass("Server monitor started. Listening to: " + AppConfig.serverUrlProperty().get());
        monitorScheduler.scheduleAtFixedRate(() -> {
            try {
                URL url = URI.create(AppConfig.serverUrlProperty().get()).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(5000);
                connection.connect();

                Platform.runLater(() -> serverStatus.set(ServerStatus.RUNNING));

                connection.disconnect();
            } catch (Exception e) {
                Platform.runLater(() -> serverStatus.set(ServerStatus.DOWN));

            }
        }, 0, AppConfig.getServerMonitorDelay(), TimeUnit.SECONDS);
    }

    public void shutdownExecutors() {
        loginExecutor.shutdown();
        monitorScheduler.shutdown();
    }

    public static AppManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppManager has not been initialized yet");
        }
        return instance;
    }

    public ObjectProperty<ServerStatus> serverStatusProperty() {
        return serverStatus;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
