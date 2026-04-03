package com.muse.editor.ui.component;

import com.muse.editor.core.EventBus;
import com.muse.editor.core.auth.AuthService;
import com.muse.editor.core.user.User;
import com.muse.editor.core.user.UserService;
import com.muse.editor.model.event.LoginSuccessEvent;
import com.muse.editor.model.event.LogoutEvent;
import com.muse.editor.model.event.OpenLoginDialogEvent;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.ButtonFactory;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class ProfileBar extends GridPane implements Presentable {

    private Circle pictureView;
    private Label usernameLabel;
    private Label statusLabel;
    private VBox textContainer;
    private HBox buttonContainer;

    private Button loginBtn;
    private Button logoutBtn;

    private User boundUser;
    private final AuthService authService;
    private final UserService userService;

    public ProfileBar() {
        authService = AuthService.getInstance();
        userService = UserService.getInstance();

        present();
    }

    @Override
    public void initComponents() {
        loginBtn = ButtonFactory.createButton("Login",  "login-btn", "Login to your account", "profile-btn");
        logoutBtn = ButtonFactory.createButton("Logout",  "logout-btn", "Logout from your account", "profile-btn");

        usernameLabel =  new Label();
        statusLabel = new Label();

        pictureView = new Circle(25);

        textContainer = new VBox(5);
        buttonContainer = new HBox(10);
    }

    @Override
    public void setupComponents() {
        pictureView.setRadius(25);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        // temporary fixed setup

        statusLabel.setText("online");
        pictureView.setFill(new ImagePattern(new Image(Objects.requireNonNull(getClass().getResource("/com/muse/editor/assets/images/user.png")).toExternalForm())));
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("profile-bar");

        textContainer.getStyleClass().add("profile-text-container");
        buttonContainer.getStyleClass().add("profile-button-container");


        statusLabel.getStyleClass().add("profile-status");
        usernameLabel.getStyleClass().add("profile-username");

        DropShadow shadow = new DropShadow(8, Color.gray(0.5));
        pictureView.setEffect(shadow);
    }

    @Override
    public void setupLayout() {
        add(buttonContainer, 0, 0);
        updateButtonVisibility();
    }

    @Override
    public void setupEventListeners() {
        EventBus.getInstance().subscribe(LoginSuccessEvent.class, event -> {
            Platform.runLater(this::updateButtonVisibility);
        });

        EventBus.getInstance().subscribe(LogoutEvent.class, event -> {
            Platform.runLater(this::updateButtonVisibility);
        });
    }


    @Override
    public void setupEventHandlers() {
        loginBtn.setOnAction(e -> handleLogin());
        logoutBtn.setOnAction(e -> authService.logout());
    }

    private void updateButtonVisibility() {
        buttonContainer.getChildren().clear();

        if (authService.isLoggedIn()) {
            buttonContainer.getChildren().add(logoutBtn);
        } else {
            buttonContainer.getChildren().add(loginBtn);
        }
    }

    private void handleLogin() {
        EventBus.getInstance().publish(new OpenLoginDialogEvent());
    }
}
