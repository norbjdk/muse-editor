package com.muse.editor.core.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.ui.view.LoginDialog;
import javafx.application.Platform;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class UserService {
    private final static UserService instance = new UserService();

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private static User currentUser;

    private Stage primaryStage;

    public static UserService getInstance() {
        return instance;
    }

    private UserService() {
        client = ApiConfig.getHttpClient();
        mapper = ApiConfig.getObjectMapper();

        setupEventListeners();
    }

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void setupEventListeners() {}

    private void handleSetupUserRequested() {
        Platform.runLater(() -> {

        });
    }

    public User getCurrentUser() throws IOException {
        final String url = ApiConfig.getBaseUrl() + "/api/v1/users/me";

        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return mapper.readValue(responseBody, User.class);
            } else {
                throw new IOException("Failed to fetch current user: " + response.code());
            }
        }
    }

    public User getUserByUsername(String username) throws IOException {
        final String url = ApiConfig.getBaseUrl() + "/api/v1/users/" + username;

        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return mapper.readValue(responseBody, User.class);
            } else {
                throw new IOException("Failed to fetch current user: " + response.code());
            }
        }
    }

    public void setUser(User user) {
        currentUser = user;
        try {
            String userJson = mapper.writeValueAsString(user);
            TokenStorage.saveUser(userJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public static void logout() {
        currentUser = null;
        TokenStorage.clear();
    }
}
