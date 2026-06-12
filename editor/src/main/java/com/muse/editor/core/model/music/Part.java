package com.muse.editor.core.model.music;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Part {
    private String id;

    private ObservableList<Measure> measures = FXCollections.observableArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObservableList<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(ObservableList<Measure> measures) {
        this.measures = measures;
    }
}
