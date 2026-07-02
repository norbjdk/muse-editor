package com.muse.editor.core.model.music;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class Attributes {
    private final IntegerProperty divisions  = new SimpleIntegerProperty();
    private final IntegerProperty fifths     = new SimpleIntegerProperty();
    private final IntegerProperty beats      = new SimpleIntegerProperty();
    private final IntegerProperty beatType   = new SimpleIntegerProperty();
    private final IntegerProperty staves     = new SimpleIntegerProperty();
    private final ObservableList<Clef> clefs = FXCollections.observableArrayList();

    private Attributes(Builder builder) {
        setDivisions(builder.divisions);
        setFifths(builder.fifths);
        setBeats(builder.beats);
        setBeatType(builder.beatType);
        setStaves(builder.staves);
        if (builder.clefs != null) {
            this.clefs.setAll(builder.clefs);
        }
    }

    public int getDivisions() { return divisions.get(); }
    public IntegerProperty divisionsProperty() { return divisions; }
    public void setDivisions(int divisions) { this.divisions.set(divisions); }

    public int getFifths() { return fifths.get(); }
    public IntegerProperty fifthsProperty() { return fifths; }
    public void setFifths(int fifths) { this.fifths.set(fifths); }

    public int getBeats() { return beats.get(); }
    public IntegerProperty beatsProperty() { return beats; }
    public void setBeats(int beats) { this.beats.set(beats); }

    public int getBeatType() { return beatType.get(); }
    public IntegerProperty beatTypeProperty() { return beatType; }
    public void setBeatType(int beatType) { this.beatType.set(beatType); }

    public int getStaves() { return staves.get(); }
    public IntegerProperty stavesProperty() { return staves; }
    public void setStaves(int staves) { this.staves.set(staves); }

    public ObservableList<Clef> getClefs() { return clefs; }

    public static class Builder {
        private int divisions;
        private int fifths;
        private int beats;
        private int beatType;
        private int staves;
        private List<Clef> clefs = new ArrayList<>();

        public Builder(Attributes existing) {
            this.divisions = existing.getDivisions();
            this.fifths = existing.getFifths();
            this.beats = existing.getBeats();
            this.beatType = existing.getBeatType();
            this.staves = existing.getStaves();
            this.clefs = new ArrayList<>(existing.getClefs());
        }

        public Builder() {}

        public Builder setDivisions(int divisions) { this.divisions = divisions; return this; }
        public Builder setFifths(int fifths) { this.fifths = fifths; return this; }
        public Builder setBeats(int beats) { this.beats = beats; return this; }
        public Builder setBeatType(int beatType) { this.beatType = beatType; return this; }
        public Builder setStaves(int staves) { this.staves = staves; return this; }
        public Builder setClefs(List<Clef> clefs) { this.clefs = clefs; return this; }
        public Builder addClef(Clef clef) {
            this.clefs.add(clef);

            return this;
        }

        public Attributes build() { return new Attributes(this); }
    }
}
