package com.muse.editor.redevelop.core.user;

import com.muse.editor.redevelop.core.api.ApiBuilder;
import com.muse.editor.redevelop.core.model.dto.UserResponse;

import java.io.IOException;

public class UserService {
    private static final UserService instance = new UserService();

    public static UserService getInstance() {
        return instance;
    }

    private UserService() {}

    private User getCurrentUser() {
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
