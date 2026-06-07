package com.muse.editor.redevelop.app;

import com.muse.editor.redevelop.app.window.LoginWindow;
import com.muse.editor.redevelop.app.window.MainWindow;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

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
    private static final int MIN_WIDTH = 900;
    private static final int MIN_HEIGHT = 600;

    private static final StringProperty titleProperty = new SimpleStringProperty("MUSE Editor");

    @Override
    public void start(Stage primaryStage){
        final MainWindow mainWindow   = new MainWindow();
        final LoginWindow loginWindow = new LoginWindow();

        final AppManager appManager = AppManager.getInstance();

        appManager.init();

        primaryStage.setTitle(titleProperty.get());
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/muse/editor/assets/images/logo.png")).toExternalForm()));
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
//        primaryStage.setMaximized(true);
        primaryStage.setScene(loginWindow.getScene());
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
