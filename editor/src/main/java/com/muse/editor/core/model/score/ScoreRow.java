package com.muse.editor.core.model.score;

import com.muse.editor.ui.component.old_music.MeasurePane;
import com.muse.editor.ui.model.Presentable;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class ScoreRow extends Pane implements Presentable {

    private final List<Part> parts;
    private final int measureStart;
    private final int measureEnd;
    private final boolean isFirst;
    private final double rowWidth;

    private final List<List<MeasurePane>> measurePanes = new ArrayList<>();

    public ScoreRow(List<Part> parts, int measureStart, int measureEnd, boolean isFirst, double rowWidth) {
        this.parts = parts;
        this.measureStart = measureStart;
        this.measureEnd = measureEnd;
        this.isFirst = isFirst;
        this.rowWidth = rowWidth;
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {

    }

    @Override
    public void setupLayout() {

    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
