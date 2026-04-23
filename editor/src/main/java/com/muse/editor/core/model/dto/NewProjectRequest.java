package com.muse.editor.core.model.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewProjectRequest {
    private final List<String> instruments = new ArrayList<>();
    private String title;
    private String composer;
    private String album;

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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void addInstruments(String ... instruments) {
        this.instruments.add(Arrays.toString(instruments));
    }
}
