package com.muse.editor.core.edit;

public class Instrument {
    public enum Name {
        Violin("Violin"),
        Viola("Viola"),
        Cello("Cello");

        private final String value;

        Name (String value) {
            this.value = value;
        }

        public String getName() {
            return value;
        }
    }

    private final Name name;

    public Instrument(Instrument.Name name) {
        this.name = name;
    }

    public Name getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.value;
    }
}
