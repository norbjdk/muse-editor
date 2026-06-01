package com.muse.editor.develop.ui.view;

import com.muse.editor.develop.core.EventBus;
import com.muse.editor.develop.core.auth.AuthService;
import com.muse.editor.develop.core.model.dto.LoginRequest;
import com.muse.editor.develop.model.event.user.LoginFailedEvent;
import com.muse.editor.develop.model.event.user.LoginSuccessEvent;
import com.muse.editor.develop.ui.model.Presentable;
import com.muse.editor.develop.ui.util.ButtonFactory;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginDialog extends Dialog<Boolean> implements Presentable {

    private Label usernameLabel;
    private Label passwordLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;
    private Label registerLabel;
    private Button loginBtn;
    private Button cancelBtn;

    private GridPane form;
    private VBox root;

    private final AuthService authService;

    public LoginDialog() {
        authService = AuthService.getInstance();
        setTitle("Login to MUSE");
        setupIcon();
        present();
    }

    @Override
    public void initComponents() {
        usernameField = new TextField();
        passwordField = new PasswordField();
        usernameLabel = new Label();
        passwordLabel = new Label();
        errorLabel = new Label();
        registerLabel = new Label();

        loginBtn = ButtonFactory.createButton("Log In", "login-btn", "Log into your account", "dialog-btn");
        cancelBtn = ButtonFactory.createButton("Cancel", "cancel-btn", "Cancel login", "dialog-btn");

        form = new GridPane();
        root = new VBox();
    }

    @Override
    public void setupComponents() {
        usernameLabel.setText("Username");
        passwordLabel.setText("Password");
        registerLabel.setText("Don't have an account yet? Register");

        usernameField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");

        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @Override
    public void setupStyle() {
        getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/dialogs.css")).toExternalForm());

        getDialogPane().getStyleClass().add("login-dialog");
        root.getStyleClass().add("login-root");
        form.getStyleClass().add("login-form");

        usernameLabel.getStyleClass().add("login-field-label");
        passwordLabel.getStyleClass().add("login-field-label");
        usernameField.getStyleClass().add("login-field");
        passwordField.getStyleClass().add("login-field");
        errorLabel.getStyleClass().add("login-error");
        registerLabel.getStyleClass().add("login-register-hint");
    }

    @Override
    public void setupLayout() {
        form.setHgap(12);
        form.setVgap(10);
        form.setPadding(new Insets(10, 0, 10, 0));

        form.add(usernameLabel, 0, 0);
        form.add(usernameField, 0, 1);
        form.add(passwordLabel, 0, 2);
        form.add(passwordField, 0, 3);

        HBox buttons = new HBox(10, cancelBtn, loginBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(10, 0, 0, 0));

        root.getChildren().addAll(form, errorLabel, buttons, registerLabel);
        root.setPadding(new Insets(20));
        root.setSpacing(6);
        root.setPrefWidth(320);

        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Node closeButton = getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);
        closeButton.setManaged(false);
    }

    @Override
    public void setupEventListeners() {
        EventBus.getInstance().subscribe(LoginSuccessEvent.class, event -> {
            Platform.runLater(() -> {
                setResult(true);
                close();
            });
        });

        EventBus.getInstance().subscribe(LoginFailedEvent.class, event -> {
            Platform.runLater(() -> showError("Invalid username or password."));
        });
    }

    @Override
    public void setupEventHandlers() {
        loginBtn.setOnAction(e -> handleLogin());
        cancelBtn.setOnAction(e -> {
            setResult(false);
            close();
        });


        passwordField.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            showError("Please fill in all fields.");
            return;
        }

        hideError();
        loginBtn.setDisable(true);
        loginBtn.setText("Logging in...");

        authService.login(new LoginRequest(username, password));
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        loginBtn.setDisable(false);
        loginBtn.setText("Log In");
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    private void setupIcon() {
        final Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/com/muse/editor/assets/images/logo.png")).toExternalForm()));
    }
}