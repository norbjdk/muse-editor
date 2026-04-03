package com.muse.editor.core.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.ui.view.LoginDialog;
import javafx.application.Platform;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;

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

    public User getCurrentUser() {
        if (currentUser == null) {
            String userJson = TokenStorage.getUser();
            if (userJson != null && !userJson.isEmpty()) {
                try {
                    currentUser = mapper.readValue(userJson, User.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return currentUser;
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

    public boolean isLoggedIn() {
        return TokenStorage.isLoggedIn() && getCurrentUser() != null;
    }
}
