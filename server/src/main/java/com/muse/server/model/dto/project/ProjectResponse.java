package com.muse.server.model.dto.project;

public class ProjectResponse {
    private Long    id;
    private String  ownerId;
    private String  title;
    private String  subtitle;
    private String  composer;
    private String  cloudCoverUrl;
    private Boolean isPublic;
    private String cloudFilePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getCloudCoverUrl() {
        return cloudCoverUrl;
    }

    public void setCloudCoverUrl(String cloudCoverUrl) {
        this.cloudCoverUrl = cloudCoverUrl;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getCloudFilePath() {
        return cloudFilePath;
    }

    public void setCloudFilePath(String cloudFilePath) {
        this.cloudFilePath = cloudFilePath;
    }
}
