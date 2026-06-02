package com.muse.editor.redevelop.app;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class EditorApp extends Application {
    private static final int MIN_WIDTH = 1200;
    private static final int MIN_HEIGHT = 650;

    private static final StringProperty titleProperty = new SimpleStringProperty("MUSE Editor");

    @Override
    public void start(Stage primaryStage){
        final MainWindow mainWindow = new MainWindow();
        final AppManager appManager = AppManager.getInstance();

        appManager.init();

        primaryStage.setTitle(titleProperty.get());
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMaximized(true);
        primaryStage.setScene(mainWindow.getScene());
        primaryStage.show();

        titleProperty.addListener(((obs, t0, t1) -> {
            if (t1 != null && !t1.isEmpty()) primaryStage.setTitle(t1);
        }));
    }

    public static StringProperty getTitleProperty() {
        return titleProperty;
    }

    public static void changeTitle(final String newTitle) {
        if (newTitle.equals(titleProperty.get())) return;

        titleProperty.set(newTitle);
    }

    public static void  main(String [] args) {
        launch(args);
    }
}
