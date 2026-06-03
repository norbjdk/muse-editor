package com.muse.editor.redevelop.core.io;

import com.muse.editor.redevelop.core.model.dto.NewProjectRequest;
import com.muse.editor.redevelop.core.model.music.ScorePartwise;

public final class FileService {
    private static final FileService instance = new FileService();

    public static FileService getInstance() {
        return instance;
    }

    private FileService() {}
}
