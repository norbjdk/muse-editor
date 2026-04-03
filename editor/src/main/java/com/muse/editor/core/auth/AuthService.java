package com.muse.editor.core.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.core.EventBus;
import com.muse.editor.core.model.dto.LoginRequest;
import com.muse.editor.core.model.dto.LoginResponse;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.core.user.User;
import com.muse.editor.core.user.UserService;
import com.muse.editor.model.event.LoginFailedEvent;
import com.muse.editor.model.event.LoginSuccessEvent;
import com.muse.editor.model.event.LogoutEvent;
import com.muse.editor.model.event.OpenLoginDialogEvent;
import com.muse.editor.ui.view.LoginDialog;
import javafx.application.Platform;
import okhttp3.*;

import java.io.IOException;

public class AuthService {
    private final static AuthService instance = new AuthService();

    public static AuthService getInstance() {
        return instance;
    }

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final UserService userService;

    private AuthService() {
        client = ApiConfig.getHttpClient();
        mapper = ApiConfig.getObjectMapper();
        userService = UserService.getInstance();

        setupEventListeners();
    }

    private void setupEventListeners() {
        EventBus.getInstance().subscribe(OpenLoginDialogEvent.class, openLoginDialogEvent -> {
            handleLoginUserRequested();
        });
    }

    private void handleLoginUserRequested() {
        Platform.runLater(() -> {
            new LoginDialog().showAndWait();
        });
    }

    public void login(LoginRequest loginRequest) {
        new  Thread(()  -> {
            try {
                final String jsonBody = mapper.writeValueAsString(loginRequest);

                final RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));

                final String url = ApiConfig.getBaseUrl() + "/api/v1/auth/login";

                final Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        final LoginResponse loginResponse = mapper.readValue(
                                response.body().string(),
                                LoginResponse.class
                        );

                        TokenStorage.saveToken(loginResponse.getToken());

                        final User user = new User.Builder()
                                .setId(loginResponse.getId())
                                .setUsername(loginResponse.getUsername())
                                .setEmail(loginResponse.getEmail())
                                .declareRole(loginResponse.getUsername())
                                .build();

                        UserService.getInstance().setUser(user);

                        System.out.println("Logged in");
                        EventBus.getInstance().publish(new LoginSuccessEvent((user)));
                    } else {
                        String errorMsg = "Login failed: " + response.code();
                        if (response.body() != null) {
                            errorMsg += " - " + response.body().string();
                        }
                        EventBus.getInstance().publish(new LoginFailedEvent(errorMsg));
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    public void logout() {
        UserService.logout();
        System.out.println("Logged out");
        EventBus.getInstance().publish(new LogoutEvent());
    }

    public boolean isLoggedIn() {
        return userService.isLoggedIn();
    }
}
