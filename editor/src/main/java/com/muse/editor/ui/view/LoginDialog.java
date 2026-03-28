package com.muse.editor.ui.view;


import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.ButtonFactory;
import javafx.scene.control.*;

public class LoginDialog extends Dialog<Boolean> implements Presentable {

    private Label usernameLabel;
    private Label passwordLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;
    private Label registerLabel;
    private Button loginBtn;

    public LoginDialog()  {
        setTitle("Login to MUSE");
        setHeaderText("Sign in to your account");
    }

    @Override
    public void initComponents() {
        usernameField = new TextField();
        passwordField = new PasswordField();
        usernameLabel = new Label();
        passwordLabel = new Label();
        errorLabel = new Label();
        registerLabel = new Label();

        loginBtn = ButtonFactory.createButton("Log In", "login-btn", "Log into your account", "login-btn");
    }

    @Override
    public void setupComponents() {
        usernameLabel.setText("Username");
        passwordLabel.setText("Password");
        registerLabel.setText("Don't have account yet? Register there");
    }

    @Override
    public void setupStyle() {

    }

    @Override
    public void setupLayout() {

    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
