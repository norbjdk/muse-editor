package com.muse.server.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sheets")
public class SheetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @Column(nullable = false)
    private Integer version = 1;

    @Column(columnDefinition = "TEXT")
    private String raxMXML;

    @Column(nullable = false)
    private Integer beats;

    @Column(nullable = false)
    private Integer beatType;

    @Column(name = "key_sig", nullable = false)
    private String keySig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getRaxMXML() {
        return raxMXML;
    }

    public void setRaxMXML(String raxMXML) {
        this.raxMXML = raxMXML;
    }

    public Integer getBeats() {
        return beats;
    }

    public void setBeats(Integer beats) {
        this.beats = beats;
    }

    public Integer getBeatType() {
        return beatType;
    }

    public void setBeatType(Integer beatType) {
        this.beatType = beatType;
    }

    public String getKeySig() {
        return keySig;
    }

    public void setKeySig(String keySig) {
        this.keySig = keySig;
    }
}
