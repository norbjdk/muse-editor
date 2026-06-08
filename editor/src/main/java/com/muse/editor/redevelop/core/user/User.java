package com.muse.editor.redevelop.core.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = User.Builder.class)
public class User {
    private final long id;
    private final String username;
    private final String email;
    private final String role;

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.role = builder.role;
    }

    public User changeUsername(String username) {
        return new User.Builder(this).setUsername(username).build();
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public static class Builder {
        private long id;
        private String username;
        private String email;
        private String role;

        public Builder(User existing) {
            this.id = existing.id;
            this.username = existing.username;
            this.email = existing.email;
            this.role = existing.role;
        }

        public Builder() {}

        @JsonProperty("id")
        public User.Builder setId(long id) {
            this.id = id;
            return this;
        }

        @JsonProperty("username")
        public User.Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        @JsonProperty("email")
        public User.Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        @JsonProperty("role")
        public User.Builder declareRole(String role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
