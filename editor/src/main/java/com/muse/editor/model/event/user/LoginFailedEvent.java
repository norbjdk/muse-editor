package com.muse.editor.model.event.user;

import com.muse.editor.model.event.AppEvent;

public class LoginFailedEvent implements AppEvent {
    private String errorMessage;

    public LoginFailedEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() { return errorMessage; }
}

