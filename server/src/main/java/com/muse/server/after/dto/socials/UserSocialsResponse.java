package com.muse.server.after.dto.socials;

public class UserSocialsResponse {
    private Long id;
    private Long userId;
    private String username;
    private String youtubeId;
    private String spotifyId;
    private String instagramId;
    private String tiktokId;
    private String wwwPath;

    public UserSocialsResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getWwwPath() {
        return wwwPath;
    }

    public void setWwwPath(String wwwPath) {
        this.wwwPath = wwwPath;
    }
}
