package com.norbjdk.museeditor.app;

import com.norbjdk.museeditor.ui.component.MenuBar;
import com.norbjdk.museeditor.ui.component.NavigationBar;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainWindow {
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 820;

    private final BorderPane root;
    private final Scene scene;

    private final MenuBar menuBar;
    private final NavigationBar navigationBar;

    public MainWindow() {
        menuBar = new MenuBar();
        navigationBar = new NavigationBar();

        root = new BorderPane();
        root.setTop(new VBox(0, menuBar, navigationBar));

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}
