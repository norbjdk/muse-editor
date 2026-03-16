package com.norbjdk.museeditor.core.io.service;

import com.norbjdk.museeditor.core.document.Project;
import com.norbjdk.museeditor.core.io.builder.MXMLParser;
import com.norbjdk.museeditor.core.model.score.ScorePartwise;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class FileIOService {
    private final static FileIOService instance = new FileIOService();

    public static FileIOService getInstance() {
        return instance;
    }

    private FileIOService() {}

    public ScorePartwise load(Path path) throws IOException {
        return MXMLParser.readData(path.toFile());
    }

}
