package com.muse.editor.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muse.editor.core.api.ResponseDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectResponse implements ResponseDTO {
    private Long id;
    private String title;
    private String composer;
    private boolean isPublic;
    private String cloudFilePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getCloudFilePath() {
        return cloudFilePath;
    }

    public void setCloudFilePath(String cloudFilePath) {
        this.cloudFilePath = cloudFilePath;
    }
}