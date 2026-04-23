package com.muse.editor.ui.component;

import com.muse.editor.model.dto.internal.Friend;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.ButtonFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;

public class FriendCard extends HBox implements Presentable {
    private final Friend friend;

    private Button inviteToProjectBtn;
    private Button viewProfileBtn;
    private Button unfriendBtn;
    private HBox   profileButtonsContainer;

    private Label  profileName;
    private Circle profilePicture;


    public FriendCard(Friend friend) {
        this.friend = friend;

        present();
    }

    @Override
    public void initComponents() {
        profileName = new Label(friend.getUsername());
        profilePicture = new Circle();
        profileButtonsContainer = new HBox();

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

        profileButtonsContainer.getStyleClass().add("profile-buttons-container");
    }

    @Override
    public void setupLayout() {
        profileButtonsContainer.getChildren().addAll(
                inviteToProjectBtn,
                viewProfileBtn,
                unfriendBtn
        );

        this.getChildren().addAll(
          profilePicture, profileName, profileButtonsContainer
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

    public Friend getFriend() {
        return friend;
    }
}
