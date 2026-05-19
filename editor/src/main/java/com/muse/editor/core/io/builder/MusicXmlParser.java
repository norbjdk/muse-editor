package com.muse.editor.core.io.builder;

import com.muse.editor.core.model.score.*;
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

public class MusicXmlParser {

    public ScorePartwise parse(Path path) throws IOException, ParserConfigurationException, SAXException {
        try (InputStream is = Files.newInputStream(path)){
            return parse(is);
        }
    }

    public ScorePartwise parse(InputStream is) throws IOException, ParserConfigurationException, SAXException {
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

    private PartList parsePartList(Document doc) {
        final PartList partList = new PartList();
        final NodeList scorePartNodes = doc.getElementsByTagName("score-part");

        for (int i = 0; i < scorePartNodes.getLength(); i++) {
            final Element element = (Element) scorePartNodes.item(i);
            final ScorePart scorePart = new ScorePart();
            final ScoreInstrument scoreInstrument = new ScoreInstrument();
            final String partId = element.getAttribute("id");
            final String partName = getText(element, "part-name");
            final String partAbbreviation = getText(element, "part-abbreviation");

            scorePart.setId(partId);
            scorePart.setPartName(partName);
            scorePart.setPartAbbreviation(partAbbreviation);
            scoreInstrument.setInstrumentName(partName);
            scorePart.setScoreInstrument(scoreInstrument);

            partList.getScoreParts().add(scorePart);
        }

        return partList;
    }

    private List<Part> parseParts(Document doc) {
        final List<Part> parts = new ArrayList<>();
        final NodeList partNodes = doc.getElementsByTagName("part");

        for (int i = 0; i < partNodes.getLength(); i++) {
            final Element partEl = (Element) partNodes.item(i);
            final Part part = new Part();
            part.setId(partEl.getAttribute("id"));

            final NodeList measureNodes = partEl.getElementsByTagName("measure");

            for (int j = 0; j < measureNodes.getLength(); j++) {
                final Element measureEl = (Element) measureNodes.item(j);
                final Measure measure = new Measure();

                final Element attrsEl = getChild(measureEl, "attributes");
                if (attrsEl != null) {
                    measure.setAttributes(parseAttributes(attrsEl));
                }

                final NodeList noteNodes = measureEl.getElementsByTagName("note");
                for (int k = 0; k < noteNodes.getLength(); k++) {
                    final Note note = parseNote((Element) noteNodes.item(k));
                    if (note != null) measure.getNotes().add(note);
                }

                part.getMeasures().add(measure);
            }

            parts.add(part);
        }

        return parts;
    }

    private Measure parseMeasure(Element el) {
        final Measure measure = new Measure();

        return measure;
    }

    private Attributes parseAttributes(Element el) {
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
            builder.whatTime(beats, beatType);
        }

        final NodeList clefNodes = el.getElementsByTagName("clef");
        final char[] signs = new char[clefNodes.getLength() == 0 ? 1 : clefNodes.getLength()];
        int firstLine = 2;
        for (int i = 0; i < clefNodes.getLength(); i++) {
            Element clefEl = (Element) clefNodes.item(i);
            String sign = getText(clefEl, "sign");
            signs[i] = sign.isEmpty() ? 'G' : sign.charAt(0);
            if (i == 0) firstLine = parseInt(getText(clefEl, "line"), 2);
        }
        if (signs.length == 0) signs[0] = 'G';
        builder.whatClef(signs, firstLine);

        return builder.build();
    }

    private Note parseNote(Element element) {
        Note.Builder builder = new Note.Builder();

        builder.isRest(getChild(element, "rest") != null);

        final Element pitchElement = getChild(element, "pitch");
        if (pitchElement != null) {
            final String step = getText(pitchElement, "step");
            if (!step.isEmpty()) {
                builder.whichStep(step.charAt(0));
            }

            builder.whichOctave(parseInt(getText(pitchElement, "octave"), 4));

            final String alter = getText(pitchElement, "alter");
            if (!alter.isEmpty()) {
                builder.setAlter(Integer.parseInt(alter));
            }
        }

        builder.setDuration(parseInt(getText(element, "duration"), 0));
        builder.whichVoice(parseInt(getText(element, "voice"), 1));

        final String type = getText(element, "type");
        if (!type.isEmpty()) {
            builder.whatType(type);
        }

        final String stem = getText(element, "stem");
        if (!stem.isEmpty()) {
            builder.stemDirection(stem);
        }

        builder.whichStaff(parseInt(getText(element, "staff"), 1));

        return builder.build();
    }

    private String getText(Element parent, String tagName) {
        final NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "";
        return nodes.item(0).getTextContent().trim();
    }

    private String getText(Document doc, String tagName) {
        final NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return "";
        return nodes.item(0).getTextContent().trim();
    }

    private Element getChild(Element parent, String tagName) {
        final NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element e && e.getTagName().equals(tagName)) {
                return e;
            }
        }
        return null;
    }

    private String getCreator(Document doc) {
        final NodeList creators = doc.getElementsByTagName("creator");
        for (int i = 0; i < creators.getLength(); i++) {
            Element el = (Element) creators.item(i);
            if ("composer".equals(el.getAttribute("type"))) {
                return el.getTextContent().trim();
            }
        }
        return "";
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return defaultValue; }
    }
}
