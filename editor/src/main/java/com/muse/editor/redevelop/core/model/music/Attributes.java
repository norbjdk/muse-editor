package com.muse.editor.redevelop.core.model.music;

import java.util.ArrayList;
import java.util.List;

public class Attributes {
    private final int        divisions;
    private final int        fifths;
    private final int        beats;
    private final int        beatType;
    private final int        staves;
    private final List<Clef> clefs = new ArrayList<>();

    private Attributes(Builder builder) {
        this.divisions = builder.divisions;
        this.fifths = builder.fifths;
        this.beats = builder.beats;
        this.beatType = builder.beatType;
        this.staves = builder.staves;
    }

    public static class Builder {
        private int        divisions;
        private int        fifths;
        private int        beats;
        private int        beatType;
        private int        staves;
        private List<Clef> clefs;

        public Builder(Attributes existing) {
            this.divisions = existing.divisions;
            this.fifths    = existing.fifths;
            this.beats     = existing.beats;
            this.beatType  = existing.beatType;
            this.staves    = existing.staves;
            this.clefs     = existing.clefs;
        }

        public Builder() {
            clefs = new ArrayList<>();
        }

        public Builder setDivisions(int divisions) {
            this.divisions = divisions;

            return this;
        }

        public Builder setFifths(int fifths) {
            this.fifths = fifths;

            return this;
        }

        public Builder setTime(int beats, int beatType) {
            this.beats    = beats;
            this.beatType = beatType;

            return this;
        }

        public Builder setStaves(int staves) {
            this.staves = staves;

            return this;
        }

        public Builder addClef(Clef clef) {
            if (clefs == null) return this;
            if (clef  == null) return this;

            clefs.add(clef);

            return this;
        }

        public Attributes build() {
            return new Attributes(this);
        }
    }
}
