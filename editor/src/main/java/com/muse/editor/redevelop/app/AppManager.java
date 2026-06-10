package com.muse.editor.redevelop.app;

import com.muse.editor.redevelop.core.edit.CursorModel;
import com.muse.editor.redevelop.core.edit.EditorService;
import com.muse.editor.redevelop.core.edit.EditorState;
import com.muse.editor.redevelop.core.edit.ScoreManager;
import com.muse.editor.redevelop.core.project.ProjectManager;
import com.muse.editor.redevelop.core.project.ProjectService;
import com.muse.editor.redevelop.core.user.*;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.user.LoggedInEvent;
import com.muse.editor.redevelop.event.view.ChangeViewEvent;
import com.muse.editor.redevelop.gui.manager.ViewManager;
import com.muse.editor.redevelop.gui.model.Viewable;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppManager {
    private final static int MONITOR_DELAY = 8;

    private final UserManager userManager = UserManager.getInstance();
    private final AuthService authService = AuthService.getInstance();

    private final ViewManager    viewManager = ViewManager.getInstance();
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private final ProjectService projectService = ProjectService.getInstance();

    private final CursorModel   cursorModel = CursorModel.getInstance();
    private final EditorState   editorState = EditorState.getInstance();
    private final ScoreManager  scoreManager = ScoreManager.getInstance();
    private final EditorService editorService = EditorService.getInstance();

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

        if (TokenStorage.isLoggedIn()) {
            Thread autoLoginThread = new Thread(() -> {
                final User user = UserService.getInstance().getCurrentUser();

                if (user != null) {
                    Platform.runLater(() -> {
                        UserManager.getInstance().setCurrentUser(user);
                        EventBus.getInstance().publish(new LoggedInEvent());
                    });
                } else {
                    Platform.runLater(() -> TokenStorage.clear());
                }

            });

            autoLoginThread.setDaemon(true);
            autoLoginThread.start();
        }
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
