package com.muse.editor.core.model.dto;

import com.muse.editor.core.api.ResponseDTO;

public class LoginResponse implements ResponseDTO {
    private Long id;
    private String token;
    private String username;
    private String email;

    public LoginResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
