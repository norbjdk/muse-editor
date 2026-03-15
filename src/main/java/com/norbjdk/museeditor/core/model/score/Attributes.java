package com.norbjdk.museeditor.core.model.score;

public class Attributes {
    private final int divisions;
    // Key
    private final int fifths;
    // Time
    private final int beats;
    private final int beatType;
    private final int staves;
    // Clef
    private final char sign;
    private final int line;

    private Attributes(Builder builder) {
        this.divisions = builder.divisions;
        this.fifths = builder.fifths;
        this.beats = builder.beats;
        this.beatType = builder.beatType;
        this.staves = builder.staves;
        this.sign = builder.sign;
        this.line = builder.line;
    }

    public static class Builder {
        private int divisions;
        // Key
        private int fifths;
        // Time
        private int beats;
        private int beatType;
        private int staves;
        // Clef
        private char sign;
        private int line;

        public Builder(Attributes existing) {
            this.divisions = existing.divisions;
            this.fifths = existing.fifths;
            this.beats = existing.beats;
            this.beatType = existing.beatType;
            this.staves = existing.staves;
            this.sign = existing.sign;
            this.line = existing.line;
        }
        public Builder() {}

        public Builder setDivisions(int divisions) {
            this.divisions = divisions;
            return this;
        }

        public Builder setFifths(int fifths) {
            this.fifths = fifths;
            return this;
        }

        public Builder whatTime(int beats, int beatType) {
            this.beats = beats;
            this.beatType = beatType;
            return this;
        }

        public Builder setStaves(int staves) {
            this.staves = staves;
            return this;
        }

        public Builder whatClef(char sign, int line) {
            this.sign = sign;
            this.line = line;
            return this;
        }

        public Attributes build() {
            return new Attributes(this);
        }
    }
}
