package com.muse.editor.model.event;

import com.muse.editor.core.user.User;

public class LoginSuccessEvent implements AppEvent {
    private User user;

    public LoginSuccessEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
