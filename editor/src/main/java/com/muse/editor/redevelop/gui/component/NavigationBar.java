package com.muse.editor.redevelop.gui.component;

import com.muse.editor.redevelop.gui.util.ButtonFactory;
import com.muse.editor.redevelop.gui.model.Presentable;
import com.muse.editor.redevelop.gui.model.Viewable;
import com.muse.editor.redevelop.gui.util.SpaceFactory;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;


public class NavigationBar extends Presentable<VBox> implements Viewable {
    private ChangeListener<Boolean> connectionListener;

    private Button homeBtn;
    private Button createProjectBtn;
    private Button openProjectBtn;
    private Button collectionBtn;
    private Button learnBtn;
    private Button settingsBtn;

    private VBox openProjectBtnContainer;

    public NavigationBar() {
        super(new VBox());
    }

    @Override
    protected void initComponents() {
        openProjectBtnContainer = new VBox(10);

        homeBtn          = ButtonFactory.createButton("Home", "home-btn", "Home View", "navigation-btn");
        createProjectBtn = ButtonFactory.createButton("New Project", "home-btn", "Home View", "navigation-btn");
        openProjectBtn   = ButtonFactory.createButton("Open", "home-btn", "Home View", "navigation-btn");
        collectionBtn    = ButtonFactory.createButton("Collection", "home-btn", "Home View", "navigation-btn");
        learnBtn         = ButtonFactory.createButton("Learn", "home-btn", "Home View", "navigation-btn");
        settingsBtn      = ButtonFactory.createButton("Settings", "home-btn", "Home View", "navigation-btn");
    }

    @Override
    protected void setupComponents() {
        ButtonFactory.addIcon(homeBtn, FontAwesomeSolid.HOME, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(createProjectBtn, FontAwesomeSolid.PLUS_CIRCLE, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(openProjectBtn, FontAwesomeSolid.FOLDER_OPEN, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(collectionBtn, FontAwesomeSolid.LAYER_GROUP, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(learnBtn, FontAwesomeSolid.GRADUATION_CAP, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(settingsBtn, FontAwesomeSolid.COG, 15, Color.rgb(5, 5, 5));
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components/navigation-bar.css")).toExternalForm());
        root.getStyleClass().add("navigation-bar");
    }

    @Override
    protected void setupLayout() {
        root.getChildren().addAll(
                homeBtn,
                createProjectBtn,
                openProjectBtn,
                SpaceFactory.createSpacer(SpaceFactory.Direction.VERTICAL),
                openProjectBtnContainer,
                SpaceFactory.createSpacer(SpaceFactory.Direction.VERTICAL),
                collectionBtn,
                learnBtn,
                settingsBtn
        );
    }

    @Override
    protected void setupEventListeners() {

    }

    @Override
    protected void setupEventHandlers() {

    }
}
