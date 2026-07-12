package com.muse.editor.core.user;

import com.muse.editor.core.model.dto.LoginResponse;
import com.muse.editor.util.Debug;

import java.util.prefs.Preferences;

public class TokenStorage {

    private static final String INSTANCE =
            System.getProperty("app.instance", "default");

    private static final Preferences prefs =
            Preferences.userRoot().node("muse-editor/" + INSTANCE);

    private static final String TOKEN_KEY = "auth_token";
    private static final String USER_ID_KEY = "user_id";
    private static final String USERNAME_KEY = "username";
    private static final String EMAIL_KEY = "email";

    public static void saveToken(String token) {
        prefs.put(TOKEN_KEY, token);
    }

    public static String getToken() {
        return prefs.get(TOKEN_KEY, null);
    }

    public static void saveUserId(Long userId) {
        if (userId != null) {
            prefs.putLong(USER_ID_KEY, userId);
        }
    }

    public static Long getUserId() {
        Long id = prefs.getLong(USER_ID_KEY, -1);
        return id;
    }

    public static void saveUsername(String username) {
        if (username != null) {
            prefs.put(USERNAME_KEY, username);
        }
    }

    public static String getUsername() {
        return prefs.get(USERNAME_KEY, null);
    }

    public static void saveEmail(String email) {
        if (email != null) {
            prefs.put(EMAIL_KEY, email);
        }
    }

    public static String getEmail() {
        return prefs.get(EMAIL_KEY, null);
    }

    public static void saveLoginResponse(LoginResponse response) {
        if (response == null) {
            Debug.fail("ERROR: LoginResponse is null!");
            return;
        }

        Debug.check("=== SAVING LOGIN RESPONSE ===");
        Debug.pass("ID: " + response.getId());
        Debug.check("Username: " + response.getUsername());
        Debug.check("Email: " + response.getEmail());
        Debug.check("Token length: " + (response.getToken() != null ? response.getToken().length() : 0));

        saveToken(response.getToken());
        saveUserId(response.getId() - 1);
        saveUsername(response.getUsername());
        saveEmail(response.getEmail());
    }

    public static boolean isLoggedIn() {
        String token = getToken();
        Long userId = getUserId();
        return token != null && !token.isEmpty() && userId != -1;
    }

    public static void clear() {
        prefs.remove(TOKEN_KEY);
        prefs.remove(USER_ID_KEY);
        prefs.remove(USERNAME_KEY);
        prefs.remove(EMAIL_KEY);
    }
}