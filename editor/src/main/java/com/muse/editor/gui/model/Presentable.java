package com.muse.editor.gui.model;

import javafx.scene.Node;

public abstract class Presentable<P extends Node> {
    protected final P root;

    public Presentable(P root) {
        this.root = root;

        initComponents();
        setupComponents();
        setupStyle();
        setupLayout();
        setupEventListeners();
        setupEventHandlers();
    }

    public P getRoot() {
        return root;
    }

    protected abstract void initComponents();
    protected abstract void setupComponents();
    protected abstract void setupStyle();
    protected abstract void setupLayout();
    protected abstract void setupEventListeners();
    protected abstract void setupEventHandlers();
}
