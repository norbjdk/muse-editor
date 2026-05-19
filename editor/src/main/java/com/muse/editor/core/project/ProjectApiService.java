package com.muse.editor.core.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.model.dto.external.ProjectResponse;
import okhttp3.*;

import java.util.Map;

public class ProjectApiService {
    private static final ProjectApiService instance = new ProjectApiService();

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public static ProjectApiService getInstance() {
        return instance;
    }

    private ProjectApiService() {
        client = ApiConfig.getHttpClient();
        mapper = ApiConfig.getObjectMapper();
    }

    public ProjectResponse createProject(String title, String subtitle, String composer) {
        try {
            String body = mapper.writeValueAsString(Map.of(
                    "title",    title    != null ? title    : "",
                    "subtitle", subtitle != null ? subtitle : "",
                    "composer", composer != null ? composer : "",
                    "isPublic", false
            ));

            Request request = new Request.Builder()
                    .url(ApiConfig.getBaseUrl() + "/api/v1/projects")
                    .post(RequestBody.create(body, MediaType.parse("application/json")))
                    .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("createProject failed: " + response.code());
                }
                String responseBody = response.body().string();
                System.out.println("createProject response: " + responseBody);
                return mapper.readValue(responseBody, ProjectResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("createProject failed", e);
        }
    }
}