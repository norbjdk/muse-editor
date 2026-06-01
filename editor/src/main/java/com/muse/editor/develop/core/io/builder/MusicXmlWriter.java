package com.muse.editor.develop.core.io.builder;

import com.muse.editor.develop.core.model.score.*;
import com.muse.editor.develop.core.model.score.*;
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

public class MusicXmlWriter {

    public void write(ScorePartwise score, Path path) throws Exception {
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

    private void writeWork(Document document, Element root, ScorePartwise score) {
        if (score.getWorkTitle() == null || score.getWorkTitle().isBlank()) return;

        final Element work = document.createElement("work");
        final Element title = document.createElement("work-title");
        final Element album = document.createElement("work-subtitle");

        title.setTextContent(score.getWorkTitle());
        album.setTextContent(score.getAlbum());
        work.appendChild(title);
        work.appendChild(album);
        root.appendChild(work);
    }

    private void writeIdentification(Document doc, Element root, ScorePartwise score) {
        if (score.getCreator() == null || score.getCreator().isBlank()) return;

        final Element identification = doc.createElement("identification");
        final Element creator = doc.createElement("creator");
        creator.setAttribute("type", "composer");
        creator.setTextContent(score.getCreator());
        identification.appendChild(creator);
        root.appendChild(identification);
    }

    private void writePartList(Document doc, Element root, ScorePartwise score) {
        if (score.getPartList() == null || score.getPartList().getScoreParts().isEmpty()) {
            return;
        };

        final Element partList = doc.createElement("part-list");

        for (ScorePart scorePart : score.getPartList().getScoreParts()) {
            final Element sP = doc.createElement("score-part");
            sP.setAttribute("id", scorePart.getId());

            final Element partName = doc.createElement("part-name");
            final Element partAbbreviation = doc.createElement("part-abbreviation");

            partName.setTextContent(scorePart.getPartName());
            partAbbreviation.setTextContent(scorePart.getPartAbbreviation());

            sP.appendChild(partName);
            sP.appendChild(partAbbreviation);

            partList.appendChild(sP);
        }

        root.appendChild(partList);
    }

    private void writeParts(Document doc, Element root, ScorePartwise score) {
        if (score.getParts() == null || score.getParts().isEmpty()) return;

        int pNumber = 0;
        for (Part parentPart : score.getParts()) {
            final Element part = doc.createElement("part");
            part.setAttribute("id", score.getPartList().getScoreParts().get(pNumber).getId());
            pNumber += 1;

            int mNumber = 0;
            for (Measure measure : parentPart.getMeasures()) {
                mNumber += 1;
                final Element msr = doc.createElement("measure");
                msr.setAttribute("number", String.valueOf(mNumber));

                for (Note note : measure.getNotes()) {
                    final Element nt = doc.createElement("note");
                    // Note child elements
                    final Element pitch = doc.createElement("pitch");
                    final Element step = doc.createElement("step");
                    final Element octave = doc.createElement("octave");
                    final Element duration = doc.createElement("duration");
                    final Element voice = doc.createElement("voice");
                    final Element type = doc.createElement("type");
                    final Element stem = doc.createElement("stem");

                    step.setTextContent(String.valueOf(note.getStep()));
                    octave.setTextContent(String.valueOf(note.getOctave()));
                    duration.setTextContent(String.valueOf(note.getDuration()));
                    voice.setTextContent(String.valueOf(note.getVoice()));
                    type.setTextContent(note.getType());
                    stem.setTextContent(note.getStem());

                    pitch.appendChild(step);
                    pitch.appendChild(octave);

                    nt.appendChild(pitch);
                    nt.appendChild(duration);
                    nt.appendChild(voice);
                    nt.appendChild(type);
                    nt.appendChild(stem);

                    msr.appendChild(nt);
                }
                part.appendChild(msr);
            }
            root.appendChild(part);
        }
    }
}
