package com.muse.editor.develop.model.event.user;

import com.muse.editor.develop.model.event.AppEvent;

public class LoginFailedEvent implements AppEvent {
    private String errorMessage;

    public LoginFailedEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() { return errorMessage; }
}

