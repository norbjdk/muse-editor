package com.muse.editor.ui.component.music;

import com.muse.editor.core.edit.Instrument;
import com.muse.editor.core.model.score.Measure;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.List;

public class StringSheet extends SheetPane {
    StringSheet(Instrument instrument) {
        super(instrument);
    }

    @Override
    protected void publishChange() {

    }

    @Override
    protected FlowPane buildMeasures() {
        final FlowPane measures = new FlowPane();
        measures.setVgap(20);
        measures.setOrientation(Orientation.HORIZONTAL);
        List<Measure> measureList = part.getMeasures();

        for (Measure measure : measureList) {
            final MeasureComponent measureComponent = new MeasureComponent();

            measureComponent.assignMeasure(measure);

            measures.getChildren().add(measureComponent);
        }

        return measures;
    }

    @Override
    public void initComponents() {
        super.initComponents();
    }

    @Override
    public void setupComponents() {
    }

    @Override
    public void setupStyle() {
        super.setupStyle();
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
    }

    @Override
    public void setupEventHandlers() {

    }
}
