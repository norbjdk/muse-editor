package com.muse.editor.core.model.music;

import java.util.ArrayList;
import java.util.List;

public class PartList {
    private List<ScorePart> scoreParts = new ArrayList<>();

    public List<ScorePart> getScoreParts() {
        return scoreParts;
    }

    public void setScoreParts(List<ScorePart> scoreParts) {
        this.scoreParts = scoreParts;
    }
}
