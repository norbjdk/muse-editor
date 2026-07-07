package com.muse.editor.event.project;

import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.event.model.AppEvent;

public class LoadProjectEvent extends AppEvent {
    private final ScorePartwise scorePartwise;
    private final Long serverId;

    public LoadProjectEvent(ScorePartwise scorePartwise) {
        this.scorePartwise = scorePartwise;
        this.serverId = null;
    }

    public LoadProjectEvent(Long serverId, ScorePartwise scorePartwise) {
        this.scorePartwise = scorePartwise;
        this.serverId = serverId;
    }

    public ScorePartwise getScorePartwise() {
        return scorePartwise;
    }

    public Long getServerId() {
        return serverId;
    }
}
