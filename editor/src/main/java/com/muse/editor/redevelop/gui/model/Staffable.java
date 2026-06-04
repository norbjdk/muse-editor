package com.muse.editor.redevelop.gui.model;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public abstract class Staffable<S extends Node> {
    protected final S root;

    protected final int    octave;
    protected final char   step;
    protected double y;

    public Staffable(S root, int octave, char step) {
        this.root   = root;
        this.octave = octave;
        this.step   = step;

        initComponents();
        setupComponents();
        setupLayout();
        setupEventListeners();
        setupEventHandlers();
    }

    protected abstract void initComponents();
    protected abstract void setupComponents();
    protected abstract void setupLayout();
    protected abstract void setupEventListeners();
    protected abstract void setupEventHandlers();
    public abstract void setPosition(double y);
    public abstract void bindWidth(DoubleProperty width);

    public S getRoot() {
        return root;
    }

    public double getY() {
        return y;
    }

    public int getOctave() {
        return octave;
    }

    public char getStep() {
        return step;
    }
}
