package com.muse.editor.redevelop.core.io;

import com.muse.editor.redevelop.core.model.music.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;

public class MXMLWriter {

    public static void write(ScorePartwise score, Path path) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.newDocument();

        final Element root = document.createElement("score-partwise");

        root.setAttribute("version", "4.0");
        document.appendChild(root);

        writeWork(document, root, score);
        writeIdentification(document, root, score);
        writePartList(document, root, score);
        writeParts(document, root, score);

        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(new DOMSource(document), new StreamResult(path.toFile()));
    }

    private static void writeWork(Document document, Element root, ScorePartwise score) {
        if (score.getWorkTitle() == null || score.getWorkTitle().isBlank()) return;

        final Element work = document.createElement("work");
        final Element title = document.createElement("work-title");

        title.setTextContent(clean(score.getWorkTitle()));
        work.appendChild(title);
        root.appendChild(work);
    }

    private static void writeIdentification(Document doc, Element root, ScorePartwise score) {
        if (score.getCreator() == null || score.getCreator().isBlank()) return;

        final Element identification = doc.createElement("identification");
        final Element creator = doc.createElement("creator");
        creator.setAttribute("type", "composer");
        creator.setTextContent(clean(score.getCreator()));
        identification.appendChild(creator);
        root.appendChild(identification);
    }

    private static void writePartList(Document doc, Element root, ScorePartwise score) {
        if (score.getPartList() == null || score.getPartList().getScoreParts().isEmpty()) {
            return;
        }

        final Element partList = doc.createElement("part-list");

        for (ScorePart scorePart : score.getPartList().getScoreParts()) {
            final Element sP = doc.createElement("score-part");
            sP.setAttribute("id", clean(scorePart.getId()));

            final Element partName = doc.createElement("part-name");
            final Element partAbbreviation = doc.createElement("part-abbreviation");

            partName.setTextContent(clean(scorePart.getPartName().getValue())); // Zabezpieczone
            partAbbreviation.setTextContent(clean(scorePart.getPartAbbreviation().getValue())); // Zabezpieczone

            sP.appendChild(partName);
            sP.appendChild(partAbbreviation);

            partList.appendChild(sP);
        }

        root.appendChild(partList);
    }

    private static void writeParts(Document doc, Element root, ScorePartwise score) {
        if (score.getParts() == null || score.getParts().isEmpty()) return;

        int pNumber = 0;
        for (Part parentPart : score.getParts()) {
            final Element part = doc.createElement("part");
            part.setAttribute("id", clean(score.getPartList().getScoreParts().get(pNumber).getId()));
            pNumber += 1;

            int mNumber = 0;
            for (Measure measure : parentPart.getMeasures()) {
                mNumber += 1;
                final Element msr = doc.createElement("measure");
                msr.setAttribute("number", String.valueOf(mNumber));

                for (Note note : measure.getNotes()) {
                    final Element nt = doc.createElement("note");

                    final Element rest = doc.createElement("rest");

                    if (rest == null) {
                        final Element pitch = doc.createElement("pitch");
                        final Element step = doc.createElement("step");
                        final Element octave = doc.createElement("octave");
                        final Element duration = doc.createElement("duration");
                        final Element voice = doc.createElement("voice");
                        final Element type = doc.createElement("type");
                        final Element stem = doc.createElement("stem");

                        step.setTextContent(clean(String.valueOf(note.getStep())));
                        octave.setTextContent(clean(String.valueOf(note.getOctave())));
                        duration.setTextContent(clean(String.valueOf(note.getDuration())));
                        voice.setTextContent(clean(String.valueOf(note.getVoice())));

                        type.setTextContent(note.getType() != null ? clean(note.getType().getValue()) : "");
                        stem.setTextContent(clean(note.getStem()));

                        pitch.appendChild(step);
                        pitch.appendChild(octave);

                        nt.appendChild(pitch);
                        nt.appendChild(duration);
                        nt.appendChild(voice);
                        nt.appendChild(type);
                        nt.appendChild(stem);
                    } else {
                        final Element duration = doc.createElement("duration");
                        final Element voice = doc.createElement("voice");
                        final Element type = doc.createElement("type");

                        duration.setTextContent(clean(String.valueOf(note.getDuration())));
                        voice.setTextContent(clean(String.valueOf(note.getVoice())));
                        type.setTextContent(note.getType() != null ? clean(note.getType().getValue()) : "");

                        nt.appendChild(rest);
                        nt.appendChild(duration);
                        nt.appendChild(voice);
                        nt.appendChild(type);
                    }

                    msr.appendChild(nt);
                }
                part.appendChild(msr);
            }
            root.appendChild(part);
        }
    }

    private static String clean(String text) {
        if (text == null) return "";
        return text.replace("\u0000", "").trim();
    }
}
