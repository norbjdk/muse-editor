package com.muse.editor.core.model.score;

public class ScorePart {
    private String partName;
    private String partAbbreviation;
    private ScoreInstrument scoreInstrument;

    public ScorePart() {}

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartAbbreviation() {
        return partAbbreviation;
    }

    public void setPartAbbreviation(String partAbbreviation) {
        this.partAbbreviation = partAbbreviation;
    }

    public ScoreInstrument getScoreInstrument() {
        return scoreInstrument;
    }

    public void setScoreInstrument(ScoreInstrument scoreInstrument) {
        this.scoreInstrument = scoreInstrument;
    }
}
