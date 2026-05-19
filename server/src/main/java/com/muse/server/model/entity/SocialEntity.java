package com.muse.server.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "socials")
public class SocialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(unique = true)
    private String youtubeId;

    @Column(unique = true)
    private String spotifyId;

    @Column(unique = true)
    private String instagramId;

    @Column(unique = true)
    private String tiktokId;

    private String personalWebUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(String instagramId) {
        this.instagramId = instagramId;
    }

    public String getTiktokId() {
        return tiktokId;
    }

    public void setTiktokId(String tiktokId) {
        this.tiktokId = tiktokId;
    }

    public String getPersonalWebUrl() {
        return personalWebUrl;
    }

    public void setPersonalWebUrl(String personalWebUrl) {
        this.personalWebUrl = personalWebUrl;
    }
}
