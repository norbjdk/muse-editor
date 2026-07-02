package com.muse.editor.app.window;

import com.muse.editor.event.EventBus;
import com.muse.editor.event.view.ViewChangedEvent;
import com.muse.editor.gui.component.NavigationBar;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public class MainWindow {
    private static final int WINDOW_WIDTH  = 1380;
    private static final int WINDOW_HEIGHT = 720;

    private BorderPane root;
    private Scene      scene;

    private NavigationBar navigationBar;

    public MainWindow() {
        navigationBar = new NavigationBar();

        root  = new BorderPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/app.css")).toExternalForm());
        root.getStyleClass().add("app");

        BorderPane.setMargin(navigationBar.getRoot(), new Insets(3,  0, 0,  0));

        root.setLeft(navigationBar.getRoot());

        EventBus.getInstance().subscribe(ViewChangedEvent.class, this::handleViewChanged);
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

    private void handleViewChanged(ViewChangedEvent event) {
        final var newView = event.getView();

        if (newView != null) {
            final Node viewNode = newView.getRoot();

            final Node currentView = root.getCenter();

            if (currentView != viewNode) {
                root.setCenter(viewNode);
            }
        }
    }
}
