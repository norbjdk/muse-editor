package com.muse.editor.core.edit;

import com.muse.editor.core.EventBus;
import com.muse.editor.core.io.service.FileIOService;
import com.muse.editor.core.model.score.Measure;
import com.muse.editor.core.model.score.Note;
import com.muse.editor.core.model.score.ScorePartwise;
import com.muse.editor.core.project.Project;
import com.muse.editor.core.project.ProjectManager;
import com.muse.editor.model.event.AddNoteRequestedEvent;
import com.muse.editor.model.event.AddRestRequestedEvent;

public class EditService {
    private final static EditService instance = new EditService();

    public static EditService getInstance() {
        return instance;
    }

    private EditService() {}

}
