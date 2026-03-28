package com.muse.editor.core.io.service;

import com.muse.editor.core.io.builder.MXMLParser;
import com.muse.editor.core.model.score.ScorePartwise;

import java.nio.file.Path;

public class FileIOService {
    private final static FileIOService instance = new FileIOService();

    public static FileIOService getInstance() {
        return instance;
    }

    private FileIOService() {}

    public ScorePartwise load(Path path) {
        return MXMLParser.readData(path.toFile());
    }

}
