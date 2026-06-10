package com.muse.editor.redevelop.core.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.muse.editor.redevelop.core.api.ApiBuilder;
import com.muse.editor.redevelop.core.api.ApiConfig;
import com.muse.editor.redevelop.core.model.dto.LoginRequest;
import com.muse.editor.redevelop.core.model.dto.LoginResponse;
import com.muse.editor.redevelop.event.EventBus;
import com.muse.editor.redevelop.event.user.LoggedInEvent;
import com.muse.editor.redevelop.event.user.LoggedOutEvent;
import com.muse.editor.redevelop.event.user.LoginEvent;
import com.muse.editor.redevelop.event.user.LogoutEvent;
import javafx.application.Platform;

import java.io.IOException;

public class AuthService {
    private static final AuthService instance = new AuthService();

    private final UserService userService = UserService.getInstance();

    public static AuthService getInstance() {
        return instance;
    }

    private AuthService() {
        setupEventListeners();
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(LoginEvent.class, loginEvent -> {
            handleLoginEvent(loginEvent.getRequest());
        });
        EventBus.getInstance().subscribe(LogoutEvent.class, logoutEvent -> {
            handleLogoutEvent();
        });
    }

    private void handleLoginEvent(LoginRequest request) {
        if (request == null) return;

        login(request);
    }


    private void handleLogoutEvent() {
        if (TokenStorage.isLoggedIn()) {
            logout();
        }
    }

    private void login(LoginRequest request) {
        try {
            final LoginResponse response = ApiBuilder.post("/api/v1/auth/login", LoginResponse.class, request);

            if (response == null) return;

            TokenStorage.saveToken(response.getToken());

            final User user = new User.Builder()
                    .setId(response.getId())
                    .setUsername(response.getUsername())
                    .setEmail(response.getEmail())
                    .build();

            UserManager.getInstance().setCurrentUser(user);
            EventBus.getInstance().publish(new LoggedInEvent());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        TokenStorage.clear();
        UserManager.getInstance().setCurrentUser(null);
        EventBus.getInstance().publish(new LoggedOutEvent());
    }
}
