package com.muse.editor.model.event;

public class LoginFailedEvent implements AppEvent {
    private String errorMessage;

    public LoginFailedEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() { return errorMessage; }
}

