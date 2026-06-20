package com.muse.server.before.model.dto.social;

public class SocialRequest {
    private String youtubeId;
    private String spotifyId;
    private String instagramId;
    private String tiktokId;
    private String personalWebUrl;

    public SocialRequest() {}

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
