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

public class MusicXmlParser {

    public ScorePartwise parse(Path path) throws IOException, ParserConfigurationException, SAXException {
        try (InputStream is = Files.newInputStream(path)){
            return parse(is);
        }
    }

    public ScorePartwise parse(InputStream is) throws IOException, ParserConfigurationException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute("http://apache.org/xml/properties/security-manager", null);

        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.parse(is);
        doc.getDocumentElement().normalize();

        final ScorePartwise score = new ScorePartwise();

        score.setWorkTitle(getText(doc, "work-title"));
        score.setCreator(getCreator(doc));
        score.setPartList(parseParseList(doc));

        NodeList partNodes = doc.getElementsByTagName("part");
        for (int i = 0; i < partNodes.getLength(); i++) {
            Element partEl = (Element) partNodes.item(i);
            Part part = new Part();
            NodeList measureNodes = partEl.getElementsByTagName("measure");
            for (int j = 0; j < measureNodes.getLength(); j++) {
                Element measureEl = (Element) measureNodes.item(j);
                Measure measure = new Measure();
                NodeList noteNodes = measureEl.getElementsByTagName("note");
                for (int k = 0; k < noteNodes.getLength(); k++) {
                    measure.getNotes().add(parseNote((Element) noteNodes.item(k)));
                }
                part.getMeasures().add(measure);
            }
            score.getParts().add(part);
        }

        return score;
    }

    private PartList parseParseList(Document doc) {
        final PartList partList = new PartList();
        final NodeList scorePartNodes = doc.getElementsByTagName("score-part");

        for (int i = 0; i < scorePartNodes.getLength(); i++) {
            final Element element = (Element) scorePartNodes.item(i);
            final ScorePart scorePart = new ScorePart();
            final String partName = getText(element, "part-name");
            final String partAbbreviation = getText(element, "part-abbreviation");

            scorePart.setPartName(partName);
            scorePart.setPartAbbreviation(partAbbreviation);

            partList.getScoreParts().add(scorePart);
        }

        return partList;
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
