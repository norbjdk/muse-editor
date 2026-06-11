package com.muse.editor.gui.view;

import com.muse.editor.core.model.dto.LoginRequest;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.user.LoginEvent;
import com.muse.editor.gui.model.Presentable;
import com.muse.editor.gui.util.SpaceFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class LoginView extends Presentable<HBox> {
    private VBox heroSection;
    private VBox loginSection;
    private VBox loginLabels;
    private HBox noAccountContainer;
    private HBox forgotPwdContainer;

    private GridPane loginForm;

    private StackPane heroImageContainer;
    private Rectangle heroImageView;

    private Label heroHeaderLabel;
    private Label noAccountLabel;
    private Label loginHeaderLabel;
    private Label loginParagraphLabel;
    private Label usernameLabel;
    private Label passwordLabel;

    private Button signUpBtn;
    private Button signInBtn;
    private Button forgotPwdBtn;

    private TextField usernameInput;
    private PasswordField passwordInput;

    public LoginView() {
        super(new HBox());
    }

    @Override
    protected void initComponents() {
        heroSection        = new VBox();
        loginSection       = new VBox();
        loginLabels        = new VBox();
        noAccountContainer = new HBox();
        forgotPwdContainer = new HBox();

        loginForm = new GridPane();

        heroImageView      = new Rectangle();
        heroImageContainer = new StackPane();

        heroHeaderLabel     = new Label();
        noAccountLabel      = new Label();
        loginHeaderLabel    = new Label();
        loginParagraphLabel = new Label();
        usernameLabel       = new Label();
        passwordLabel       = new Label();

        signUpBtn    = new Button();
        signInBtn    = new Button();
        forgotPwdBtn = new Button();

        usernameInput = new TextField();
        passwordInput = new PasswordField();
    }

    @Override
    protected void setupComponents() {
        heroHeaderLabel.setText("""
                Many compositions.
                For free in MUSE.
                """);

        noAccountLabel.setText("Don't have an account?");
        loginHeaderLabel.setText("Welcome Back");
        loginParagraphLabel.setText("Sign in to your account");
        usernameLabel.setText("Username or Email");
        passwordLabel.setText("Password");

        signUpBtn.setText("Sign Up");
        signInBtn.setText("Sign In");
        forgotPwdBtn.setText("Forgot password?");

        usernameInput.setPromptText("Enter your username or email");
        passwordInput.setPromptText("Enter your password");

        setupLogo();
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views/login.css")).toExternalForm()
        );

        root.getStyleClass().add("login-view");

        heroSection.getStyleClass().add("hero-section");
        loginSection.getStyleClass().add("login-section");

        loginForm.getStyleClass().add("login-form");
        noAccountContainer.getStyleClass().add("no-acc-container");
        forgotPwdContainer.getStyleClass().add("forgot-pwd-container");
        loginLabels.getStyleClass().add("login-labels");

        heroHeaderLabel.getStyleClass().add("hero-header-label");
        loginHeaderLabel.getStyleClass().add("login-header-label");
        loginParagraphLabel.getStyleClass().add("login-paragraph-label");

        usernameInput.getStyleClass().add("input");
        passwordInput.getStyleClass().add("input");

        signInBtn.getStyleClass().add("sign-in-btn");
        signUpBtn.getStyleClass().add("sign-up-btn");
        forgotPwdBtn.getStyleClass().add("forgot-pwd-btn");

        HBox.setHgrow(heroSection, Priority.ALWAYS);
        HBox.setHgrow(loginSection, Priority.ALWAYS);
        HBox.setMargin(loginSection, new Insets(30));
        HBox.setHgrow(forgotPwdContainer, Priority.ALWAYS);
    }

    @Override
    protected void setupLayout() {
        noAccountContainer.getChildren().addAll(
                noAccountLabel, signUpBtn
        );

        loginLabels.getChildren().addAll(
                loginHeaderLabel,
                loginParagraphLabel
        );

        forgotPwdContainer.getChildren().add(forgotPwdBtn);

        heroSection.getChildren().addAll(
                heroImageContainer,
                SpaceFactory.createSpacer(SpaceFactory.Direction.VERTICAL),
                heroHeaderLabel,
                SpaceFactory.createSpacer(SpaceFactory.Direction.VERTICAL),
                noAccountContainer
        );

        loginForm.add(usernameLabel, 0, 0);
        loginForm.add(usernameInput, 0, 1);
        loginForm.add(passwordLabel, 0, 2);
        loginForm.add(passwordInput, 0, 3);

        loginSection.getChildren().addAll(
                loginLabels,
                loginForm,
                forgotPwdContainer,
                signInBtn,
                SpaceFactory.createSpacer(SpaceFactory.Direction.VERTICAL)
        );

        root.getChildren().addAll(
                heroSection,
                loginSection
        );
    }

    @Override
    protected void setupEventListeners() {
        signInBtn.setOnAction(actionEvent -> handleSignInBtn());
    }

    @Override
    protected void setupEventHandlers() {

    }

    private void handleSignInBtn() {
        final String username = usernameInput.getText();
        final String password = passwordInput.getText();

        final LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        EventBus.getInstance().publish(new LoginEvent(request));
    }

    private void setupLogo() {
        final Image heroImage = new Image(
                Objects.requireNonNull(getClass().getResource(
                        "/com/muse/editor/assets/images/logo.png")).toExternalForm()
        );
        heroImageView.setWidth(200);
        heroImageView.setHeight(200);
        heroImageView.setArcWidth(24);
        heroImageView.setArcHeight(24);
        heroImageView.setFill(new ImagePattern(heroImage));

        heroImageContainer.setAlignment(Pos.CENTER);
        heroImageContainer.getChildren().add(heroImageView);
    }
}
