package com.muse.editor.core.model.dto;

import com.muse.editor.core.model.score.Attributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewProjectRequest {
    private final List<String> instruments = new ArrayList<>();
    private String title;
    private String subtitle;
    private String composer;
    private int    beats;
    private int    beatType;
    private int    tempo;
    private int    measures;

    public NewProjectRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public int getMeasuresCount() {
        return measures;
    }

    public void setMeasuresCount(int measuresCount) {
        this.measures = measuresCount;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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
