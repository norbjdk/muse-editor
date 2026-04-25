package com.muse.editor.core.model.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewProjectRequest {
    private final List<String> instruments = new ArrayList<>();
    private String title;
    private String subtitle;
    private String composer;

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void addInstruments(String ... instruments) {
        this.instruments.add(Arrays.toString(instruments));
    }
}
