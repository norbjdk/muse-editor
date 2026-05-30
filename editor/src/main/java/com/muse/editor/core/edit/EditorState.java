package com.muse.editor.core.edit;

public class EditorState {
    private final EditorState instance = new EditorState();

    public EditorState getInstance() {
        return instance;
    }

    private String  selectedNoteType = "quarter";
    private int     selectedAlter    = 0;
    private boolean dotted           = false;
    private boolean restMode         = false;

    private EditorState() {}

    public String getSelectedNoteType() {
        return selectedNoteType;
    }

    public void setSelectedNoteType(String selectedNoteType) {
        this.selectedNoteType = selectedNoteType;
    }

    public int getSelectedAlter() {
        return selectedAlter;
    }

    public void setSelectedAlter(int selectedAlter) {
        this.selectedAlter = selectedAlter;
    }

    public boolean isDotted() {
        return dotted;
    }

    public void setDotted(boolean dotted) {
        this.dotted = dotted;
    }

    public boolean isRestMode() {
        return restMode;
    }

    public void setRestMode(boolean restMode) {
        this.restMode = restMode;
    }
}
