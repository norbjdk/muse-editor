package com.muse.editor.redevelop.gui.model;

import javafx.scene.Node;

public interface Viewable {
    enum Name {
        HOME,
        CREATE_PROJECT,
        PROJECT
    }

    Node getRoot();
}
