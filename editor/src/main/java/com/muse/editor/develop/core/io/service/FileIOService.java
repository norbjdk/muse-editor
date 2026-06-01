package com.muse.editor.develop.core.io.service;

import com.muse.editor.develop.core.io.builder.MusicXmlParser;
import com.muse.editor.develop.core.io.builder.MusicXmlWriter;
import com.muse.editor.develop.core.model.dto.NewProjectRequest;
import com.muse.editor.develop.core.model.score.ScorePartwise;
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

    public ScorePartwise create(final NewProjectRequest request) {
        final ScorePartwise score = new ScorePartwise();

        score.setWorkTitle(request.getTitle());
        score.setCreator(request.getComposer());
        score.setAlbum(request.getSubtitle());

        return score;
    }

    public void save(ScorePartwise score, Path path) {
        try {
            new MusicXmlWriter().write(score, path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
