package com.muse.editor.redevelop.core.model.music;

public class Note {
    private static int counter = 0;

    private final int id;

    public enum Type {
        Whole("whole"),
        Half("half"),
        Quarter("quarter"),
        Eighth("eighth"),
        Semiquaver("16th");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private final boolean isChord;
    private final boolean isRest;
    private final char    step;
    private final int     alter;
    private final int     octave;
    private final int     duration;
    private final int     voice;
    private final Type    type;
    private final String  stem;
    private final int     staff;

    private Note(Builder builder) {
        counter += 1;

        this.id = counter;

        this.isRest   = builder.isRest;
        this.isChord  = builder.isChord;
        this.step     = builder.step;
        this.alter    = builder.alter;
        this.octave   = builder.octave;
        this.duration = builder.duration;
        this.voice    = builder.voice;
        this.type     = builder.type;
        this.stem     = builder.stem;
        this.staff    = builder.staff;
    }

    public static int getCounter() {
        return counter;
    }

    public int getId() {
        return id;
    }

    public boolean isChord() {
        return isChord;
    }

    public boolean isRest() {
        return isRest;
    }

    public char getStep() {
        return step;
    }

    public int getAlter() {
        return alter;
    }

    public int getOctave() {
        return octave;
    }

    public int getDuration() {
        return duration;
    }

    public int getVoice() {
        return voice;
    }

    public Type getType() {
        return type;
    }

    public String getStem() {
        return stem;
    }

    public int getStaff() {
        return staff;
    }

    public static class Builder {
        private boolean isRest;
        private boolean isChord;
        private char    step;
        private int     alter;
        private int     octave;
        private int     duration;
        private int     voice;
        private Type    type;
        private String  stem;
        private int     staff;

        public Builder(Note existing) {
            this.isRest   = existing.isRest;
            this.isChord  = existing.isChord;
            this.step     = existing.step;
            this.alter    = existing.alter;
            this.octave   = existing.octave;
            this.duration = existing.duration;
            this.voice    = existing.voice;
            this.type     = existing.type;
            this.stem     = existing.stem;
            this.staff    = existing.staff;
        }

        public Builder() {}

        public Builder isRest(boolean isRest) {
            this.isRest = isRest;

            return this;
        }

        public Builder isChord(boolean isChord) {
            this.isChord = isChord;

            return this;
        }

        public Builder setStep(char step) {
            this.step = step;

            return this;
        }

        public Builder setAlter(int alter) {
            this.alter = alter;

            return this;
        }

        public Builder setOctave(int octave) {
            this.octave = octave;

            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;

            return this;
        }

        public Builder setVoice(int voice) {
            this.voice = voice;

            return this;
        }

        public Builder setType(Type type) {
            this.type = type;

            return this;
        }

        public Builder setStem(String stem) {
            this.stem = stem;

            return this;
        }

        public Builder setStaff(int staff) {
            this.staff = staff;

            return this;
        }

        public Note build() {
            return new Note(this);
        }
    }
}
