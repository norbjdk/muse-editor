package com.muse.editor.core.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.core.model.dto.LoginRequest;
import com.muse.editor.core.model.dto.LoginResponse;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.core.user.UserService;
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
    }

    private void setupEventListeners() {

    }

    private void handleLoginUserRequested() {
        Platform.runLater(() -> {

        });
    }

    private void login(LoginRequest loginRequest) {
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
                                response.body().toString(),
                                LoginResponse.class
                        );

                        TokenStorage.saveToken(loginResponse.getToken());
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }
}
