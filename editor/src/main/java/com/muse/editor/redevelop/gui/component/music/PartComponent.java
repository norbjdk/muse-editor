package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.model.music.Part;
import com.muse.editor.redevelop.core.model.music.ScorePart;
import com.muse.editor.redevelop.core.project.ProjectManager;
import com.muse.editor.redevelop.gui.model.Presentable;
import javafx.scene.layout.FlowPane;

public class PartComponent extends Presentable<FlowPane> {
    private final ProjectManager projectManager = ProjectManager.getInstance();

    private final String         partID;
    private final ScorePart.Name partName;

    private Part part;

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

    }

    @Override
    protected void setupEventHandlers() {

    }

    public void assignPart(Part part) {
        this.part = part;
    }

    public String getPartID() {
        return partID;
    }

    public ScorePart.Name getPartName() {
        return partName;
    }
}
