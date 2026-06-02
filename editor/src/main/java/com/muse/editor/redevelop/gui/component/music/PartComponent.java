package com.muse.editor.redevelop.gui.component.music;

import com.muse.editor.redevelop.core.model.music.ScorePart;
import com.muse.editor.redevelop.gui.model.Presentable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class PartComponent extends Presentable<ScrollPane> {
    private final double PAGE_WIDTH    = 794;
    private final double PAGE_MARGIN_H = 72;
    private final double PAGE_MARGIN_V = 60;

    private final String         partID;
    private final ScorePart.Name partName;

    private VBox pageContainer;

    public PartComponent(String partID, ScorePart.Name name) {
        super(new ScrollPane());

        this.partID = partID;
        this.partName = name;
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

    public String getPartID() {
        return partID;
    }

    public ScorePart.Name getPartName() {
        return partName;
    }
}
