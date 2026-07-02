package com.muse.editor.event.project;

import com.muse.editor.core.model.music.ScorePartwise;
import com.muse.editor.event.model.AppEvent;

public class LoadProjectEvent extends AppEvent {
    private final ScorePartwise scorePartwise;

    public LoadProjectEvent(ScorePartwise scorePartwise) {
        this.scorePartwise = scorePartwise;
    }

    public ScorePartwise getScorePartwise() {
        return scorePartwise;
    }
}
