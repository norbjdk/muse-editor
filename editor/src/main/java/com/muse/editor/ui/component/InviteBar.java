package com.muse.editor.ui.component;

import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.ButtonFactory;
import com.muse.editor.ui.util.SpaceFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;

import static com.muse.editor.ui.util.SpaceFactory.createSpacer;

public class InviteBar extends HBox implements Presentable {

    private TextField friendNameInput;
    private Button addFriendBtn;
    private Button notificationsBtn;

    public InviteBar() {
        present();
    }

    @Override
    public void initComponents() {
        friendNameInput = new TextField();
        addFriendBtn = ButtonFactory.createButton("", "add-friend-btn", "Add new friend", "invite-bar-btn");
        notificationsBtn = ButtonFactory.createButton("", "notifications-btn", "See your notifications", "invite-bar-btn");
    }

    @Override
    public void setupComponents() {
        ButtonFactory.addIcon(addFriendBtn, FontAwesomeSolid.USER_PLUS, 15, Color.rgb(5, 5, 5));
        ButtonFactory.addIcon(notificationsBtn, FontAwesomeSolid.BELL, 15, Color.rgb(5,5 ,5));

        friendNameInput.setPromptText("Type friend name");
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("invite-bar");

        friendNameInput.getStyleClass().add("friend-name-input");
    }

    @Override
    public void setupLayout() {
        this.getChildren().addAll(
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                friendNameInput,
                addFriendBtn,
                notificationsBtn,
                createSpacer(SpaceFactory.Direction.HORIZONTAL)
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
