package com.muse.editor.core.edit;

public class CursorModel {
    private int measureIndex = 0;
    private int staffIndex = 0;
    private int voice = 1;

    public int getMeasureIndex() { return measureIndex; }
    public int getStaffIndex()   { return staffIndex; }
    public int getVoice()        { return voice; }

    public void setMeasureIndex(int i) { this.measureIndex = i; }
    public void setStaffIndex(int s)   { this.staffIndex = s; }
    public void setVoice(int v)        { this.voice = v; }

    public void nextMeasure() { measureIndex++; }
}
