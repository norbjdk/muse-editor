package com.muse.editor.develop.core.model.score;

/**
 * isRest is critical if note is just a rest </br>
 * step is sound letter, all possibles are: C, D, E, F, G, A, B </br>
 * octave is scientific octave, e.g. 4 is middle c </br>
 * duration is time in division units. E.g. if divisions is equal to 4, then duration equal 4 is quarter-note </br>
 * type is optional but might be useful in UI </br>
 * duration type: </br>
 * <duration>16</duration> <type>whole</type>,
 * <duration>8</duration> <type>half</type>,
 * <duration>4</duration> <type>quarter</type>,
 * <duration>2</duration> <type>eighth</type>,
 * <duration>1</duration> <type>16th</type> </br>
 * alter is for declaring if it has sharp, flat or natural, 1 for sharp, -1 for flat and 0 for natural </br>
 * stem is for declaring stem direction whether is up or down, depends on note placement
 */

public class Note {
    private final int id;
    private final boolean isRest;
    private final char step;
    private final int alter;
    private final int octave;
    private final int duration;
    private final int voice;
    private final String type;
    private final String stem;
    private final int staff;

    private Note(Builder builder) {
        this.id = builder.id;
        this.isRest = builder.isRest;
        this.step = builder.step;
        this.alter = builder.alter;
        this.octave = builder.octave;
        this.duration = builder.duration;
        this.voice = builder.voice;
        this.type = builder.type;
        this.stem = builder.stem;
        this.staff = builder.staff;
    }

    public Note withStem(String newStem) {
        return new Builder(this).stemDirection(newStem).build();
    }

    public int getId() {
        return id;
    }
    public char getStep() {
        return step;
    }
    public int getOctave() {
        return octave;
    }
    public int getDuration() {
        return duration;
    }
    public int getAlter() {
        return alter;
    }
    public boolean isRest() {
        return isRest;
    }
    public String getType() {
        return type;
    }
    public String getStem() {
        return stem;
    }
    public int getVoice() {
        return voice;
    }
    public int getStaff() {
        return staff;
    }

    public static class Builder {
        private int id;
        private boolean isRest;
        private char step;
        private int alter;
        private int octave;
        private int duration;
        private int voice;
        private String type;
        private String stem;
        private int staff;

        public Builder(Note existing) {
            this.id = existing.id;
            this.isRest = existing.isRest;
            this.step = existing.step;
            this.alter = existing.alter;
            this.octave = existing.octave;
            this.duration = existing.duration;
            this.voice = existing.voice;
            this.type = existing.type;
            this.stem = existing.stem;
            this.staff = existing.staff;
        }

        public Builder() {}

        public Builder setId(int id) {
            this.id = id;

            return this;
        }

        public Builder isRest(boolean isRest) {
            this.isRest = isRest;
            return this;
        }

        public Builder whichStep(char step) {
            this.step = step;
            return this;
        }

        public Builder setAlter(int alter) {
            this.alter = alter;
            return this;
        }

        public Builder whichOctave(int octave) {
            this.octave = octave;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder whichVoice(int voice) {
            this.voice = voice;
            return this;
        }

        public Builder whatType(String type) {
            this.type = type;
            return this;
        }

        public Builder stemDirection(String stem) {
            this.stem = stem;
            return this;
        }

        public Builder whichStaff(int staff) {
            this.staff = staff;
            return this;
        }

        public Note build() {
            return new Note(this);
        }
    }
}
