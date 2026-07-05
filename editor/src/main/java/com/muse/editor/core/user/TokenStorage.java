package com.muse.editor.core.user;

import java.util.prefs.Preferences;

public class TokenStorage {

    private static final String INSTANCE =
            System.getProperty("app.instance", "default");

    private static final Preferences prefs =
            Preferences.userRoot().node("muse-editor/" + INSTANCE);

    private static final String TOKEN_KEY = "auth_token";
    private static final String USER_KEY = "user_data";

    public static void saveToken(String token) {
        prefs.put(TOKEN_KEY, token);
    }

    public static String getToken() {
        return prefs.get(TOKEN_KEY, null);
    }

    public static void saveUser(String userJson) {
        prefs.put(USER_KEY, userJson);
    }

    public static String getUser() {
        return prefs.get(USER_KEY, null);
    }

    public static boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    public static void clear() {
        prefs.remove(TOKEN_KEY);
        prefs.remove(USER_KEY);
    }
}