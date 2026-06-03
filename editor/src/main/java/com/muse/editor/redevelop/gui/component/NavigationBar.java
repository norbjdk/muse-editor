package com.muse.editor.redevelop.gui.component;

import com.muse.editor.develop.model.dto.internal.ViewRequest;
import com.muse.editor.develop.model.event.ChangeViewRequestedEvent;
import com.muse.editor.develop.ui.model.ViewName;
import com.muse.editor.redevelop.core.project.ProjectManager;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.project.ProjectCreatedEvent;
import com.muse.editor.redevelop.event.view.ChangeViewEvent;
import com.muse.editor.redevelop.gui.util.ButtonFactory;
import com.muse.editor.redevelop.gui.model.Presentable;
import com.muse.editor.redevelop.gui.model.Viewable;
import com.muse.editor.redevelop.gui.util.SpaceFactory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;


public class NavigationBar extends Presentable<VBox> implements Viewable {
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private ChangeListener<Boolean> connectionListener;

    private Button homeBtn;
    private Button createProjectBtn;
    private Button openProjectBtn;
    private Button collectionBtn;
    private Button learnBtn;
    private Button settingsBtn;
    private Button currentProjectBtn;

    private StackPane logoContainer;
    private Rectangle logoView;

    public NavigationBar() {
        super(new VBox());
    }

    @Override
    protected void initComponents() {
        logoContainer = new StackPane();
        logoView      = new Rectangle();

        homeBtn           = ButtonFactory.createButton("Home", "home-btn", "Home View", "navigation-btn");
        createProjectBtn  = ButtonFactory.createButton("New Project", "home-btn", "Home View", "navigation-btn");
        openProjectBtn    = ButtonFactory.createButton("Open", "home-btn", "Home View", "navigation-btn");
        collectionBtn     = ButtonFactory.createButton("Collection", "home-btn", "Home View", "navigation-btn");
        learnBtn          = ButtonFactory.createButton("Learn", "home-btn", "Home View", "navigation-btn");
        settingsBtn       = ButtonFactory.createButton("Settings", "home-btn", "Home View", "navigation-btn");
        currentProjectBtn = ButtonFactory.createButton("Project Name", "project-btn", "Go to your open project", "navigation-btn");
    }

    @Override
    protected void setupComponents() {
        ButtonFactory.addIcon(homeBtn, FontAwesomeSolid.HOME, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(createProjectBtn, FontAwesomeSolid.PLUS_CIRCLE, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(openProjectBtn, FontAwesomeSolid.FOLDER_OPEN, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(collectionBtn, FontAwesomeSolid.LAYER_GROUP, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(learnBtn, FontAwesomeSolid.GRADUATION_CAP, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(settingsBtn, FontAwesomeSolid.COG, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(currentProjectBtn, FontAwesomeSolid.EDIT, 15, Color.rgb(5,5 ,5));

        currentProjectBtn.setVisible(false);
        currentProjectBtn.setManaged(false);

        collectionBtn.setDisable(true);

        setupLogo();
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components/navigation-bar.css")).toExternalForm());
        root.getStyleClass().add("navigation-bar");
    }

    @Override
    protected void setupLayout() {
        root.getChildren().addAll(
                logoContainer,
                homeBtn,
                createProjectBtn,
                openProjectBtn,
                SpaceFactory.createSpacer(SpaceFactory.Direction.VERTICAL),
                currentProjectBtn,
                SpaceFactory.createSpacer(SpaceFactory.Direction.VERTICAL),
                SpaceFactory.createSpacer(SpaceFactory.Direction.VERTICAL),
                collectionBtn,
                learnBtn,
                settingsBtn
        );
    }

    @Override
    protected void setupEventListeners() {
        EventBus.getInstance().subscribe(ProjectCreatedEvent.class, projectCreatedEvent -> {
            Platform.runLater(() -> {
                currentProjectBtn.setText(projectCreatedEvent.getProjectTitle());
                currentProjectBtn.setVisible(true);
                currentProjectBtn.setManaged(true);

                createProjectBtn.setDisable(true);
                openProjectBtn.setDisable(true);
            });
        });
    }

    @Override
    protected void setupEventHandlers() {
        homeBtn.setOnAction(actionEvent -> handleHomeButtonClicked());
        createProjectBtn.setOnAction(actionEvent -> handleNewProjectButtonClicked());
        currentProjectBtn.setOnAction(actionEvent -> handleCurrentProjectButtonClicked());
    }

    private void handleHomeButtonClicked() {
        EventBus.getInstance().publish(new ChangeViewEvent(Name.HOME));
    }

    private void handleNewProjectButtonClicked() {
        EventBus.getInstance().publish(new ChangeViewEvent(Name.CREATE_PROJECT));
    }

    private void handleCurrentProjectButtonClicked() {
        EventBus.getInstance().publish(new ChangeViewEvent(Name.PROJECT));
    }

    private void setupLogo() {
        final Image heroImage = new Image(
                Objects.requireNonNull(getClass().getResource(
                        "/com/muse/editor/assets/images/logo.png")).toExternalForm()
        );
        logoView.setWidth(140);
        logoView.setHeight(140);
        logoView.setArcWidth(24);
        logoView.setArcHeight(24);
        logoView.setFill(new ImagePattern(heroImage));

        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.getChildren().add(logoView);
    }
}
