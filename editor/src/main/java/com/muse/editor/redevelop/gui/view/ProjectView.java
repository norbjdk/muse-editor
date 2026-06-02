package com.muse.editor.redevelop.gui.view;

import com.muse.editor.redevelop.core.model.music.PartList;
import com.muse.editor.redevelop.core.model.music.ScorePart;
import com.muse.editor.redevelop.core.project.ProjectManager;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.project.ChangePartComponentEvent;
import com.muse.editor.redevelop.event.project.ProjectCreatedEvent;
import com.muse.editor.redevelop.gui.component.ToolBar;
import com.muse.editor.redevelop.gui.component.music.PartComponent;
import com.muse.editor.redevelop.gui.model.Presentable;
import com.muse.editor.redevelop.gui.model.Viewable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.Objects;

public class ProjectView extends Presentable<BorderPane> implements Viewable {
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private ToolBar toolBar;

    private List<PartComponent> partComponents;

    public ProjectView() {
        super(new BorderPane());
    }

    @Override
    protected void initComponents() {
        toolBar = new ToolBar();
    }

    @Override
    protected void setupComponents() {

    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views/project.css")).toExternalForm());
        root.getStyleClass().add("project-root");

        BorderPane.setMargin(root, new Insets(20));
    }

    @Override
    protected void setupLayout() {
        getRoot().setTop(toolBar.getRoot());
    }

    @Override
    protected void setupEventListeners() {
        EventBus.getInstance().subscribe(ProjectCreatedEvent.class, projectCreatedEvent -> {
            Platform.runLater(() -> {
                PartList partList = projectManager.scoreProperty().get().getPartList();
                for (ScorePart scorePart : partList.getScoreParts()) {
                    partComponents.add(new PartComponent(scorePart.getId(), scorePart.getPartName()));
                }
            });
        });

        EventBus.getInstance().subscribe(ChangePartComponentEvent.class, changePartComponentEvent -> {
            Platform.runLater(() -> {
                partComponents.forEach(partComponent -> {
                    if (partComponent != null) {
                        if (partComponent.getPartName().getValue().equals(changePartComponentEvent.getPartName())) {
                            root.setCenter(partComponent.getRoot());
                        }
                    }
                });
            });
        });
    }

    @Override
    protected void setupEventHandlers() {

    }
}
