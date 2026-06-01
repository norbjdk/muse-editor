package com.muse.editor.develop.core.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muse.editor.develop.app.config.ApiConfig;
import com.muse.editor.develop.model.dto.external.FriendResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendsApiService {
    private static final FriendsApiService instance = new FriendsApiService();

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public static FriendsApiService getInstance() {
        return instance;
    }

    private static final List<FriendResponse> friendList = new ArrayList<>();

    private FriendsApiService() {
        client = ApiConfig.getHttpClient();
        mapper = ApiConfig.getObjectMapper();
    }

    public List<FriendResponse> getFriendList() throws IOException {
        final String url = ApiConfig.getBaseUrl() + "/api/v1/friendships/friendlist";

        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.isSuccessful() && response.body() != null) {

                String responseBody = response.body().string();

                return mapper.readValue(
                        responseBody,
                        new TypeReference<List<FriendResponse>>() {}
                );

            } else {
                throw new IOException(
                        "Failed to fetch friend list: " + response.code()
                );
            }
        }
    }
}
