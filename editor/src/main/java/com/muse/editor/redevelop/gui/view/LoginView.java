package com.muse.editor.redevelop.gui.view;

import com.muse.editor.redevelop.gui.model.Presentable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class LoginView extends Presentable<BorderPane> {
    private VBox heroSection;
    private VBox loginSection;
    private HBox noAccountContainer;

    private StackPane heroImageContainer;
    private Circle    heroImageView;

    private Label heroHeaderLabel;
    private Label noAccountLabel;
    private Label loginHeaderLabel;
    private Label loginParagraphLabel;

    private Button signUpBtn;
    private Button signInBtn;
    private Button forgotPwdBtn;

    private TextField usernameInput;
    private TextField passwordInput;

    public LoginView() {
        super(new BorderPane());
    }

    @Override
    protected void initComponents() {
        heroSection        = new VBox();
        loginSection       = new VBox();
        noAccountContainer = new HBox();

        heroHeaderLabel = new Label();
        noAccountLabel  = new Label();
    }

    @Override
    protected void setupComponents() {
        heroHeaderLabel.setText("""
                Many compositions.
                For free in MUSE.
                Go check it yourself.
                """);

        noAccountLabel.setText("Don't have an account?");
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views/login.css")).toExternalForm()
        );

        root.getStyleClass().add("login-view");
    }

    @Override
    protected void setupLayout() {

    }

    @Override
    protected void setupEventListeners() {

    }

    @Override
    protected void setupEventHandlers() {

    }
}
