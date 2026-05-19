package com.muse.editor.core.collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.model.dto.external.ProjectCardResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.List;

public class CollectionApiService {
    private static final CollectionApiService instance = new CollectionApiService();

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public static CollectionApiService getInstance() {
        return instance;
    }

    private CollectionApiService() {
        client = ApiConfig.getHttpClient();
        mapper = ApiConfig.getObjectMapper();
    }

    public List<ProjectCardResponse> getMyProjects() {
        Request request = new Request.Builder()
                .url(ApiConfig.getBaseUrl() + "/api/v1/projects/me")
                .get()
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to fetch projects: " + response.code());
            }
            String body = response.body().string();
            System.out.println("getMyProjects response: " + body);
            return mapper.readValue(body, new TypeReference<List<ProjectCardResponse>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getMyProjects failed", e);
        }
    }
}