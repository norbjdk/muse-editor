package com.muse.server.after.dto.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectRequest {
    private String  title;
    private String creator;
    private Boolean isPublic;
    private List<Long> collaboratorsIds = new ArrayList<>();

    public ProjectRequest() {}

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

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public List<Long> getCollaboratorsIds() {
        return collaboratorsIds;
    }

    public void setCollaboratorsIds(List<Long> collaboratorsIds) {
        this.collaboratorsIds = collaboratorsIds;
    }
}

