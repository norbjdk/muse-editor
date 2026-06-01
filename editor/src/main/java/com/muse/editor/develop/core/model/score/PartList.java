package com.muse.editor.develop.core.model.score;

import java.util.ArrayList;
import java.util.List;

public class PartList {
    private List<ScorePart> scoreParts = new ArrayList<>();

    public PartList() {}

    public List<ScorePart> getScoreParts() {
        return scoreParts;
    }

    public void setScoreParts(List<ScorePart> scoreParts) {
        this.scoreParts = scoreParts;
    }
}
