package com.muse.editor.ui.component;

import com.muse.editor.model.dto.external.FriendResponse;
import com.muse.editor.ui.model.Presentable;
import com.muse.editor.ui.util.ButtonFactory;
import com.muse.editor.ui.util.SpaceFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;

import static com.muse.editor.ui.util.SpaceFactory.createSpacer;

public class RequestCard extends HBox implements Presentable {
    private final FriendResponse friendResponse;

    private Button acceptBtn;
    private Button rejectBtn;

    private HBox decisionButtonsContainer;

    private Label usernameLabel;

    public RequestCard(FriendResponse friendResponse) {
        this.friendResponse = friendResponse;

        present();
    }

    @Override
    public void initComponents() {
        decisionButtonsContainer = new HBox();

        usernameLabel = new Label();

        acceptBtn = ButtonFactory.createButton("", "accept-btn", "Accept friend", "request-card-btn");
        rejectBtn = ButtonFactory.createButton("", "reject-btn", "Reject friend", "request-card-btn");
    }

    @Override
    public void setupComponents() {
        usernameLabel.setText(friendResponse.getUsername());

        ButtonFactory.addIcon(acceptBtn, FontAwesomeSolid.USER_PLUS, 15, Color.rgb(15, 15, 15));
        ButtonFactory.addIcon(rejectBtn, FontAwesomeSolid.USER_MINUS, 15, Color.rgb(15, 15, 15));
    }

    @Override
    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/muse/editor/styles/components.css")).toExternalForm());
        this.getStyleClass().add("request-card");

        usernameLabel.getStyleClass().add("request-username-label");

        decisionButtonsContainer.getStyleClass().add("decision-buttons-container");
    }

    @Override
    public void setupLayout() {
        decisionButtonsContainer.getChildren().addAll(
                acceptBtn,
                rejectBtn
        );

        this.getChildren().addAll(
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                usernameLabel,
                createSpacer(SpaceFactory.Direction.HORIZONTAL),
                decisionButtonsContainer
        );
    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
