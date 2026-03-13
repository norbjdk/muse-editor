package com.norbjdk.museeditor.app;

import com.norbjdk.museeditor.ui.component.NavigationBar;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MainWindow {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 820;

    private final BorderPane root;
    private final Scene scene;

    private final NavigationBar navigationBar;

    public MainWindow() {
        navigationBar = new NavigationBar();

        root = new BorderPane();
        root.setTop(navigationBar);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}
