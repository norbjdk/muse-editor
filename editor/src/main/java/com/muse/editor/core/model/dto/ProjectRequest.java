package com.muse.editor.core.model.dto;

import com.muse.editor.core.api.RequestDTO;

public class ProjectRequest implements RequestDTO {
    private String title;
    private String creator;
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

    public void setPublic(boolean p) {
        this.isPublic = p;
    }
}
