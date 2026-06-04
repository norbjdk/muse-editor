package com.muse.editor.redevelop.gui.model;

import com.muse.editor.redevelop.core.model.music.Note;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class Measurable <C extends Canvas> {
    protected final C root;
    protected GraphicsContext graphicsContext;

    public Measurable(C root) {
        this.root = root;
        this.graphicsContext = root.getGraphicsContext2D();

        setupEventListeners();
        setupEventHandlers();
    }

    protected abstract void draw();
    protected abstract void highlight();
    protected abstract void setupEventListeners();
    protected abstract void setupEventHandlers();

    public C getRoot() {
        return root;
    }
}
