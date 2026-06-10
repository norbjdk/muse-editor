package com.muse.editor.redevelop.event.user;

import com.muse.editor.redevelop.core.model.dto.LoginRequest;
import com.muse.editor.redevelop.event.model.AppEvent;

public class LoginEvent extends AppEvent {
    private final LoginRequest request;

    public LoginEvent(LoginRequest request) {
        this.request = request;
    }

    public LoginRequest getRequest() {
        return request;
    }
}
