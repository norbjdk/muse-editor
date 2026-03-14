package com.norbjdk.museeditor.app;

import com.norbjdk.museeditor.core.EventBus;
import com.norbjdk.museeditor.model.dto.internal.ViewRequest;
import com.norbjdk.museeditor.model.dto.internal.ViewResponse;
import com.norbjdk.museeditor.model.event.ViewChangedEvent;
import com.norbjdk.museeditor.ui.component.MenuBar;
import com.norbjdk.museeditor.ui.component.NavigationBar;
import com.norbjdk.museeditor.ui.component.StatusBar;
import com.norbjdk.museeditor.ui.manager.ViewManager;
import com.norbjdk.museeditor.ui.model.Presentable;
import com.norbjdk.museeditor.ui.model.ViewName;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class MainWindow implements Presentable {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 820;

    private BorderPane root;
    private Scene scene;

    private MenuBar menuBar;
    private NavigationBar navigationBar;
    private StatusBar statusBar;

    public MainWindow() {
        present();

        ViewManager.getInstance().changeView(new ViewRequest(ViewName.HOME));
    }

    @Override
    public void initComponents() {
        menuBar = new MenuBar();
        navigationBar = new NavigationBar();
        statusBar = new StatusBar();

        root = new BorderPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/museeditor/styles/app.css")).toExternalForm());
        root.getStyleClass().add("app");
    }

    @Override
    public void setupLayout() {
        root.setTop(new VBox(0, menuBar, navigationBar));
        root.setBottom(statusBar);
    }

    @Override
    public void setupEventListeners() {
        EventBus.getInstance().subscribe(ViewChangedEvent.class, this::handleViewChanged);
    }

    @Override
    public void setupEventHandlers() {

    }

    private void handleViewChanged(ViewChangedEvent event) {
        final var response = event.getResponse();

        changeView(response);
    }

    private void changeView(ViewResponse response) {
        final var newView = response.getView();

        if (newView != null) {
            final Node currentView = root.getCenter();

            if (currentView != newView) {
                root.setCenter((Node) newView);
            }
        }
    }

    public Scene getScene() {
        return scene;
    }

    public BorderPane getRoot() {
        return root;
    }

    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public static int getWindowHeight() {
        return WINDOW_HEIGHT;
    }
}
