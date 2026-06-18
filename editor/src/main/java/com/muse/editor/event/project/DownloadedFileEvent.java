package com.muse.editor.event.project;

import com.muse.editor.event.model.AppEvent;

import java.io.File;

public class DownloadedFileEvent extends AppEvent {
    private final File file;

    public DownloadedFileEvent(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
