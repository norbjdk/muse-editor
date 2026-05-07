package com.muse.editor.ui.component.music;

import com.muse.editor.core.edit.Instrument;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class ViolinPane extends SheetPane {

    ViolinPane(Instrument instrument) {
        super(instrument);
    }

    @Override
    public void initComponents() {
        pageContainer = new VBox();

        titleLabel = new Label();
        subtitleLabel = new Label();
        composerLabel = new Label();
    }

    @Override
    public void setupComponents() {
        titleLabel.setText("Untitled");
        subtitleLabel.setText("Unsubtitled");
        composerLabel.setText("Uncomposed");
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/muse/editor/styles/music.css")).toExternalForm()
        );

        this.getStyleClass().add("sheet-pane");
        pageContainer.getStyleClass().add("page-container");

        titleLabel.getStyleClass().add("title-label");
        subtitleLabel.getStyleClass().add("subtitle-label");
        composerLabel.getStyleClass().add("composer-label");
    }

    @Override
    public void setupLayout() {
        setFitToWidth(true);
        StackPane wrapper = new StackPane(pageContainer);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(20, 40, 20, 40));
        setContent(wrapper);
    }

    @Override
    public void setupEventListeners() {
        if (project == null) return;

        project.getScorePartwise().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                System.out.println("Rebuilding GuitarPane...");
                Platform.runLater(() -> rebuild(part));
            }
        });
    }

    @Override
    public void setupEventHandlers() {

    }

    @Override
    protected void publishChange() {

    }

    @Override
    protected VBox buildHeader() {
        final VBox header = new VBox(4);

        header.setPadding(new Insets(0, 0, 20, 0));

        titleLabel.setText(project.getScorePartwise().get().getWorkTitle());
        subtitleLabel.setText(project.getScorePartwise().get().getAlbum());
        composerLabel.setText(project.getScorePartwise().get().getCreator());

        header.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                composerLabel
        );

        return header;
    }

    @Override
    protected VBox buildMeasures() {
        return new VBox();
    }
}
