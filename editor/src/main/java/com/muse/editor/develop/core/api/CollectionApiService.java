package com.muse.editor.develop.core.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.develop.app.config.ApiConfig;
import okhttp3.OkHttpClient;

public class CollectionApiService {
    private static final CollectionApiService instance = new CollectionApiService();

    public static CollectionApiService getInstance() {
        return instance;
    }

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    private CollectionApiService() {
        client = ApiConfig.getHttpClient();
        mapper = ApiConfig.getObjectMapper();
    }
}
