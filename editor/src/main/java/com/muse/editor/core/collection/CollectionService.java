package com.muse.editor.core.collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.muse.editor.core.api.ApiConfig;
import com.muse.editor.core.model.dto.ProjectResponse;
import com.muse.editor.core.user.TokenStorage;
import com.muse.editor.event.EventBus;
import com.muse.editor.event.view.ChangeViewEvent;
import com.muse.editor.gui.model.Viewable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class CollectionService {
    private static final CollectionService instance = new CollectionService();

    private final ObservableList<ProjectResponse> projectResponses = FXCollections.observableArrayList();

    public static CollectionService getInstance() {
        return instance;
    }

    private CollectionService() {
        EventBus.getInstance().subscribe(ChangeViewEvent.class, changeViewEvent -> {
            if (changeViewEvent.getViewName().equals(Viewable.Name.COLLECTION))
                fetchScores();
        });
    }

    private void fetchScores() {
        final String url = ApiConfig.getURL() + "/api/v1/projects/my";

        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + TokenStorage.getToken())
                .get()
                .build();

        try (Response response = ApiConfig.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                List<ProjectResponse> responses = ApiConfig.getObjectMapper()
                        .readValue(responseBody, new TypeReference<List<ProjectResponse>>() {});

                projectResponses.setAll(responses);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<ProjectResponse> getProjectResponses() {
        return projectResponses;
    }
}
