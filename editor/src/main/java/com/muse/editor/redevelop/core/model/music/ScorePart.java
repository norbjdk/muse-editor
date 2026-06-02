package com.muse.editor.redevelop.core.model.music;

public class ScorePart {
    public enum Name {
        Piano("Piano"),
        Violin("Violin"),
        Viola("Viola"),
        Cello("Cello"),
        Flute("Flute"),
        Trumpet("Trumpet");

        private final String value;

        Name(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private final String          id;
    private final Name            partName;
    private final String          partAbbreviation;
    private final ScoreInstrument scoreInstrument;

    private ScorePart(Builder builder) {
        this.id               = builder.id;
        this.partName         = builder.partName;
        this.partAbbreviation = builder.partAbbreviation;
        this.scoreInstrument  = builder.scoreInstrument;
    }

    public String getId() {
        return id;
    }

    public Name getPartName() {
        return partName;
    }

    public String getPartAbbreviation() {
        return partAbbreviation;
    }

    public ScoreInstrument getScoreInstrument() {
        return scoreInstrument;
    }

    public static class Builder {
        private String          id;
        private Name            partName;
        private String          partAbbreviation;
        private ScoreInstrument scoreInstrument;

        public Builder(ScorePart existing) {
            this.id               = existing.id;
            this.partName         = existing.partName;
            this.partAbbreviation = existing.partAbbreviation;
            this.scoreInstrument  = existing.scoreInstrument;
        }

        public Builder() {}

        public Builder setId(String id) {
            this.id = id;

            return this;
        }

        public Builder setPartName(Name partName) {
            this.partName = partName;

            return this;
        }

        public Builder setPartAbbreviation(String partAbbreviation) {
            this.partAbbreviation = partAbbreviation;

            return this;
        }

        public Builder setScoreInstrument(ScoreInstrument scoreInstrument) {
            this.scoreInstrument = scoreInstrument;

            return this;
        }

        public ScorePart build() {
            return new ScorePart(this);
        }
    }
}
