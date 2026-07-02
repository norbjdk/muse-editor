package com.muse.editor.core.model.music;

import javafx.beans.property.*;

public class Note {
    public enum Type {
        Whole("whole"),
        Half("half"),
        Quarter("quarter"),
        Eighth("eighth"),
        Semiquaver("16th");

        private final String value;
        Type(String value) { this.value = value; }
        public String getValue() { return value; }
    }

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final BooleanProperty isChord = new SimpleBooleanProperty();
    private final BooleanProperty isRest = new SimpleBooleanProperty();
    private final ObjectProperty<Character> step = new SimpleObjectProperty<>();
    private final IntegerProperty alter = new SimpleIntegerProperty();
    private final IntegerProperty octave = new SimpleIntegerProperty();
    private final IntegerProperty duration = new SimpleIntegerProperty();
    private final IntegerProperty voice = new SimpleIntegerProperty();
    private final ObjectProperty<Type> type = new SimpleObjectProperty<>();
    private final StringProperty stem = new SimpleStringProperty();
    private final IntegerProperty staff = new SimpleIntegerProperty();

    private Note(Builder builder) {
        setId(builder.id);
        setIsChord(builder.isChord);
        setIsRest(builder.isRest);
        setStep(builder.step);
        setAlter(builder.alter);
        setOctave(builder.octave);
        setDuration(builder.duration);
        setVoice(builder.voice);
        setType(builder.type);
        setStem(builder.stem);
        setStaff(builder.staff);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public boolean isChord() { return isChord.get(); }
    public BooleanProperty isChordProperty() { return isChord; }
    public void setIsChord(boolean isChord) { this.isChord.set(isChord); }

    public boolean isRest() { return isRest.get(); }
    public BooleanProperty isRestProperty() { return isRest; }
    public void setIsRest(boolean isRest) { this.isRest.set(isRest); }

    public char getStep() { return step.get() != null ? step.get() : '\0'; }
    public ObjectProperty<Character> stepProperty() { return step; }
    public void setStep(char step) { this.step.set(step); }

    public int getAlter() { return alter.get(); }
    public IntegerProperty alterProperty() { return alter; }
    public void setAlter(int alter) { this.alter.set(alter); }

    public int getOctave() { return octave.get(); }
    public IntegerProperty octaveProperty() { return octave; }
    public void setOctave(int octave) { this.octave.set(octave); }

    public int getDuration() { return duration.get(); }
    public IntegerProperty durationProperty() { return duration; }
    public void setDuration(int duration) { this.duration.set(duration); }

    public int getVoice() { return voice.get(); }
    public IntegerProperty voiceProperty() { return voice; }
    public void setVoice(int voice) { this.voice.set(voice); }

    public Type getType() { return type.get(); }
    public ObjectProperty<Type> typeProperty() { return type; }
    public void setType(Type type) { this.type.set(type); }

    public String getStem() { return stem.get(); }
    public StringProperty stemProperty() { return stem; }
    public void setStem(String stem) { this.stem.set(stem); }

    public int getStaff() { return staff.get(); }
    public IntegerProperty staffProperty() { return staff; }
    public void setStaff(int staff) { this.staff.set(staff); }

    public static class Builder {
        private int id;
        private boolean isRest;
        private boolean isChord;
        private char step;
        private int alter;
        private int octave;
        private int duration;
        private int voice;
        private Type type;
        private String stem;
        private int staff;

        public Builder(Note existing) {
            this.id = existing.getId();
            this.isRest = existing.isRest();
            this.isChord = existing.isChord();
            this.step = existing.getStep();
            this.alter = existing.getAlter();
            this.octave = existing.getOctave();
            this.duration = existing.getDuration();
            this.voice = existing.getVoice();
            this.type = existing.getType();
            this.stem = existing.getStem();
            this.staff = existing.getStaff();
        }

        public Builder() {}

        public Builder setId(int id) { this.id = id; return this; }
        public Builder isRest(boolean isRest) { this.isRest = isRest; return this; }
        public Builder isChord(boolean isChord) { this.isChord = isChord; return this; }
        public Builder setStep(char step) { this.step = step; return this; }
        public Builder setAlter(int alter) { this.alter = alter; return this; }
        public Builder setOctave(int octave) { this.octave = octave; return this; }
        public Builder setDuration(int duration) { this.duration = duration; return this; }
        public Builder setVoice(int voice) { this.voice = voice; return this; }
        public Builder setType(Type type) { this.type = type; return this; }
        public Builder setStem(String stem) { this.stem = stem; return this; }
        public Builder setStaff(int staff) { this.staff = staff; return this; }

        public Note build() { return new Note(this); }
    }
}
