package com.muse.editor.ui.view;

import com.muse.editor.core.EventBus;
import com.muse.editor.core.edit.Instrument;
import com.muse.editor.core.model.score.ScorePart;
import com.muse.editor.core.model.score.ScorePartwise;
import com.muse.editor.model.event.project.ProjectCreatedEvent;
import com.muse.editor.model.event.project.ProjectLoadedEvent;
import com.muse.editor.ui.component.ToolBar;
import com.muse.editor.ui.component.ToolBox;
import com.muse.editor.ui.component.music.SheetFactory;
import com.muse.editor.ui.component.music.SheetPane;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.model.Viewable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public class ProjectView extends BorderPane implements Presentable, Viewable {

    private ToolBar toolBar;
    private ToolBox toolBox;
    private TabPane instrumentsPane;

    public ProjectView() {
        present();
    }

    @Override
    public void initComponents() {
        toolBar = new ToolBar();
        toolBox = new ToolBox();
        instrumentsPane = new TabPane();
    }

    @Override
    public void setupComponents() {
        BorderPane.setMargin(instrumentsPane, new Insets(10));
        BorderPane.setMargin(toolBox, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(toolBar, new Insets(15, 10, 15, 10));
        BorderPane.setAlignment(instrumentsPane, Pos.CENTER);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/views.css")).toExternalForm());
        this.getStyleClass().add("project-view");
    }

    @Override
    public void setupLayout() {
        this.setTop(toolBar);
        this.setLeft(toolBox);
        this.setCenter(instrumentsPane);
    }

    @Override
    public void setupEventListeners() {
        EventBus.getInstance().subscribe(ProjectLoadedEvent.class, event -> {
            Platform.runLater(() -> {
                final ScorePartwise scorePartwise = event.getProject().getScorePartwise().get();

                for (ScorePart part : scorePartwise.getPartList().getScoreParts()) {
                    Tab instrumentTab = new Tab(part.getPartName());
                    instrumentTab.setClosable(false);

                    SheetPane sheetPane = SheetFactory.createSheetPane(extractInstrument(part.getPartName()));
                    sheetPane.bindProject(event.getProject());

                    instrumentTab.setContent(sheetPane);
                    instrumentsPane.getTabs().add(instrumentTab);
                }
            });
        });
        EventBus.getInstance().subscribe(ProjectCreatedEvent.class, event -> {
            Platform.runLater(() -> {
                final ScorePartwise scorePartwise = event.getProject().getScorePartwise().get();

                for (ScorePart part : scorePartwise.getPartList().getScoreParts()) {
                    Tab instrumentTab = new Tab(part.getPartName());
                    instrumentTab.setClosable(false);

                    SheetPane sheetPane = SheetFactory.createSheetPane(extractInstrument(part.getPartName()));
                    sheetPane.bindProject(event.getProject());

                    instrumentTab.setContent(sheetPane);
                    instrumentsPane.getTabs().add(instrumentTab);
                }
            });
        });
    }

    @Override
    public void setupEventHandlers() {

    }

    private Instrument extractInstrument(String name) {
        return switch (name) {
            case "Violin" -> new Instrument(Instrument.Name.Violin);
            case "Viola" -> new Instrument(Instrument.Name.Viola);
            case "Cello" -> new Instrument(Instrument.Name.Cello);
            case null, default -> throw new IllegalArgumentException();
        };
    }
}
