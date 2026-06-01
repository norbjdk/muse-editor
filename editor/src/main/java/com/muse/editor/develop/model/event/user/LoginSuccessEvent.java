package com.muse.editor.develop.model.event.user;

import com.muse.editor.develop.core.user.User;
import com.muse.editor.develop.model.event.AppEvent;

public class LoginSuccessEvent implements AppEvent {
    private User user;

    public LoginSuccessEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
