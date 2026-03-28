package com.muse.editor.app;

import com.muse.editor.core.EventBus;
import com.muse.editor.model.dto.internal.ViewRequest;
import com.muse.editor.model.dto.internal.ViewResponse;
import com.muse.editor.model.event.ViewChangedEvent;
import com.muse.editor.ui.component.MenuBar;
import com.muse.editor.ui.component.NavigationBar;
import com.muse.editor.ui.component.SideBar;
import com.muse.editor.ui.component.StatusBar;
import com.muse.editor.ui.manager.ViewManager;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.model.ViewName;
import javafx.geometry.Insets;
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
    private SideBar sideBar;
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
        sideBar = new SideBar();

        root = new BorderPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    @Override
    public void setupComponents() {
        BorderPane.setMargin(sideBar, new Insets(5, 0, 5, 6));
    }

    @Override
    public void setupStyle() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/app.css")).toExternalForm());
        root.getStyleClass().add("app");
    }

    @Override
    public void setupLayout() {
        root.setTop(new VBox(0, menuBar, navigationBar));
        root.setRight(sideBar);
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
