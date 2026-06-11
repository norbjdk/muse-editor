package com.muse.editor.app.window;

import com.muse.editor.gui.view.LoginView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class LoginWindow {
    private static final int WINDOW_WIDTH  = 801;
    private static final int WINDOW_HEIGHT = 501;

    private final LoginView loginView;

    private StackPane  root;
    private Scene      scene;

    public LoginWindow() {
        loginView = new LoginView();

        root  = new StackPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        root.setAlignment(Pos.CENTER);
        root.getChildren().add(loginView.getRoot());
    }

    public Scene getScene() {
        return scene;
    }
}
