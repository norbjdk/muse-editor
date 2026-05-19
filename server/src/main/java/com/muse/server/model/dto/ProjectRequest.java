package com.muse.server.model.dto;

public class ProjectRequest {

    private String title;
    private String subtitle;
    private String composer;
    private Boolean isPublic = false;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getComposer() { return composer; }
    public void setComposer(String composer) { this.composer = composer; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
}