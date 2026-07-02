package com.muse.editor.app;

import com.muse.editor.app.window.LoginWindow;
import com.muse.editor.app.window.MainWindow;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.view.ShowLoginWindow;
import com.muse.editor.event.view.ShowMainWindow;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
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
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 500;

    private static final StringProperty titleProperty = new SimpleStringProperty("MUSE Editor");

    @Override
    public void start(Stage primaryStage) {
        AppManager.init(primaryStage);

        final MainWindow mainWindow   = new MainWindow();
        final LoginWindow loginWindow = new LoginWindow();

        primaryStage.setTitle(titleProperty.get());
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/muse/editor/assets/images/logo.png")).toExternalForm()));
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        EventBus.getInstance().subscribe(ShowLoginWindow.class, showLoginWindow -> {
            primaryStage.setScene(loginWindow.getScene());
        });
        EventBus.getInstance().subscribe(ShowMainWindow.class, showMainWindow -> {
            primaryStage.setScene(mainWindow.getScene());
        });

        AppManager.getInstance().startApp();
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
