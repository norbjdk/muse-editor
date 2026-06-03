package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.core.model.music.ScorePart;
import com.muse.editor.redevelop.core.project.ProjectManager;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.project.PartComponentChangedEvent;
import com.muse.editor.redevelop.gui.model.Presentable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class PartComponent extends Presentable<FlowPane> {
    private final ProjectManager projectManager = ProjectManager.getInstance();

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

    }

    @Override
    protected void setupLayout() {

    }

    @Override
    protected void setupEventListeners() {
        EventBus.getInstance().subscribe(PartComponentChangedEvent.class, partComponentChangedEvent -> {
            redraw();
        });
        partProperty.addListener((observableValue, part, t1) -> {
            if (t1 != null) {

            }
        });
    }

    @Override
    protected void setupEventHandlers() {

    }

    public void assignPart(Part part) {
        partProperty.set(part);
    }

    public String getPartID() {
        return partID;
    }

    public ScorePart.Name getPartName() {
        return partName;
    }

    private void redraw() {
        root.getChildren().clear();

        root.getChildren().add(new Label(partName.getValue()));
    }
}
