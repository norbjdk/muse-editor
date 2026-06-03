package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.core.model.music.PartList;
import com.muse.editor.redevelop.core.model.music.ScorePart;
import com.muse.editor.redevelop.core.model.music.ScorePartwise;
import com.muse.editor.redevelop.core.project.Project;
import com.muse.editor.redevelop.core.project.ProjectManager;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.project.ProjectCreatedEvent;
import com.muse.editor.redevelop.gui.model.Presentable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SheetComponent extends Presentable<ScrollPane> {
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private ChangeListener<ScorePartwise> scoreListener;

    private final double PAGE_WIDTH    = 794;
    private final double PAGE_MARGIN_H = 72;
    private final double PAGE_MARGIN_V = 60;

    private VBox pageContainer;
    private VBox componentContainer;

    private TextField workTitleInput;
    private TextField creatorInput;

    private List<PartComponent> partComponents;

    public SheetComponent() {
        super(new ScrollPane());
    }

    @Override
    protected void initComponents() {
        pageContainer      = new VBox();
        componentContainer = new VBox();

        workTitleInput = new TextField();
        creatorInput = new TextField();
    }

    @Override
    protected void setupComponents() {

    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/muse/editor/styles/components/sheet-component.css")).toExternalForm()
        );

        root.getStyleClass().add("sheet-pane");

        pageContainer.getStyleClass().add("page-container");

        workTitleInput.getStyleClass().add("title-input");
        creatorInput.getStyleClass().add("creator-input");
    }

    @Override
    protected void setupLayout() {
        root.setFitToWidth(true);

        final StackPane wrapper = new StackPane(pageContainer);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(20, 40, 20, 40));

        root.setContent(wrapper);
    }

    @Override
    protected void setupEventListeners() {
        scoreListener = (observableValue, scorePartwise, t1) -> {
            Platform.runLater(() -> {
                workTitleInput.setText(t1.getWorkTitle());
                creatorInput.setText(t1.getCreator());
            });
        };
        ProjectManager.getInstance().scoreProperty().addListener(scoreListener);
    }

    @Override
    protected void setupEventHandlers() {

    }

    public void load() {
        partComponents = new ArrayList<>();

        PartList partList = projectManager.scoreProperty().get().getPartList();
        for (ScorePart scorePart : partList.getScoreParts()) {
            partComponents.add(new PartComponent(scorePart.getId(), scorePart.getPartName()));
        }

        for (PartComponent partComponent : partComponents) {
            for (Part part : projectManager.scoreProperty().get().getParts()) {
                if (part.getId().equals(partComponent.getPartID())) {
                    partComponent.assignPart(part);
                }
            }
        }

        Platform.runLater(() -> pageContainer.getChildren().add(buildPage()));
    }

    private VBox buildPage() {
        final VBox page = new VBox(90);

        page.setPadding(
                new Insets(
                        PAGE_MARGIN_V,
                        PAGE_MARGIN_H,
                        PAGE_MARGIN_V,
                        PAGE_MARGIN_H
                )
        );

        final VBox header = new VBox();

        header.getStyleClass().add("header");

        workTitleInput.setText(projectManager.scoreProperty().get().getWorkTitle());
        creatorInput.setText(projectManager.scoreProperty().get().getCreator());

        header.getChildren().addAll(
                workTitleInput,
                creatorInput
        );

        page.getChildren().add(header);

        return page;
    }
}
