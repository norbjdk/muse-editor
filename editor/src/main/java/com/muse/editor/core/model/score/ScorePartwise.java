package com.muse.editor.core.model.score;

import java.util.ArrayList;
import java.util.List;

public class ScorePartwise {
    private String workTitle;
    private String creator;
    private String album;
    private PartList partList;
    private List<Part> parts = new ArrayList<>();

    public ScorePartwise() {}

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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public PartList getPartList() {
        return partList;
    }

    public void setPartList(PartList partList) {
        this.partList = partList;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
