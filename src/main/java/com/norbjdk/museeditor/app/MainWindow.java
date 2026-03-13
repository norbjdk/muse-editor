package com.norbjdk.museeditor.app;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MainWindow {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 820;

    private final BorderPane root;
    private final Scene scene;

    public MainWindow() {
        root = new BorderPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}
