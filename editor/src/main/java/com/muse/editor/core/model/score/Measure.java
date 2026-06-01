package com.muse.editor.core.model.score;

import java.util.ArrayList;
import java.util.List;

public class Measure implements Comparable<Measure> {
    private int id;
    private static int counter = 0;
    private Attributes attributes;
    private final List<Note> notes = new ArrayList<>();

    public Measure() {
        this.id = ++counter;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public Attributes getAttributes() { return attributes; }
    public void setAttributes(Attributes attributes) { this.attributes = attributes; }
    public List<Note> getNotes() { return notes; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public double usedBeats() {
        if (attributes == null) return 0;
        int beatType = attributes.getBeatType();
        return notes.stream()
                .mapToDouble(n -> noteTypeToBeats(n.getType(), beatType) * (isDotted(n) ? 1.5 : 1.0))
                .sum();
    }

    public double remainingBeats() {
        if (attributes == null) return Double.MAX_VALUE;
        return attributes.getBeats() - usedBeats();
    }

    public boolean isFull() {
        return remainingBeats() <= 0.0001;
    }

    public boolean canFit(String noteType) {
        if (attributes == null) return true;
        double needed = noteTypeToBeats(noteType, attributes.getBeatType());
        return remainingBeats() >= needed - 0.0001;
    }

    private boolean isDotted(Note note) {
        return false;
    }

    private double noteTypeToBeats(String type, int beatType) {
        double baseBeats = switch (type != null ? type : "quarter") {
            case "whole"   -> 4.0;
            case "half"    -> 2.0;
            case "quarter" -> 1.0;
            case "eighth"  -> 0.5;
            case "16th"    -> 0.25;
            case "32nd"    -> 0.125;
            case "64th"    -> 0.0625;
            default        -> 1.0;
        };
        return baseBeats * (4.0 / beatType);
    }

    @Override
    public int compareTo(Measure other) {
        return Integer.compare(this.id, other.id);
    }
}