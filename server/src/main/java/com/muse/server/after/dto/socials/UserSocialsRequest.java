package com.muse.server.after.dto.socials;

public class UserSocialsRequest {
    private String youtubeId;
    private String spotifyId;
    private String instagramId;
    private String tiktokId;
    private String wwwPath;

    public UserSocialsRequest() {}

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
