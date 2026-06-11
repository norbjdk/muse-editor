package com.muse.editor.core.user;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;

public class UserManager {
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>(null);

    private static final UserManager instance = new UserManager();

    public static UserManager getInstance() {
        return instance;
    }

    private UserManager() {}

    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }

    public User getCurrentUser() throws IOException {
        return currentUser.get();
    }

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public User getUserByUsername(String username) throws IOException {
        return null;
    }
}
