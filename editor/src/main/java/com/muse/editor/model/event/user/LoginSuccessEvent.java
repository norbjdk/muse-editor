package com.muse.editor.model.event.user;

import com.muse.editor.core.user.User;
import com.muse.editor.model.event.AppEvent;

public class LoginSuccessEvent implements AppEvent {
    private User user;

    public LoginSuccessEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
