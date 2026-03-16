package com.norbjdk.museeditor.ui.component;

import com.norbjdk.museeditor.core.EventBus;
import com.norbjdk.museeditor.model.dto.internal.ViewRequest;
import com.norbjdk.museeditor.model.event.ChangeViewRequestedEvent;
import com.norbjdk.museeditor.ui.model.Presentable;
import com.norbjdk.museeditor.ui.model.ViewName;
import com.norbjdk.museeditor.ui.util.ButtonFactory;
import com.norbjdk.museeditor.ui.util.SpaceFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;

import static com.norbjdk.museeditor.ui.util.SpaceFactory.createSpacer;

public class NavigationBar extends HBox implements Presentable {

    private Button homeBtn;
    private Button createProjectBtn;
    private Button openProjectBtn;
    private Button collectionBtn;
    private Button learnBtn;
    private Button settingsBtn;
    private Button currentProjectBtn;

    public NavigationBar() {
        present();
    }

    @Override
    public void initComponents() {
        homeBtn = ButtonFactory.createButton("Home", "home-btn", "Home View", "navigation-btn");
        createProjectBtn = ButtonFactory.createButton("New Project", "home-btn", "Home View", "navigation-btn");
        openProjectBtn = ButtonFactory.createButton("Open", "home-btn", "Home View", "navigation-btn");
        collectionBtn = ButtonFactory.createButton("Collection", "home-btn", "Home View", "navigation-btn");
        learnBtn = ButtonFactory.createButton("Learn", "home-btn", "Home View", "navigation-btn");
        settingsBtn = ButtonFactory.createButton("Settings", "home-btn", "Home View", "navigation-btn");
        currentProjectBtn = ButtonFactory.createButton("Project Name", "project-btn", "Go to your open project", "navigation-btn");
    }

    @Override
    public void setupComponents() {
        ButtonFactory.addIcon(homeBtn, FontAwesomeSolid.HOME, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(createProjectBtn, FontAwesomeSolid.PLUS_CIRCLE, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(openProjectBtn, FontAwesomeSolid.FOLDER_OPEN, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(currentProjectBtn, FontAwesomeSolid.EDIT, 15, Color.rgb(5,5 ,5));
        ButtonFactory.addIcon(collectionBtn, FontAwesomeSolid.LAYER_GROUP, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(learnBtn, FontAwesomeSolid.GRADUATION_CAP, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(settingsBtn, FontAwesomeSolid.COG, 15, Color.rgb(5, 5, 5));
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/museeditor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("navigation-bar");
    }

    @Override
    public void setupLayout() {
        getChildren().addAll(
                homeBtn,
                createProjectBtn,
                openProjectBtn,
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                currentProjectBtn,
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                collectionBtn,
                learnBtn,
                settingsBtn
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {
        homeBtn.setOnAction(actionEvent -> handleHomeButtonClicked());
        createProjectBtn.setOnAction(actionEvent -> handleNewProjectButtonClicked());
        collectionBtn.setOnAction(actionEvent -> handleCollectionButtonClicked());
        settingsBtn.setOnAction(actionEvent -> handleSettingsButtonClicked());
        currentProjectBtn.setOnAction(actionEvent -> handleCurrentProjectButtonClicked());
    }

    private void handleHomeButtonClicked() {
        EventBus.getInstance().publish(new ChangeViewRequestedEvent(new ViewRequest(ViewName.HOME)));
    }

    private void handleSettingsButtonClicked() {
        EventBus.getInstance().publish(new ChangeViewRequestedEvent(new ViewRequest(ViewName.SETTINGS)));
    }

    private void handleCollectionButtonClicked() {
        EventBus.getInstance().publish(new ChangeViewRequestedEvent(new ViewRequest(ViewName.COLLECTION)));
    }

    private void handleNewProjectButtonClicked() {
        EventBus.getInstance().publish(new ChangeViewRequestedEvent(new ViewRequest(ViewName.NEW_PROJECT)));
    }

    private void handleCurrentProjectButtonClicked() {
        EventBus.getInstance().publish(new ChangeViewRequestedEvent(new ViewRequest(ViewName.PROJECT)));
    }
}
