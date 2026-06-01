package com.muse.editor.develop.core.edit;

import com.muse.editor.develop.core.model.score.Measure;
import com.muse.editor.develop.core.model.score.Part;

import java.util.List;

public class CursorModel {
    private static final CursorModel instance = new CursorModel();

    public static CursorModel getInstance() {
        return instance;
    }

    private CursorModel() {}

    private Part part;
    private int  measureIndex;
    private int  noteIndex;

    public void bindPart(Part part) {
        this.part         = part;
        this.measureIndex = 0;
        this.noteIndex    = 0;

        System.out.println("New bind for id part:" + part.getId());
    }

    public int getMeasureIndex() {
        return measureIndex;
    }

    public int getNoteIndex() {
        return noteIndex;
    }

    public Measure currentMeasure() {
        if (part == null) return null;

        final List<Measure> measures = part.getMeasures();

        if (measureIndex >= measures.size()) return null;

        return measures.get(measureIndex);
    }

    public void moveToNextMeasure() {
        if (part == null) return;

        if (measureIndex < part.getMeasures().size() - 1) {
            measureIndex++;
            noteIndex = 0;
        }
    }

    public void moveToPrevMeasure() {
        if (part == null) return;

        if (measureIndex > 0) {
            measureIndex--;
            final Measure measure = currentMeasure();
            noteIndex = measure != null ? measure.getNotes().size() : 0;
        }
    }

    public void moveLeft() {
        if (noteIndex > 0) {
            noteIndex--;
        } else if (measureIndex > 0) {
            moveToPrevMeasure();
        }
    }

    public void moveRight() {
        final Measure measure = currentMeasure();

        if (measure == null) return;

        if (noteIndex < measure.getNotes().size()) {
            noteIndex++;
        } else {
            moveToNextMeasure();
        }
    }
}
