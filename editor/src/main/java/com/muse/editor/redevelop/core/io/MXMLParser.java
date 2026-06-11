package com.muse.editor.redevelop.core.io;

import com.muse.editor.redevelop.core.model.music.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MXMLParser {

    public static ScorePartwise parse(Path path) throws IOException, ParserConfigurationException, SAXException {
        try (InputStream is = Files.newInputStream(path)){
            return parse(is);
        }
    }

    private static ScorePartwise parse(InputStream is) throws IOException, ParserConfigurationException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute("http://apache.org/xml/properties/security-manager", null);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setValidating(false);

        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();

        final ScorePartwise score = new ScorePartwise();

        score.setWorkTitle(getText(doc, "work-title"));
        score.setCreator(getCreator(doc));
        score.setPartList(parsePartList(doc));
        score.getParts().addAll(parseParts(doc));

        return score;
    }

    private static PartList parsePartList(Document doc) {
        final PartList partList = new PartList();
        final NodeList scorePartNodes = doc.getElementsByTagName("score-part");

        for (int i = 0; i < scorePartNodes.getLength(); i++) {
            final Element element = (Element) scorePartNodes.item(i);
            final ScoreInstrument scoreInstrument = new ScoreInstrument();
            final String partId = element.getAttribute("id");
            final String partName = getText(element, "part-name");
            final String partAbbreviation = getText(element, "part-abbreviation");

            scoreInstrument.setInstrumentName(partName);

            final ScorePart scorePart = new ScorePart.Builder()
                    .setPartName(ScorePart.Name.fromString(partName))
                    .setPartAbbreviation(ScorePart.Abbreviation.fromString(partAbbreviation))
                    .setId(partId)
                    .setScoreInstrument(scoreInstrument)
                    .build();

            partList.getScoreParts().add(scorePart);
        }

        return partList;
    }

    private static List<Part> parseParts(Document doc) {
        final List<Part> parts = new ArrayList<>();
        final NodeList partNodes = doc.getElementsByTagName("part");

        int globalNoteId = 1;

        for (int i = 0; i < partNodes.getLength(); i++) {
            final Element partEl = (Element) partNodes.item(i);

            final Part part = new Part();
            part.setId(partEl.getAttribute("id"));

            final NodeList measureNodes = partEl.getElementsByTagName("measure");

            for (int j = 0; j < measureNodes.getLength(); j++) {
                final Element measureEl = (Element) measureNodes.item(j);

                final Measure.Builder measureBuilder = new Measure.Builder();

                String measureNumStr = measureEl.getAttribute("number");
                if (!measureNumStr.isEmpty()) {
                    measureBuilder.setId(parseInt(measureNumStr, j + 1));
                } else {
                    measureBuilder.setId(j + 1);
                }

                final Element attrsEl = getChild(measureEl, "attributes");
                if (attrsEl != null) {
                    measureBuilder.setAttributes(parseAttributes(attrsEl));
                }

                final Measure measure = measureBuilder.build();

                final NodeList noteNodes = measureEl.getElementsByTagName("note");
                for (int k = 0; k < noteNodes.getLength(); k++) {
                    final Note note = parseNote(globalNoteId, (Element) noteNodes.item(k));
                    if (note != null) {
                        measure.getNotes().add(note);
                        globalNoteId++;
                    }
                }

                part.getMeasures().add(measure);
            }

            parts.add(part);
        }

        return parts;
    }


    private static Measure parseMeasure(Element el) {
        final Measure measure = new Measure.Builder().build();

        return measure;
    }

    private static Attributes parseAttributes(Element el) {
        final Attributes.Builder builder = new Attributes.Builder();

        final String divisions = getText(el, "divisions");
        if (!divisions.isEmpty()) builder.setDivisions(parseInt(divisions, 1));

        final String staves = getText(el, "staves");
        builder.setStaves(parseInt(staves, 1));

        final Element keyEl = getChild(el, "key");
        if (keyEl != null) {
            builder.setFifths(parseInt(getText(keyEl, "fifths"), 0));
        }

        final Element timeEl = getChild(el, "time");
        if (timeEl != null) {
            int beats     = parseInt(getText(timeEl, "beats"), 4);
            int beatType  = parseInt(getText(timeEl, "beat-type"), 4);
            builder.setBeats(beats);
            builder.setBeatType(beatType);
        }

        final NodeList clefNodes = el.getElementsByTagName("clef");
        final List<Clef> parsedClefs = new ArrayList<>();

        if (clefNodes.getLength() > 0) {
            for (int i = 0; i < clefNodes.getLength(); i++) {
                Element clefEl = (Element) clefNodes.item(i);

                String signText = getText(clefEl, "sign");
                char sign = signText.isEmpty() ? 'G' : signText.charAt(0);

                int defaultLine = (sign == 'F') ? 4 : 2;
                int line = parseInt(getText(clefEl, "line"), defaultLine);

                String numberAttr = clefEl.getAttribute("number");
                int number = numberAttr.isEmpty() ? (i + 1) : parseInt(numberAttr, i + 1);


                final Clef clef = new Clef();
                clef.setSign(sign);
                clef.setLine(line);
                clef.setNumber(number);

                parsedClefs.add(clef);
            }
        } else {
            final Clef defaultClef = new Clef();
            defaultClef.setSign('G');
            defaultClef.setLine(2);
            defaultClef.setNumber(1);

            parsedClefs.add(defaultClef);
        }

        builder.setClefs(parsedClefs);

        return builder.build();
    }


    private static Note parseNote(int id, Element element) {
        Note.Builder builder = new Note.Builder();

        builder.setId(id);
        builder.isRest(getChild(element, "rest") != null);

        final Element pitchElement = getChild(element, "pitch");
        if (pitchElement != null) {
            final String step = getText(pitchElement, "step");
            if (!step.isEmpty()) {
                builder.setStep(step.charAt(0));
            }

            builder.setOctave(parseInt(getText(pitchElement, "octave"), 4));

            final String alter = getText(pitchElement, "alter");
            if (!alter.isEmpty()) {
                builder.setAlter(Integer.parseInt(alter));
            }
        }

        builder.setDuration(parseInt(getText(element, "duration"), 0));
        builder.setVoice(parseInt(getText(element, "voice"), 1));

        final String typeStr = getText(element, "type");
        if (!typeStr.isEmpty()) {
            Note.Type mappedType = Note.Type.Quarter;
            for (Note.Type t : Note.Type.values()) {
                if (t.getValue().equalsIgnoreCase(typeStr)) {
                    mappedType = t;
                    break;
                }
            }
            builder.setType(mappedType);
        }

        final String stem = getText(element, "stem");
        if (!stem.isEmpty()) {
            builder.setStem(stem);
        }

        builder.setStaff(parseInt(getText(element, "staff"), 1));

        return builder.build();
    }

    private static String getText(Element parent, String tagName) {
        final NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "";
        return nodes.item(0).getTextContent().trim();
    }

    private static String getText(Document doc, String tagName) {
        final NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "";
        return nodes.item(0).getTextContent().trim();
    }

    private static Element getChild(Element parent, String tagName) {
        final NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element e && e.getTagName().equals(tagName)) {
                return e;
            }
        }
        return null;
    }

    private static String getCreator(Document doc) {
        final NodeList creators = doc.getElementsByTagName("creator");
        for (int i = 0; i < creators.getLength(); i++) {
            Element el = (Element) creators.item(i);
            if ("composer".equals(el.getAttribute("type"))) {
                return el.getTextContent().trim();
            }
        }
        return "";
    }

    private static int parseInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return defaultValue; }
    }
}
