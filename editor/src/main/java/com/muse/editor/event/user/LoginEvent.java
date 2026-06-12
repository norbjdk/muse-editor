package com.muse.editor.event.user;

import com.muse.editor.core.model.dto.LoginRequest;
import com.muse.editor.event.model.AppEvent;

public class LoginEvent extends AppEvent {
    private final LoginRequest request;

    public LoginEvent(LoginRequest request) {
        this.request = request;
    }

    public LoginRequest getRequest() {
        return request;
    }
}
