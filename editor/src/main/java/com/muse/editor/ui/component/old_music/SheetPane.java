package com.muse.editor.ui.component.old_music;

import com.muse.editor.core.model.score.ScorePartwise;
import com.muse.editor.core.project.Project;
import com.muse.editor.ui.model.Presentable;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class SheetPane extends ScrollPane implements Presentable {
    private final static double PAGE_WIDTH = 794;
    private final static double PAGE_MARGIN_H = 72;
    private final static double PAGE_MARGIN_V = 60;
    private static final double SYSTEM_WIDTH = PAGE_WIDTH - PAGE_MARGIN_H * 2;

    private VBox pageContainer;

    private Consumer<String> onContentChanged;
    private Consumer<String> onSelectionChanged;
    private Runnable onDocumentReady;

    private final BooleanProperty sheetReady = new SimpleBooleanProperty(false);
    private Project boundProject;

    List<PagePane> pages = new ArrayList<>();

    public SheetPane() {
        present();
    }

    @Override
    public void initComponents() {
        pageContainer = new VBox();
    }

    @Override
    public void setupComponents() {
        setFitToWidth(true);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("sheet-pane");

        pageContainer.getStyleClass().add("page-container");
    }

    @Override
    public void setupLayout() {
        setContent(pageContainer);
    }

    @Override
    public void setupEventListeners() {
        if (boundProject != null) {
            boundProject.getScorePartwise().addListener((obs, old, newScore) -> {
                if (newScore != null) {
                    Platform.runLater(() -> rebuild(newScore, getWidth()));
                }
            });
        }

        widthProperty().addListener((obs, old, newWidth) -> {
            if (boundProject != null && boundProject.getScorePartwise().get() != null) {
                rebuild(boundProject.getScorePartwise().get(), newWidth.doubleValue());
            }
        });
    }

    @Override
    public void setupEventHandlers() {}

    public void bindProject(Project project) {
        this.boundProject = project;

        final ScorePartwise score = project.getScorePartwise().get();
        if (score != null) {
            rebuild(score, getWidth());
        }
    }

    public void rebuild(ScorePartwise scorePartwise, double viewWidth) {
        pageContainer.getChildren().clear();

        if (scorePartwise == null) {
            showEmptyState();
            return;
        }

        final VBox page = buildPage(scorePartwise.getWorkTitle(), scorePartwise.getCreator());
//        final var parts = scorePartwise.getParts();
//        final int measureCount = parts.getFirst().getMeasures().size();
//        final double measureWidth = SheetMetrics.MEASURE_MIN_WIDTH * 4;
//
//        for (int i = 0; i < measureCount; i++) {
//            final SystemPane system = new SystemPane();
//            for (int p = 0; p < parts.size(); p++) {
//                final var measures = parts.get(p).getMeasures();
//                if (i < measures.size()) {
//                    boolean isFirst = (i == 0);
//                    MeasurePane mp = new MeasurePane(measures.get(i), isFirst, measureWidth);
//                    system.addStaff(mp);
//                }
//            }
//            page.getChildren().add(system);
//        }

        pageContainer.getChildren().add(page);
        sheetReady.set(true);
    }

    private VBox buildPage(String title, String composer) {
        final VBox page = new VBox(0);

        page.setStyle("""
            -fx-background-color: white;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8, 0, 0, 2);
            """);
        page.setMaxWidth(PAGE_WIDTH);
        page.setMinWidth(PAGE_WIDTH);
        page.setPadding(new Insets(PAGE_MARGIN_V, PAGE_MARGIN_H, PAGE_MARGIN_V, PAGE_MARGIN_H));

        if (title != null && !title.isBlank()) {
            VBox header = buildHeader(title, composer);
            page.getChildren().add(header);
        }

        return page;
    }

    private VBox buildHeader(String title, String composer) {
        final VBox header = new VBox(4);
        header.setPadding(new Insets(0, 0, 20, 0));

        final Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Georgia", 22));
        titleLabel.setTextFill(Color.web("#1A1A1A"));
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setStyle("-fx-alignment: center;");

        header.getChildren().add(titleLabel);

        if (composer != null && !composer.isBlank()) {
            final Label composerLabel = new Label(composer);
            composerLabel.setFont(Font.font("Georgia", 13));
            composerLabel.setTextFill(Color.web("#4A4540"));
            composerLabel.setMaxWidth(Double.MAX_VALUE);
            composerLabel.setStyle("-fx-alignment: center-right;");
            header.getChildren().add(composerLabel);
        }

        return header;
    }

    private void showEmptyState() {
        final Label empty = new Label("Open any MusicXML file");
        empty.setStyle("""
            -fx-font-size: 16px;
            -fx-text-fill: #B0A898;
            -fx-padding: 60;
            """);
        pageContainer.getChildren().add(empty);
    }

    public BooleanProperty    sheetReadyProperty() {
        return sheetReady;
    }
}
