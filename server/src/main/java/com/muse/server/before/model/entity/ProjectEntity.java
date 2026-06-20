package com.muse.server.before.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(nullable = false)
    private String title;

    private String subtitle;
    private String composer;
    private String localFilePath;
    private String cloudFilePath;
    private String cloudCoverUrl;

    @Column(nullable = false)
    private Boolean isPublic = false;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMemberEntity> members = new ArrayList();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SheetEntity> sheets = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
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

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getCloudFilePath() {
        return cloudFilePath;
    }

    public void setCloudFilePath(String cloudFilePath) {
        this.cloudFilePath = cloudFilePath;
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

    public List<ProjectMemberEntity> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMemberEntity> members) {
        this.members = members;
    }

    public List<SheetEntity> getSheets() {
        return sheets;
    }

    public void setSheets(List<SheetEntity> sheets) {
        this.sheets = sheets;
    }
}
