package com.muse.editor.core.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.muse.editor.core.api.ApiBuilder;
import com.muse.editor.core.model.dto.UserResponse;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserService {
    private static final UserService instance = new UserService();

    private UserManager userManager = UserManager.getInstance();

    public static UserService getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        try {
            UserResponse response = ApiBuilder.get("/api/v1/users/me", UserResponse.class);

            return new User.Builder()
                    .setId(response.getId())
                    .setEmail(response.getEmail())
                    .setUsername(response.getUsername())
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
