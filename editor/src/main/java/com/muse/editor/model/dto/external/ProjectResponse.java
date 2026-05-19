package com.muse.editor.model.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectResponse {
    private Long id;
    private String title;
    private String subtitle;
    private String composer;
    private String cloudFilePath;
    private String cloudCoverUrl;
    private Boolean isPublic;

    public ProjectResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getComposer() { return composer; }
    public void setComposer(String composer) { this.composer = composer; }

    public String getCloudFilePath() { return cloudFilePath; }
    public void setCloudFilePath(String cloudFilePath) { this.cloudFilePath = cloudFilePath; }

    public String getCloudCoverUrl() { return cloudCoverUrl; }
    public void setCloudCoverUrl(String cloudCoverUrl) { this.cloudCoverUrl = cloudCoverUrl; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
}