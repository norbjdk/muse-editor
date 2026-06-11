package com.muse.editor.gui.component.music;

import com.muse.editor.core.edit.ScoreManager;
import com.muse.editor.core.model.music.PartList;
import com.muse.editor.core.model.music.ScorePart;
import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.editor.ChangeInputModeEvent;
import com.muse.editor.event.project.ChangePartComponentEvent;
import com.muse.editor.event.project.PartComponentChangedEvent;
import com.muse.editor.event.project.PartComponentsCreatedEvent;
import com.muse.editor.gui.model.Presentable;
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
    private VBox partContainer;

    private TextField workTitleInput;
    private TextField creatorInput;

    private List<PartComponent> partComponents;

    public SheetComponent() {
        super(new ScrollPane());
    }

    @Override
    protected void initComponents() {
        pageContainer = new VBox();
        partContainer = new VBox();

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

        EventBus.getInstance().subscribe(PartComponentsCreatedEvent.class, partComponentsCreatedEvent -> {
            EventBus.getInstance().publish(new ChangePartComponentEvent(partComponents.getFirst().getPartName().getValue()));
        });
    }

    @Override
    protected void setupEventHandlers() {
        EventBus.getInstance().subscribe(ChangePartComponentEvent.class, changePartComponentEvent -> {
                for (PartComponent partComponent : partComponents) {
                    if (partComponent.getPartName().getValue().equals(changePartComponentEvent.getPartName())) {
                        partContainer.getChildren().clear();
                        partContainer.getChildren().add(partComponent.getRoot());
                        EventBus.getInstance().publish(new PartComponentChangedEvent(partComponent.partProperty().get()));
                    }
                }
        });

        getRoot().setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case N -> EventBus.getInstance().publish(new ChangeInputModeEvent());
                case P -> System.out.println("Switch part!");
            }
        });
    }

    public void load() {
        partComponents = new ArrayList<>();

        PartList partList = projectManager.scoreProperty().get().getPartList();
        for (ScorePart scorePart : partList.getScoreParts()) {
            partComponents.add(new PartComponent(scorePart.getId(), scorePart.getPartName()));
        }

        final ScoreManager scoreManager = ScoreManager.getInstance();

        for (PartComponent partComponent : partComponents) {
            partComponent.assignPart(scoreManager.getPartProperties().stream()
                    .filter(partObjectProperty -> partObjectProperty.get().getId().equals(partComponent.getPartID()))
                    .findFirst()
                    .orElseThrow(()-> new IllegalArgumentException("Part not found")));
        }

        Platform.runLater(() -> pageContainer.getChildren().add(buildPage()));

        EventBus.getInstance().publish(new PartComponentsCreatedEvent());
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
        page.getChildren().add(partContainer);

        return page;
    }
}
