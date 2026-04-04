package com.muse.editor.core.io.builder;

import com.muse.editor.core.model.score.ScorePartwise;
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

        title.setTextContent(score.getWorkTitle());
        work.appendChild(title);
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
}
