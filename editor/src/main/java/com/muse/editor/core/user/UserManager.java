package com.muse.editor.core.user;

import java.io.IOException;

public class UserManager {
    private static final UserManager instance = new UserManager();

    private final UserService userService;

    public static UserManager getInstance() {
        return instance;
    }

    private UserManager() {
        userService = UserService.getInstance();
    }

    public User getCurrentUser() throws IOException {
        return userService.getCurrentUser();
    }

    public User getUserByUsername(String username) throws IOException {
        return userService.getUserByUsername(username);
    }
}
