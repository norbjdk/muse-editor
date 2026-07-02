package com.muse.editor.event.project;

import com.muse.editor.event.model.AppEvent;

public class DownloadProjectEvent extends AppEvent {
    private final Long id;

    public DownloadProjectEvent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
