package com.muse.editor.core.io.service;

import com.muse.editor.core.io.builder.MusicXmlParser;
import com.muse.editor.core.model.score.ScorePartwise;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;

public class FileIOService {
    private final static FileIOService instance = new FileIOService();

    public static FileIOService getInstance() {
        return instance;
    }

    private FileIOService() {}

    public ScorePartwise load(Path path) {
        final MusicXmlParser parser = new MusicXmlParser();

        try {
            return parser.parse(path);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

}
