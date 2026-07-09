package com.muse.editor.gui.component;

import com.muse.editor.core.model.music.PartList;
import com.muse.editor.core.model.music.ScorePart;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.project.*;
import com.muse.editor.event.view.ShowCollaboratorsEvent;
import com.muse.editor.gui.model.Presentable;
import com.muse.editor.gui.util.ButtonFactory;
import com.muse.editor.gui.util.SpaceFactory;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;

public class ToolBar extends Presentable<HBox> {
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private Button cloudBtn;
    private Button undoBtn;
    private Button redoBtn;
    private Button publishBtn;
    private Button saveBtn;
    private Button closeBtn;
    private Button collaboratorsBtn;

    private ComboBox<String> partsBox;

    public ToolBar() {
        super(new HBox());
    }

    @Override
    protected void initComponents() {
        partsBox = new ComboBox<>();

        cloudBtn         = ButtonFactory.createButton("", "cloud-btn", "", "tool-bar-btn");
        undoBtn          = ButtonFactory.createButton("", "undo-btn", "", "tool-bar-btn");
        redoBtn          = ButtonFactory.createButton("", "redo-btn", "", "tool-bar-btn");
        publishBtn       = ButtonFactory.createButton("Publish", "share-btn", "", "tool-bar-btn-b");
        saveBtn          = ButtonFactory.createButton("", "save-btn", "", "tool-bar-btn");
        closeBtn         = ButtonFactory.createButton("", "close-btn", "", "tool-bar-btn");
        collaboratorsBtn = ButtonFactory.createButton("", "collaborators-btn", "", "tool-bar-btn");
    }

    @Override
    protected void setupComponents() {
        ButtonFactory.addIcon(cloudBtn, FontAwesomeSolid.CLOUD, 17, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(undoBtn, FontAwesomeSolid.UNDO, 17, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(redoBtn, FontAwesomeSolid.REDO, 17, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(publishBtn, FontAwesomeSolid.SHARE, 17, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(saveBtn, FontAwesomeSolid.SAVE, 17, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(closeBtn, FontAwesomeSolid.DOOR_CLOSED, 17, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(collaboratorsBtn, FontAwesomeSolid.USER_FRIENDS, 17, Color.rgb(5, 5, 5));
    }

    @Override
    protected void setupStyle() {
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components/tool-bar.css")).toExternalForm());
        root.getStyleClass().add("tool-bar");
    }

    @Override
    protected void setupLayout() {
        root.getChildren().addAll(
                cloudBtn,
                partsBox,
                SpaceFactory.createSpacer(SpaceFactory.Direction.HORIZONTAL),
                undoBtn,
                redoBtn,
                SpaceFactory.createSpacer(SpaceFactory.Direction.HORIZONTAL),
                publishBtn,
                saveBtn,
                closeBtn,
                collaboratorsBtn
        );
    }

    @Override
    protected void setupEventListeners() {
        EventBus.getInstance().subscribe(ProjectCreatedEvent.class, projectCreatedEvent -> {
            Platform.runLater(() -> {
                PartList partList = projectManager.scoreProperty().get().getPartList();
                for (ScorePart scorePart : partList.getScoreParts()) {
                    partsBox.getItems().add(scorePart.getPartName().getValue());
                }
                partsBox.getSelectionModel().selectFirst();
            });
        });
        EventBus.getInstance().subscribe(ProjectOpenedEvent.class, projectOpenedEvent -> {
            Platform.runLater(() -> {
                PartList partList = projectManager.scoreProperty().get().getPartList();
                for (ScorePart scorePart : partList.getScoreParts()) {
                    partsBox.getItems().add(scorePart.getPartName().getValue());
                }
                partsBox.getSelectionModel().selectFirst();
            });
        });
    }

    @Override
    protected void setupEventHandlers() {
        partsBox.getSelectionModel().selectedItemProperty().addListener((observableValue, p0, p1) -> {
            EventBus.getInstance().publish(new ChangePartComponentEvent(p1));
        });
        saveBtn.setOnAction(actionEvent -> EventBus.getInstance().publish(new SaveProjectEvent()));
        publishBtn.setOnAction(actionEvent -> EventBus.getInstance().publish(new PublishProjectEvent()));
        closeBtn.setOnAction(actionEvent -> EventBus.getInstance().publish(new CloseProjectEvent()));
        collaboratorsBtn.setOnAction(actionEvent -> EventBus.getInstance().publish(new ShowCollaboratorsEvent()));
    }
}
