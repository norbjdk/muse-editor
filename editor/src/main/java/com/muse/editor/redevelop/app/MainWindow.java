package com.muse.editor.redevelop.app;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class MainWindow {
    private static final int WINDOW_WIDTH  = 1380;
    private static final int WINDOW_HEIGHT = 820;

    private BorderPane root;
    private Scene      scene;

    MainWindow() {
        root  = new BorderPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
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
