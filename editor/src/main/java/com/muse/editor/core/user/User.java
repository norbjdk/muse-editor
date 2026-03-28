package com.muse.editor.core.user;

import com.muse.editor.core.project.Project;
import javafx.beans.property.*;

public class User {
    private final static User instance = new User();

    public static User getInstance() {
        return instance;
    }

    private final LongProperty idProperty = new SimpleLongProperty();
    private final StringProperty usernameProperty = new SimpleStringProperty();
    private final StringProperty emailProperty = new SimpleStringProperty();
    private final StringProperty roleProperty = new SimpleStringProperty();

    private final BooleanProperty isLoggedProperty = new SimpleBooleanProperty(false);

    private User() {}

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
