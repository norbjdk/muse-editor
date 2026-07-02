package com.muse.editor.gui.model;

import javafx.scene.Node;

public interface Viewable {
    enum Name {
        HOME,
        CREATE_PROJECT,
        PROJECT,
        COLLECTION
    }

    Node getRoot();
}
