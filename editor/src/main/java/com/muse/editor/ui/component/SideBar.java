package com.muse.editor.ui.component;

import com.muse.editor.ui.model.Presentable;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class SideBar extends VBox implements Presentable {

    private ProfileBar profileBar;

    public SideBar() {
        present();
    }

    @Override
    public void initComponents() {
        profileBar = new ProfileBar();
    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("side-bar");
    }

    @Override
    public void setupLayout() {
        getChildren().addAll(
                profileBar
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
