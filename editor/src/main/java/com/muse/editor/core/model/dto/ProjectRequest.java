package com.muse.editor.core.model.dto;

import com.muse.editor.core.api.RequestDTO;

import java.util.ArrayList;
import java.util.List;

public class ProjectRequest implements RequestDTO {
    private String title;
    private String creator;
    private List<Long> collaboratorsId = new ArrayList<>();
    private boolean isPublic;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setPublic(boolean p) {
        this.isPublic = p;
    }
}
