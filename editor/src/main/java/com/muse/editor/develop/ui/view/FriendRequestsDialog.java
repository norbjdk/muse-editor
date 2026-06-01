package com.muse.editor.develop.ui.view;

import com.muse.editor.develop.core.api.FriendsApiService;
import com.muse.editor.develop.ui.component.RequestCard;
import com.muse.editor.develop.ui.model.Presentable;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsDialog extends Dialog<Boolean> implements Presentable {

    private VBox root;

    private List<RequestCard> requestCards = new ArrayList<>();

    private FriendsApiService friendsApiService;

    public FriendRequestsDialog() {
        present();
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setupComponents() {

    }

    @Override
    public void setupStyle() {

    }

    @Override
    public void setupLayout() {

    }

    @Override
    public void setupEventListeners() {

    }

    @Override
    public void setupEventHandlers() {

    }
}
