package com.muse.editor.redevelop.core.model.dto;

import com.muse.editor.redevelop.core.api.ResponseDTO;

public class UserResponse implements ResponseDTO {
    private Long id;
    private String username;
    private String email;

    public UserResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
