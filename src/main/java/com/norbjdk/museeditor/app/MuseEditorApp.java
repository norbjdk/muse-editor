package com.norbjdk.museeditor.app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Muse Editor Application
 *
 * @see <a href="https://github.com/norbjdk/muse-editor">GitHub</a>
 *
 * @author norbjdk
 *
 * @version 1.0.0
 *
 */

public class MuseEditorApp extends Application {
    private static final int MIN_WIDTH = 1200;
    private static final int MIN_HEIGHT = 650;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Muse Workspace");
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
    }

    public static void main(String [] args) {
        launch(args);
    }
}

