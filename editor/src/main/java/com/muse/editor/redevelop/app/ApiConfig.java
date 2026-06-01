package com.muse.editor.redevelop.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

public class ApiConfig {
    private static final String BASE_URL = "http://localhost:8080";

    private static OkHttpClient httpClient;
    private static ObjectMapper objectMapper;

    public static String getURL() {
        return BASE_URL;
    }

    public static OkHttpClient getClient() {
        return httpClient == null ? null : httpClient;
    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper  = new ObjectMapper();
        }

        return objectMapper;
    }
}
