package com.norbjdk.museeditor.core.model.score;

import java.util.ArrayList;
import java.util.List;

public class Part {
    private List<Measure> measures = new ArrayList<>();

    public Part() {}

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }
}
