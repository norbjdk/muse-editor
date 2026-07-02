package com.muse.editor.gui.dialog;

import com.muse.editor.core.model.dto.internal.PublishData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class PublishDialog extends Dialog<PublishData> {

    private final TextField titleField = new TextField();
    private final TextField creatorField = new TextField();
    private final ToggleButton privacySwitch = new ToggleButton("Public");
    private final ImageView previewImageView = new ImageView();

    public PublishDialog(String title, String creator) {
        this.setTitle("Publish Project");
        this.setHeaderText("Fill score credentials and review preview");

        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.getDialogPane().getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/muse/editor/styles/dialogs/publish-dialog.css")).toExternalForm()
        );
        this.getDialogPane().getStyleClass().add("publish-dialog");

        Label titleLabel = new Label("Title:");
        titleLabel.getStyleClass().add("publish-form-label");

        Label creatorLabel = new Label("Creator:");
        creatorLabel.getStyleClass().add("publish-form-label");

        Label visibilityLabel = new Label("Visibility:");
        visibilityLabel.getStyleClass().add("publish-form-label");

        titleField.setPromptText("Enter title");
        titleField.setText(title);
        titleField.getStyleClass().add("publish-form-input");

        creatorField.setPromptText("Enter creator name");
        creatorField.setText(creator);
        creatorField.getStyleClass().add("publish-form-input");

        privacySwitch.setMaxWidth(Double.MAX_VALUE);
        privacySwitch.getStyleClass().add("publish-privacy-toggle");
        privacySwitch.selectedProperty().addListener((obs, oldVal, newVal) ->
                privacySwitch.setText(newVal ? "Private" : "Public")
        );

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("publish-form-grid");
        formGrid.add(titleLabel,      0, 0);
        formGrid.add(titleField,      1, 0);
        formGrid.add(creatorLabel,    0, 1);
        formGrid.add(creatorField,    1, 1);
        formGrid.add(visibilityLabel, 0, 2);
        formGrid.add(privacySwitch,   1, 2);

        Label previewLabel = new Label("Preview:");
        previewLabel.getStyleClass().add("publish-preview-label");

        previewImageView.setFitWidth(300);
        previewImageView.setFitHeight(400);
        previewImageView.setPreserveRatio(true);
        previewImageView.setSmooth(true);

        VBox imageContainer = new VBox();
        imageContainer.getStyleClass().add("publish-preview-container");
        imageContainer.getChildren().addAll(previewLabel, previewImageView);

        HBox mainLayout = new HBox();
        mainLayout.getStyleClass().add("publish-dialog-layout");
        mainLayout.getChildren().addAll(formGrid, imageContainer);

        this.getDialogPane().setContent(mainLayout);

        this.setResultConverter(dialogButton -> {
            final PublishData publishData = new PublishData();

            publishData.setTitle(titleField.getText());
            publishData.setCreator(creatorField.getText());

            if (dialogButton == ButtonType.OK) {
                return publishData;
            }
            return null;
        });
    }

    public void setPreviewImage(WritableImage image) {
        this.previewImageView.setImage(image);
    }
}