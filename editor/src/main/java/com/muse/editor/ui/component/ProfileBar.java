package com.muse.editor.ui.component;

import com.muse.editor.app.AppManager;
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

import java.io.IOException;
import java.util.Objects;

public class ProfileBar extends GridPane implements Presentable {
    private final AppManager appManager = AppManager.getInstance();

    private Circle pictureView;
    private Label usernameLabel;
    private Label statusLabel;
    private VBox textContainer;
    private HBox buttonContainer;

    private Button loginBtn;
    private Button logoutBtn;

    private final AuthService authService;
    private final UserService userService;

    public ProfileBar() {
        authService = AuthService.getInstance();
        userService = UserService.getInstance();

        present();

        if (appManager.IsUserLoggedIn()) {
            updateProfileData();
        }
    }

    @Override
    public void initComponents() {
        loginBtn = ButtonFactory.createButton("Login", "login-btn", "Login to your account", "profile-btn");
        logoutBtn = ButtonFactory.createButton("Logout", "logout-btn", "Logout from your account", "profile-btn");

        usernameLabel = new Label();
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

        setDefaultProfileData();
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
        textContainer.getChildren().addAll(
                usernameLabel,
                statusLabel
        );

        add(pictureView, 0, 0);
        add(textContainer, 1, 0);
        add(buttonContainer, 2, 0);

        final ColumnConstraints firstCol = new ColumnConstraints();
        firstCol.setPercentWidth(20);
        final ColumnConstraints secondCol = new ColumnConstraints();
        secondCol.setPercentWidth(40);
        final ColumnConstraints thirdCol = new ColumnConstraints();
        thirdCol.setPercentWidth(40);

        this.getColumnConstraints().addAll(firstCol, secondCol, thirdCol);

        updateButtonVisibility();
    }

    @Override
    public void setupEventListeners() {
        EventBus.getInstance().subscribe(LoginSuccessEvent.class, event -> {
            Platform.runLater(() -> {
                updateButtonVisibility();
                updateProfileData();
            });
        });

        EventBus.getInstance().subscribe(LogoutEvent.class, event -> {
            Platform.runLater(() -> {
                updateButtonVisibility();
                updateProfileData();
            });
        });
    }

    @Override
    public void setupEventHandlers() {
        loginBtn.setOnAction(e -> handleLogin());
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void updateButtonVisibility() {
        buttonContainer.getChildren().clear();

        if (appManager.IsUserLoggedIn()) {
            buttonContainer.getChildren().add(logoutBtn);
        } else {
            buttonContainer.getChildren().add(loginBtn);
        }
    }

    private void updateProfileData() {
        try {
            if (appManager.IsUserLoggedIn()) {
                User currentUser = userService.getCurrentUser();

                if (currentUser != null) {
                    usernameLabel.setText(currentUser.getUsername());

                    String status = getStatusText(currentUser.getRole());
                    statusLabel.setText(status);

                    updateStatusStyle(currentUser.getRole());

                } else {
                    setDefaultProfileData();
                }
            }
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDefaultProfileData() {
        usernameLabel.setText("Guest");
        statusLabel.setText("offline");
        statusLabel.getStyleClass().removeAll("profile-status-online", "profile-status-admin");
        statusLabel.getStyleClass().add("profile-status-offline");

        try {
            Image defaultAvatar = new Image(Objects.requireNonNull(
                    getClass().getResource("/com/muse/editor/assets/images/user.png")).toExternalForm());
            pictureView.setFill(new ImagePattern(defaultAvatar));
        } catch (Exception e) {
            pictureView.setFill(Color.GRAY);
        }
    }

    private String getStatusText(String role) {
        if (role == null) return "online";

        switch (role.toLowerCase()) {
            case "admin":
                return "administrator";
            case "user":
                return "online";
            default:
                return "online";
        }
    }

    private void updateStatusStyle(String role) {
        statusLabel.getStyleClass().removeAll(
                "profile-status-online",
                "profile-status-offline",
                "profile-status-admin"
        );

        if (role == null) {
            statusLabel.getStyleClass().add("profile-status-online");
        } else {
            switch (role.toLowerCase()) {
                case "admin":
                    statusLabel.getStyleClass().add("profile-status-admin");
                    break;
                case "user":
                    statusLabel.getStyleClass().add("profile-status-online");
                    break;
                default:
                    statusLabel.getStyleClass().add("profile-status-online");
            }
        }
    }

    private void handleLogout() {
        authService.logout();
        setDefaultProfileData();
    }

    private void handleLogin() {
        EventBus.getInstance().publish(new OpenLoginDialogEvent());
    }
}