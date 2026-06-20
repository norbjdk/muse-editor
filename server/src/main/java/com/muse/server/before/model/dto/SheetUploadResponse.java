package com.muse.server.before.model.dto;

public class SheetUploadResponse {
    private boolean success;
    private String message;
    private SheetResponse sheet;
    private String fileUrl;
    private String coverUrl;

    public SheetUploadResponse() {}

    public SheetUploadResponse(boolean success, String message, SheetResponse sheet) {
        this.success = success;
        this.message = message;
        this.sheet = sheet;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SheetResponse getSheet() {
        return sheet;
    }

    public void setSheet(SheetResponse sheet) {
        this.sheet = sheet;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
