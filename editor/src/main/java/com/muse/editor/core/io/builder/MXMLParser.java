package com.muse.editor.core.io.builder;

import com.muse.editor.core.model.score.*;
import com.muse.editor.core.model.score.*;

import nu.xom.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * MusicXML Parser.
 *
 * @see <a href="https://github.com/norbjdk/muse-editor">GitHub</a>
 * @author norbjdk
 */

public class MXMLParser {
    private static final Builder builder = new Builder();

    private MXMLParser() {}

    public static ScorePartwise readData(File mxmlFile) {
        if (mxmlFile == null || !mxmlFile.exists()) {
            throw new NullPointerException("Data is empty");
        }

        try {
            final Document mxmlDocument = builder.build(mxmlFile);
            final Element root = mxmlDocument.getRootElement();

            final ScorePartwise scorePartwise = new ScorePartwise();
            readMetaData(scorePartwise, root);
            readPartList(scorePartwise, root);
            readParts(scorePartwise, root);

            return scorePartwise;

        } catch (ParsingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readMetaData(final ScorePartwise scorePartwise, Element root) {
        final Element work = root.getFirstChildElement("work");
        if (isNotNullElement(work)) {
            final Element title = work.getFirstChildElement("work-title");
            if (isNotNullElement(title)) {
                scorePartwise.setWorkTitle(title.getValue());
            }
        }

        // Creator area

        final Element identification = root.getFirstChildElement("identification");
        if (isNotNullElement(identification)) {
            final Element creator = identification.getFirstChildElement("creator");
            if (isNotNullElement(creator)) {
                scorePartwise.setCreator(creator.getValue());
            }
        }
    }

    private static void readPartList(final ScorePartwise scorePartwise, Element root) {
        if (scorePartwise.getPartList() == null) {
            scorePartwise.setPartList(new PartList());
        }

        final Element partList = root.getFirstChildElement("part-list");
        if (isNotNullElement(partList)) {
            final Elements scoreParts = partList.getChildElements();
            if (scoreParts.size() <= 0) return;

            for (Element element : scoreParts) {
                final ScorePart scorePart = new ScorePart();
                final ScoreInstrument scoreInstrument = new ScoreInstrument();

                // Part name area

                final Element partName = element.getFirstChildElement("part-name");
                if (isNotNullElement(partName)) {
                    scorePart.setPartName(partName.getValue());
                }

                // Part abbreviation area

                final Element partAbbreviation = element.getFirstChildElement("part-abbreviation");
                if (isNotNullElement(partAbbreviation)) {
                    scorePart.setPartAbbreviation(partAbbreviation.getValue());
                }

                // Score instrument area

                final Element scoreInstr = element.getFirstChildElement("score-instrument");
                if (isNotNullElement(scoreInstr)) {
                    final Element instrumentName = scoreInstr.getFirstChildElement("instrument-name");
                    final Element instrumentSound = scoreInstr.getFirstChildElement("instrument-sound");

                    if (isNotNullElement(instrumentName) && isNotNullElement(instrumentSound)) {
                        scoreInstrument.setInstrumentName(instrumentName.getValue());
                        scoreInstrument.setInstrumentSound(instrumentSound.getValue());
                    }
                }

                scorePart.setScoreInstrument(scoreInstrument);
                scorePartwise.getPartList().getScoreParts().add(scorePart);
            }
        }
    }

    private static void readParts(final ScorePartwise scorePartwise, Element root) {
        if (scorePartwise.getParts() == null) {
            scorePartwise.setParts(new ArrayList<>());
        }

        final Elements parts = root.getChildElements("part");
        if (parts.size() <= 0) return;
        for (Element element : parts) {
            final Part part = new Part();
            final Elements measures = element.getChildElements("measure");
            for (Element measure: measures) {
                part.getMeasures().add(readMeasure(measure));
            }
            scorePartwise.getParts().add(part);
        }
    }

    private static Measure readMeasure(final Element measure) {
        final Measure result = new Measure();

        if (isNotNullElement(measure)) {
            final Element attributes = measure.getFirstChildElement("attributes");
            if (isNotNullElement(attributes)) {
                final Element divisions = attributes.getFirstChildElement("divisions");
                final Element staves = attributes.getFirstChildElement("staves");
                final Element fifths = attributes.getFirstChildElement("key").getFirstChildElement("fifths");
                final Element beats = attributes.getFirstChildElement("time").getFirstChildElement("beats");
                final Element beatType = attributes.getFirstChildElement("time").getFirstChildElement("beat-type");
                final Element sign = attributes.getFirstChildElement("clef").getFirstChildElement("sign");
                final Element line = attributes.getFirstChildElement("clef").getFirstChildElement("line");

                if (isNotNullElement(divisions)
                        && isNotNullElement(staves)
                        && isNotNullElement(fifths)
                        && isNotNullElement(beats)
                        && isNotNullElement(beatType)
                        && isNotNullElement(sign)
                        && isNotNullElement(line))
                {
                    final Attributes measureAttributes = new Attributes
                            .Builder()
                            .setDivisions(Integer.parseInt(divisions.getValue()))
                            .setFifths(Integer.parseInt(fifths.getValue()))
                            .whatTime(
                                    Integer.parseInt(beats.getValue()),
                                    Integer.parseInt(beatType.getValue())
                            )
                            .setStaves(Integer.parseInt(staves.getValue()))
                            .whatClef(
                                    sign.getValue().charAt(0),
                                    Integer.parseInt(line.getValue())
                            ).build();
                    result.setAttributes(measureAttributes);

                    final Elements notes = measure.getChildElements("note");
                    if (notes.size() > 0) {
                        for (Element element : notes) {
                            result.addNote(readNote(element));
                        }
                    }
                }
            }
        }
        return result;
    }

    private static Note readNote(final Element note) {
        if (isNotNullElement(note)) {
            final Element rest = note.getFirstChildElement("rest");
            final Element step = note.getFirstChildElement("step");
            final Element alter = note.getFirstChildElement("alter");
            final Element octave = note.getFirstChildElement("octave");
            final Element duration = note.getFirstChildElement("duration");
            final Element voice = note.getFirstChildElement("voice");
            final Element type = note.getFirstChildElement("type");
            final Element stem = note.getFirstChildElement("stem");
            final Element staff = note.getFirstChildElement("staff");

            if (isNotNullElement(rest)
                    && isNotNullElement(step)
                    && isNotNullElement(alter)
                    && isNotNullElement(octave)
                    && isNotNullElement(duration)
                    && isNotNullElement(voice)
                    && isNotNullElement(type)
                    && isNotNullElement(stem)
                    && isNotNullElement(staff)
            ) {
                return new Note
                        .Builder()
                        .isRest(Boolean.parseBoolean(rest.getValue()))
                        .whichStep(step.getValue().charAt(0))
                        .setAlter(Integer.parseInt(alter.getValue()))
                        .whichOctave(Integer.parseInt(octave.getValue()))
                        .setDuration(Integer.parseInt(duration.getValue()))
                        .whichVoice(Integer.parseInt(voice.getValue()))
                        .whatType(type.getValue())
                        .stemDirection(stem.getValue())
                        .whichStaff(Integer.parseInt(staff.getValue()))
                        .build();
            }
        }
        return null;
    }

    private static boolean isNotNullElement(Element element) {
        return element != null;
    }
}
