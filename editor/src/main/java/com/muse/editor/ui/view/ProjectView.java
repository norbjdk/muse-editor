package com.muse.editor.ui.view;

import com.muse.editor.core.EventBus;
import com.muse.editor.model.event.ProjectCreatedEvent;
import com.muse.editor.model.event.ProjectLoadedEvent;
import com.muse.editor.ui.component.ToolBar;
import com.muse.editor.ui.component.ToolBox;
import com.muse.editor.ui.component.music.SheetPane;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.model.Viewable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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
        BorderPane.setMargin(toolBox, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(toolBar, new Insets(15, 10, 15, 10));
        BorderPane.setAlignment(centerStack, Pos.CENTER);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views.css")).toExternalForm());
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
        EventBus.getInstance().subscribe(ProjectCreatedEvent.class, event -> {
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
