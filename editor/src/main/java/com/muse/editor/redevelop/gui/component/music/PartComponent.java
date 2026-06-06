package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.edit.ScoreManager;
import com.muse.editor.redevelop.core.model.music.Attributes;
import com.muse.editor.redevelop.core.model.music.Measure;
import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.core.model.music.ScorePart;
import com.muse.editor.redevelop.core.project.ProjectManager;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.project.PartComponentChangedEvent;
import com.muse.editor.redevelop.gui.model.Presentable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class PartComponent extends Presentable<FlowPane> {
    private final ProjectManager projectManager = ProjectManager.getInstance();
    private final ScoreManager   scoreManager   = ScoreManager.getInstance();

    private final String         partID;
    private final ScorePart.Name partName;

    private ObjectProperty<Part> partProperty = new SimpleObjectProperty<>(null);

    public PartComponent(String partID, ScorePart.Name partName) {
        super(new FlowPane());

        this.partID   = partID;
        this.partName = partName;
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void setupComponents() {

    }

    @Override
    protected void setupStyle() {
        root.setVgap(20);
        root.setOrientation(Orientation.HORIZONTAL);
        root.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void setupLayout() {
    }

    @Override
    protected void setupEventListeners() {
        EventBus.getInstance().subscribe(PartComponentChangedEvent.class, partComponentChangedEvent -> {
            if (partComponentChangedEvent.getPart() != null && partComponentChangedEvent.getPart().getId().equals(this.partID)) {
                redraw();
            }
        });
    }

    @Override
    protected void setupEventHandlers() {

    }

    public void assignPart(ObjectProperty<Part> part) {
        this.partProperty.bind(part);
    }

    public String getPartID() {
        return partID;
    }

    public ScorePart.Name getPartName() {
        return partName;
    }

    public ObjectProperty<Part> partProperty() {
        return partProperty;
    }

    private void redraw() {
        root.getChildren().clear();

        final ObservableList<ObjectProperty<Measure>> measures = ScoreManager.getInstance().getMeasures(partID);

        for (ObjectProperty<Measure> measureProperty : measures) {
            if (!partName.equals(ScorePart.Name.Piano)) {
                final MeasureComponent measureComponent = new MeasureComponent();

                measureComponent.assignMeasure(measureProperty, 1);

                root.getChildren().add(measureComponent.getRoot());
            } else {
                final VBox measuresContainer = new VBox(40);

                final MeasureComponent upperMeasureComponent = new MeasureComponent();
                final MeasureComponent bottomMeasureComponent = new MeasureComponent();

                upperMeasureComponent.assignMeasure(measureProperty, 1);
                bottomMeasureComponent.assignMeasure(measureProperty, 2);

                measuresContainer.getChildren().addAll(
                        upperMeasureComponent.getRoot(),
                        bottomMeasureComponent.getRoot()
                );

                root.getChildren().add(measuresContainer);
            }

        }
    }
}
