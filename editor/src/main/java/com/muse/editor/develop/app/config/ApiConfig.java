package com.muse.editor.develop.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.develop.core.user.TokenStorage;
import okhttp3.OkHttpClient;

public class ApiConfig {
    private static final String BASE_URL = "http://localhost:8080";
    private static OkHttpClient httpClient;
    private static ObjectMapper objectMapper;

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        var originalRequest = chain.request();
                        var token = TokenStorage.getToken();

                        if (token != null && !token.isEmpty()) {
                            var requestBuilder = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + token);
                            return chain.proceed(requestBuilder.build());
                        }
                        return chain.proceed(originalRequest);
                    })
                    .build();
        }
        return httpClient;
    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
}
