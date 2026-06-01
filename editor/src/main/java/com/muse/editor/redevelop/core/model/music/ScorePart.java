package com.muse.editor.redevelop.core.model.music;

public class ScorePart {
    private final String          id;
    private final String          partName;
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

    public String getPartName() {
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
        private String          partName;
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

        public Builder setPartName(String partName) {
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
