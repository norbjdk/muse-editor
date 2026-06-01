package com.muse.editor.develop.core.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.develop.app.config.ApiConfig;

public class CurrentUserService {
    private static final CurrentUserService currentUserService = new CurrentUserService();

    public static CurrentUserService getCurrentUserService() {
        return currentUserService;
    }

    private static User currentUser;
    private static final ObjectMapper mapper = ApiConfig.getObjectMapper();

    private CurrentUserService() {}

    public static void setCurrentUser(User user) {
        currentUser = user;
        try {
            String userJson = mapper.writeValueAsString(user);
            TokenStorage.saveUser(userJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            String userJson = TokenStorage.getUser();
            if (userJson != null && !userJson.isEmpty()) {
                try {
                    currentUser = mapper.readValue(userJson, User.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return currentUser;
    }

    public boolean isLoggedIn() {
        return TokenStorage.isLoggedIn() && getCurrentUser() != null;
    }

    public void logout() {
        currentUser = null;
        TokenStorage.clear();
    }

    public static String getUsername() {
        User user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    public static Long getUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }
}
