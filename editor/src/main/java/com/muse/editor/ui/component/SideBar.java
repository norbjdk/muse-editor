package com.muse.editor.ui.component;

import com.muse.editor.model.dto.internal.Friend;
import com.muse.editor.ui.model.Presentable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Objects;

public class SideBar extends VBox implements Presentable {

    private ProfileBar profileBar;
    private InviteBar inviteBar;
    private ScrollPane friendsScrollContainer;
    private VBox friendsContainer;

    public SideBar() {
        present();
    }

    @Override
    public void initComponents() {
        profileBar = new ProfileBar();
        inviteBar = new InviteBar();
        friendsScrollContainer = new ScrollPane();
        friendsContainer = new VBox();
    }

    @Override
    public void setupComponents() {
        friendsScrollContainer.setContent(friendsContainer);
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("side-bar");

        friendsScrollContainer.getStyleClass().add("friends-scroll");
        friendsContainer.getStyleClass().add("friends-container");
    }

    @Override
    public void setupLayout() {
        addFixedFriends();

        getChildren().addAll(
                profileBar,
                inviteBar,
                friendsContainer
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }

    private void addFixedFriends() {
        final Friend friend1 = new Friend();
        friend1.setUsername("Musician3040");
        friend1.setPicturePath("https://static.vecteezy.com/system/resources/previews/005/520/556/non_2x/cartoon-drawing-of-a-musician-vector.jpg");

        final Friend friend2 = new Friend();
        friend2.setUsername("GuitarLover20");
        friend2.setPicturePath("https://static.vecteezy.com/system/resources/previews/005/520/506/non_2x/cartoon-drawing-of-a-musician-vector.jpg");

        final Friend friend3 = new Friend();
        friend3.setUsername("PianoEnjoyer99");
        friend3.setPicturePath("https://static.vecteezy.com/system/resources/previews/049/165/981/non_2x/pianist-playing-the-piano-element-illustration-in-cartoon-style-free-vector.jpg");

        friendsContainer.getChildren().addAll(
                new FriendCard(friend1),
                new FriendCard(friend2),
                new FriendCard(friend3)
        );
    }
}
