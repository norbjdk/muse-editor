package com.muse.editor.core.user;

import com.muse.editor.app.ClientService;
import com.muse.editor.core.api.ApiBuilder;
import com.muse.editor.core.cloud.CloudSyncService;
import com.muse.editor.core.edit.CursorModel;
import com.muse.editor.core.edit.EditorState;
import com.muse.editor.core.edit.ScoreManager;
import com.muse.editor.core.model.dto.LoginRequest;
import com.muse.editor.core.model.dto.LoginResponse;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.user.LoggedInEvent;
import com.muse.editor.event.user.LoggedOutEvent;
import com.muse.editor.event.user.LoginEvent;
import com.muse.editor.event.user.LogoutEvent;
import com.muse.editor.util.Debug;

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
        ClientService.getInstance().leaveSession();

        ClientService.getInstance().disconnect();

        CloudSyncService.getInstance().detach();
        ProjectManager.getInstance().reset();
        ScoreManager.getInstance().reset();
        CursorModel.getInstance().reset();
        EditorState.getInstance().reset();

        TokenStorage.clear();
        UserManager.getInstance().reset();
    }

    private void login(LoginRequest request) {
        try {
            TokenStorage.clear();

            final LoginResponse response = ApiBuilder.post("/api/v1/auth/login", LoginResponse.class, request);

            if (response == null) return;

            TokenStorage.saveLoginResponse(response);

            Debug.pass("Logged in as: " + TokenStorage.getUsername() + " (ID: " + TokenStorage.getUserId() + ")");

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
