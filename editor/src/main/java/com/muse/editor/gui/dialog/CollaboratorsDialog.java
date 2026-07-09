package com.muse.editor.gui.dialog;

import com.muse.editor.core.model.dto.UserResponse;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CollaboratorsDialog extends Dialog<Boolean> {

    private final List<HBox> collaboratorCards = new ArrayList<>();

    public CollaboratorsDialog(List<UserResponse> responses) {
        this.setTitle("Collaborators");
        this.setHeaderText("List of active collaborators working on this project");

        responses.forEach(this::addCard);
    }

    private void addCard(UserResponse response) {
        final HBox card = new HBox();

        final Label numLabel      = new Label(String.valueOf(collaboratorCards.size() + 1));
        final Label usernameLabel = new Label(response.getUsername());
        final Label emailLabel    = new Label(response.getEmail());

        this.getDialogPane().getStylesheets().addAll(
                Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components/collab-card.css")).toExternalForm()
        );

        numLabel.getStyleClass().add("num-label");
        usernameLabel.getStyleClass().add("username-label");
        emailLabel.getStyleClass().add("email-label");

        card.getChildren().addAll(
                numLabel,
                usernameLabel,
                emailLabel
        );

        collaboratorCards.add(card);
    }
}
