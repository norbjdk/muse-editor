package com.muse.editor.core.io.builder;

import com.muse.editor.core.model.score.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class WriterTest {
    public static void main(String [] args) {
        MusicXmlWriter writer = new MusicXmlWriter();
        File file = new File("C:\\Users\\Norbert\\Desktop\\Praca Dyplomowa\\muse-editor\\test.musicxml");
        try {
            ScorePartwise scorePartwise = new ScorePartwise();
            scorePartwise.setWorkTitle("Si No Esta's");
            scorePartwise.setCreator("Inigo Quintero");

            final PartList partList = new PartList();
            final ScorePart violinPart = new ScorePart();
            violinPart.setId("V1");
            violinPart.setPartName("Violin");
            violinPart.setPartAbbreviation("Vln");

            final ScorePart pianoPart = new ScorePart();
            pianoPart.setId("P1");
            pianoPart.setPartName("Piano");
            pianoPart.setPartAbbreviation("Pno");

            partList.getScoreParts().add(violinPart);
            partList.getScoreParts().add(pianoPart);
            scorePartwise.setPartList(partList);

            final Part partP1 = new Part();
            final Part partP2 = new Part();

            final Measure measureP1 = new Measure();
            final Measure measureP2 = new Measure();

            measureP1.addNote(new Note.Builder()
                    .isRest(false)
                    .whichStep('D')
                    .whichOctave(4)
                    .setDuration(1)
                    .whichVoice(1)
                    .whatType("quarter")
                    .stemDirection("up")
                    .build()
            );

            measureP2.addNote(new Note.Builder()
                    .isRest(false)
                    .whichStep('A')
                    .whichOctave(4)
                    .setDuration(1)
                    .whichVoice(1)
                    .whatType("quarter")
                    .stemDirection("up")
                    .build()
            );

            measureP1.addNote(new Note.Builder()
                    .isRest(false)
                    .whichStep('E')
                    .whichOctave(4)
                    .setDuration(1)
                    .whichVoice(1)
                    .whatType("quarter")
                    .stemDirection("up")
                    .build()
            );

            measureP2.addNote(new Note.Builder()
                    .isRest(false)
                    .whichStep('B')
                    .whichOctave(4)
                    .setDuration(1)
                    .whichVoice(1)
                    .whatType("quarter")
                    .stemDirection("up")
                    .build()
            );

            partP1.getMeasures().add(measureP1);
            partP2.getMeasures().add(measureP2);

            scorePartwise.getParts().add(partP1);
            scorePartwise.getParts().add(partP2);

            writer.write(scorePartwise, file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
