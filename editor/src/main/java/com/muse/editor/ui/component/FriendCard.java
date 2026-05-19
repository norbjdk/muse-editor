package com.muse.editor.ui.component;

import com.muse.editor.model.dto.internal.Friend;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.ButtonFactory;
import com.muse.editor.ui.util.SpaceFactory;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;

import static com.muse.editor.ui.util.SpaceFactory.createSpacer;

public class FriendCard extends HBox implements Presentable {
    private final Friend friend;

    private Button inviteToProjectBtn;
    private Button viewProfileBtn;
    private Button unfriendBtn;

    private Label  profileName;
    private Label  friendStatus;
    private Circle profilePicture;

    private HBox profileButtonsContainer;
    private VBox nameAndStatusContainer;

    public FriendCard(Friend friend) {
        this.friend = friend;

        present();
        updateStatusLabel();
    }

    @Override
    public void initComponents() {
        profileName = new Label(friend.getUsername());
        friendStatus = new Label(friend.getStatus());
        profilePicture = new Circle();

        profileButtonsContainer = new HBox();
        nameAndStatusContainer = new VBox();

        inviteToProjectBtn = ButtonFactory.createButton("", "inv-proj-btn", "Invite to Project", "friend-card-btn");
        viewProfileBtn = ButtonFactory.createButton("", "profile-btn", "View Profile", "friend-card-btn");
        unfriendBtn = ButtonFactory.createButton("", "unfriend-btn", "Unfriend", "friend-card-btn");
    }

    @Override
    public void setupComponents() {
        profilePicture.setRadius(25);

        ButtonFactory.addIcon(inviteToProjectBtn, FontAwesomeSolid.ENVELOPE, 15, Color.rgb(15, 15, 15));
        ButtonFactory.addIcon(viewProfileBtn, FontAwesomeSolid.LINK, 15, Color.rgb(15, 15, 15));
        ButtonFactory.addIcon(unfriendBtn, FontAwesomeSolid.USER_SLASH, 15, Color.rgb(15, 15, 15));
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("friend-card");

        profileName.getStyleClass().add("friend-name-label");
        friendStatus.getStyleClass().add("friend-status-label");

        profileButtonsContainer.getStyleClass().add("profile-buttons-container");
        nameAndStatusContainer.getStyleClass().add("name-status-container");
    }

    @Override
    public void setupLayout() {
        profileButtonsContainer.getChildren().addAll(
                inviteToProjectBtn,
                viewProfileBtn,
                unfriendBtn
        );

        nameAndStatusContainer.getChildren().addAll(
                profileName,
                createSpacer(SpaceFactory.Direction.VERTICAL),
                friendStatus
        );

        this.getChildren().addAll(
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                profilePicture,
                nameAndStatusContainer,
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                profileButtonsContainer,
                createSpacer(SpaceFactory.Direction.HORIZONTAL)
        );
    }

    @Override
    public void setupEventListeners() {
        Image remoteImage = new Image(friend.getPicturePath(), true);
        remoteImage.progressProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == 1.0) {
                profilePicture.setFill(new ImagePattern(remoteImage));
            }
        });

        remoteImage.errorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                profilePicture.setFill(Color.GRAY);
            }
        });
    }

    @Override
    public void setupEventHandlers() {

    }

    private void updateStatusLabel() {
        Platform.runLater(() -> {
            if (friend.getStatus().equals("offline")) {
                friendStatus.setStyle("-fx-text-fill: #9e9e9e");
            } else if (friend.getStatus().equals("online")) {
                friendStatus.setStyle("-fx-text-fill: #45a049");
            }
        });
    }

    public Friend getFriend() {
        return friend;
    }
}
