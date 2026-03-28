package com.muse.editor.ui.component;

import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.SpaceFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Objects;

import static com.muse.editor.ui.util.SpaceFactory.createSpacer;

public class StatusBar extends HBox implements Presentable {

    private Label noteCountLabel;
    private Label measureNumberLabel;
    private Label savedLabel;
    private Label docTitleLabel;

    public StatusBar() {
        present();
    }

    @Override
    public void initComponents() {
        noteCountLabel = new Label();
        measureNumberLabel = new Label();
        savedLabel = new Label();
        docTitleLabel = new Label();
    }

    @Override
    public void setupComponents() {
        noteCountLabel.setText("0 notes");
        measureNumberLabel.setText("Measure: 0");
        savedLabel.setText("Saved");
        docTitleLabel.setText("Document name...");
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("status-bar");

        noteCountLabel.getStyleClass().add("status-bar-label");
        measureNumberLabel.getStyleClass().add("status-bar-label");
        savedLabel.getStyleClass().add("status-bar-label");
        docTitleLabel.getStyleClass().add("status-bar-label");
    }

    @Override
    public void setupLayout() {
        this.getChildren().addAll(
                noteCountLabel,
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                measureNumberLabel,
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                savedLabel,
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                docTitleLabel
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
