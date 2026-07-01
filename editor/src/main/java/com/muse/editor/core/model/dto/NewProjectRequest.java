package com.muse.editor.core.model.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewProjectRequest {
    private final List<String> instruments = new ArrayList<>();
    private String workTitle;
    private String creator;
    private int    beats;
    private int    beatType;
    private int    tempo;
    private int    measures;
    private List<Long> collaboratorsId = new ArrayList<>();

    public NewProjectRequest() {}

    public String getWorkTitle() {
        return workTitle;
    }

    public void setWorkTitle(String workTitle) {
        this.workTitle = workTitle;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<Long> getCollaboratorsId() {
        return collaboratorsId;
    }

    public void setCollaboratorsId(List<Long> collaboratorsId) {
        this.collaboratorsId = collaboratorsId;
    }

    public int getBeats() {
        return beats;
    }

    public void setBeats(int beats) {
        this.beats = beats;
    }

    public int getBeatType() {
        return beatType;
    }

    public void setBeatType(int beatType) {
        this.beatType = beatType;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getMeasures() {
        return measures;
    }

    public void setMeasures(int measures) {
        this.measures = measures;
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void addInstruments(String ... instruments) {
        this.instruments.add(Arrays.toString(instruments));
    }
}
