package com.muse.server.model.dto;

import java.time.LocalDateTime;

public class ProjectResponse {

    private Long id;
    private Long ownerId;
    private String ownerUsername;
    private String title;
    private String subtitle;
    private String composer;
    private String cloudFilePath;
    private String cloudCoverUrl;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}