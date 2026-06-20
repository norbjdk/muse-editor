package com.muse.server.before.model.dto;

import jakarta.validation.constraints.NotBlank;

public class SheetRequest {
    @NotBlank(message = "Track title is required")
    private String title;

    @NotBlank(message = "Composer name is required")
    private String composer;

    private String album;

    private String description;

    private String instrument;

    private transient String mxmlFileUrl;
    private transient String coverImageUrl;

    public SheetRequest() {}

    public SheetRequest(String title, String composer, String album) {
        this.title = title;
        this.composer = composer;
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getMxmlFileUrl() {
        return mxmlFileUrl;
    }

    public void setMxmlFileUrl(String mxmlFileUrl) {
        this.mxmlFileUrl = mxmlFileUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
}
