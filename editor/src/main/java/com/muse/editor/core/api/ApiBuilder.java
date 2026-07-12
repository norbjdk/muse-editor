package com.muse.editor.core.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.util.Debug;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ApiBuilder {
    private static final OkHttpClient client = ApiConfig.getClient();
    private static final ObjectMapper mapper = ApiConfig.getObjectMapper();

    private ApiBuilder() {}

    public static <R extends ResponseDTO> R get(String endpoint, Class<R> responseType) throws IOException {
        if (endpoint == null || endpoint.isEmpty()) return null;

        Request request = new Request.Builder()
                .url(buildUrl(endpoint))
                .get()
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        return executeRequest(request, responseType);
    }

    public static Map<?, ?> getAsMap(String endpoint, Class<Map> responseType) throws IOException {
        if (endpoint == null || endpoint.isEmpty()) return null;

        Request request = new Request.Builder()
                .url(buildUrl(endpoint))
                .get()
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        return executeRequestAsMap(request, responseType);
    }


    public static <R extends ResponseDTO, A extends RequestDTO> R post(String endpoint, Class<R> responseType, A req) throws IOException {
        if (endpoint == null || endpoint.isEmpty()) return null;

        String jsonBody = mapper.writeValueAsString(req);
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(buildUrl(endpoint))
                .post(body)
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        return executeRequest(request, responseType);
    }

    public static <R extends ResponseDTO> R put(String endpoint, Class<R> responseType, File file, String mediaType) throws IOException {
        final RequestBody fileBody = RequestBody.create(file, MediaType.parse(mediaType));

        final MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        final Request request = new Request.Builder()
                .url(buildUrl(endpoint))
                .put(body)
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        return executeRequest(request, responseType);
    }

    public static <R extends ResponseDTO> R post(String endpoint, Class<R> responseType, File file) throws IOException {
        final RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/xml"));

        final MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        final Request request = new Request.Builder()
                .url(buildUrl(endpoint))
                .post(body)
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .build();

        return executeRequest(request, responseType);
    }

    private static String buildUrl(String endpoint) {
        if (!endpoint.startsWith("/")) {
            endpoint = "/" + endpoint;
        }
        return ApiConfig.getURL() + endpoint;
    }

    private static <R extends ResponseDTO> R executeRequest(Request request, Class<R> responseType) throws IOException {
        Debug.check("=== API REQUEST ===");
        Debug.check("URL: " + request.url());
        Debug.check("Method: " + request.method());
        Debug.check("Headers: " + request.headers());

        String token = TokenStorage.getToken();
        Debug.check("Token exists: " + (token != null && !token.isEmpty()));
        Debug.check("Token length: " + (token != null ? token.length() : 0));

        try (Response response = client.newCall(request).execute()) {
            Debug.check("Response code: " + response.code());
            Debug.check("Response message: " + response.message());

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "null";
                Debug.fail("Error body: " + errorBody);
                throw new IOException("API request failed, code: " + response.code() + ", body: " + errorBody);
            }
            if (responseType == null || response.body() == null) {
                return null;
            }
            String responseBody = response.body().string();
            Debug.pass("Response body: " + responseBody);
            return mapper.readValue(responseBody, responseType);
        }
    }

    private static Map<?, ?> executeRequestAsMap(Request request, Class<Map> responseType) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return mapper.readValue(responseBody, responseType);
            } else {
                throw new IOException("API request failed, code: " + response.code());
            }
        }
    }
}
