package com.muse.editor.model.event;

import java.nio.file.Path;

public class OpenCloudProjectRequestedEvent implements AppEvent {
    private final Path path;

    public OpenCloudProjectRequestedEvent(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}