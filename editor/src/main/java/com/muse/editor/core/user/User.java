package com.muse.editor.core.user;

import com.muse.editor.core.project.Project;
import javafx.beans.property.*;

@Deprecated(forRemoval=true)
class UserDeprecated {
    private final LongProperty idProperty = new SimpleLongProperty();
    private final StringProperty usernameProperty = new SimpleStringProperty();
    private final StringProperty emailProperty = new SimpleStringProperty();
    private final StringProperty roleProperty = new SimpleStringProperty();

    private final BooleanProperty isLoggedProperty = new SimpleBooleanProperty(false);

    private UserDeprecated() {}

    public long getId() {
        return idProperty.get();
    }

    public LongProperty getIdProperty() {
        return idProperty;
    }

    public String getUsername() {
        return usernameProperty.get();
    }

    public StringProperty getUsernameProperty() {
        return usernameProperty;
    }

    public String getEmail() {
        return emailProperty.get();
    }

    public StringProperty getEmailProperty() {
        return emailProperty;
    }

    public String getRole() {
        return roleProperty.get();
    }

    public StringProperty getRoleProperty() {
        return roleProperty;
    }

    public BooleanProperty getIsLoggedProperty() {
        return isLoggedProperty;
    }
}

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
        return new Builder(this).setUsername(username).build();
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

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder declareRole(String role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
