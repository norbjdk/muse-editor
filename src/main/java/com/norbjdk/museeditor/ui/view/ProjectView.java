package com.norbjdk.museeditor.ui.view;

import com.norbjdk.museeditor.core.EventBus;
import com.norbjdk.museeditor.model.event.ProjectLoadedEvent;
import com.norbjdk.museeditor.ui.component.ToolBar;
import com.norbjdk.museeditor.ui.component.ToolBox;
import com.norbjdk.museeditor.ui.component.music.SheetPane;
import com.norbjdk.museeditor.ui.model.Presentable;
import com.norbjdk.museeditor.ui.model.Viewable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.Objects;

public class ProjectView extends BorderPane implements Presentable, Viewable {

    private ToolBar toolBar;
    private ToolBox toolBox;
    private SheetPane sheetPane;
    private Label loadingLabel;
    private StackPane centerStack;

    public ProjectView() {
        present();
    }

    @Override
    public void initComponents() {
        toolBar = new ToolBar();
        toolBox = new ToolBox();
        sheetPane = new SheetPane();
        centerStack = new StackPane();
        loadingLabel = new Label();
    }

    @Override
    public void setupComponents() {
        loadingLabel.setText("Loading...");
        loadingLabel.setVisible(false);

        centerStack.getChildren().addAll(
                sheetPane,
                loadingLabel
        );
        StackPane.setAlignment(loadingLabel, Pos.CENTER);
        BorderPane.setMargin(centerStack, new Insets(10));
        BorderPane.setAlignment(centerStack, Pos.CENTER);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/norbjdk/museeditor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("project-view");

        loadingLabel.getStyleClass().add("loading-label");
    }

    @Override
    public void setupLayout() {
        this.setTop(toolBar);
        this.setLeft(toolBox);
        this.setCenter(centerStack);
    }

    @Override
    public void setupEventListeners() {
        EventBus.getInstance().subscribe(ProjectLoadedEvent.class, event -> {
            Platform.runLater(() -> {
                loadingLabel.setVisible(false);
                sheetPane.bindProject(event.getProject());
            });
        });
    }

    @Override
    public void setupEventHandlers() {

    }
}
