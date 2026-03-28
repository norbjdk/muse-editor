package com.muse.editor.core.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.ui.view.LoginDialog;
import javafx.application.Platform;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;

public class UserService {
    private final static UserService instance = new UserService();

    private final OkHttpClient client;
    private final ObjectMapper mapper;

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

    private boolean showLoginDialog() {
        final LoginDialog loginDialog = new LoginDialog();

        loginDialog.showAndWait();

        return false;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public OkHttpClient getClient() {
        return client;
    }
}
