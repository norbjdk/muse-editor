package com.muse.editor.app;

import com.muse.editor.core.edit.EditService;
import com.muse.editor.core.project.ProjectService;
import com.muse.editor.core.user.UserService;
import javafx.application.Application;
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

public class MuseEditorApp extends Application {
    private static final int MIN_WIDTH = 1200;
    private static final int MIN_HEIGHT = 650;

    @Override
    public void start(Stage primaryStage) {
        final MainWindow mainWindow = new MainWindow();

        primaryStage.setTitle("Muse Workspace");
        primaryStage.setScene(mainWindow.getScene());
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/muse/editor/assets/images/logo.png")).toExternalForm()));
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMaximized(true);
        primaryStage.show();

        ProjectService.getInstance().init(primaryStage);
        UserService.getInstance().init(primaryStage);
        AppManager.getInstance().init(primaryStage);
    }

    public static void main(String [] args) {
        launch(args);
    }
}

