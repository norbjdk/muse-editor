package com.muse.editor.core.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.app.config.ApiConfig;
import com.muse.editor.core.user.TokenStorage;
import okhttp3.*;

import java.io.File;
import java.util.Map;

public class StorageApiService {
    private static final StorageApiService instance = new StorageApiService();

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public static StorageApiService getInstance() {
        return instance;
    }

    private StorageApiService() {
        client = ApiConfig.getHttpClient();
        mapper = ApiConfig.getObjectMapper();
    }

    public String uploadProjectFile(Long projectId, File file) {
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/xml"));

        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(ApiConfig.getBaseUrl() + "/api/v1/storage/projects/" + projectId + "/file")
                .post(body)
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Upload failed: " + response.code());
            }
            Map<?, ?> result = mapper.readValue(response.body().string(), Map.class);
            return (String) result.get("url");
        } catch (Exception e) {
            throw new RuntimeException("uploadProjectFile failed", e);
        }
    }

    public String getProjectFileUrl(Long projectId) {
        Request request = new Request.Builder()
                .url(ApiConfig.getBaseUrl() + "/api/v1/storage/projects/" + projectId + "/file")
                .get()
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to get file url: " + response.code());
            }
            Map<?, ?> result = mapper.readValue(response.body().string(), Map.class);
            return (String) result.get("url");
        } catch (Exception e) {
            throw new RuntimeException("getProjectFileUrl failed", e);
        }
    }

    public String uploadProjectCover(Long projectId, File file) {
        String ext = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        String mediaType = switch (ext.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };

        RequestBody fileBody = RequestBody.create(file, MediaType.parse(mediaType));

        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(ApiConfig.getBaseUrl() + "/api/v1/storage/projects/" + projectId + "/cover")
                .post(body)
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Cover upload failed: " + response.code());
            }
            Map<?, ?> result = mapper.readValue(response.body().string(), Map.class);
            return (String) result.get("url");
        } catch (Exception e) {
            throw new RuntimeException("uploadProjectCover failed", e);
        }
    }
}