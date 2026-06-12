package com.muse.editor.core.model.music;

import java.util.ArrayList;
import java.util.List;

public class ScorePartwise {
    private String           workTitle;
    private String           creator;
    private PartList         partList;
    private final List<Part> parts = new ArrayList<>();

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

    public PartList getPartList() {
        return partList;
    }

    public void setPartList(PartList partList) {
        this.partList = partList;
    }

    public List<Part> getParts() {
        return parts;
    }


}
