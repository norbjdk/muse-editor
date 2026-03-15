package com.norbjdk.museeditor.ui.view;

import com.norbjdk.museeditor.ui.component.ToolBar;
import com.norbjdk.museeditor.ui.component.ToolBox;
import com.norbjdk.museeditor.ui.model.Presentable;
import com.norbjdk.museeditor.ui.model.Viewable;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public class ProjectView extends BorderPane implements Presentable, Viewable {

    private ToolBar toolBar;
    private ToolBox toolBox;

    public ProjectView() {
        present();
    }

    @Override
    public void initComponents() {
        toolBar = new ToolBar();
        toolBox = new ToolBox();
    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/museeditor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("project-view");
    }

    @Override
    public void setupLayout() {
        this.setTop(toolBar);
        this.setLeft(toolBox);
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
