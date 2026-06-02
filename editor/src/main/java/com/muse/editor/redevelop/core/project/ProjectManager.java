package com.muse.editor.redevelop.core.project;

import com.muse.editor.redevelop.core.model.dto.NewProjectRequest;
import com.muse.editor.redevelop.core.model.music.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProjectManager {
    private static final ProjectManager instance = new ProjectManager();

    public static ProjectManager getInstance() {
        return instance;
    }

    private final ObjectProperty<Project> currentProject = new SimpleObjectProperty<>(null);

    private ProjectManager() {}

    public Project newProject(NewProjectRequest request) {
        if (request == null) return null;

        final Project       project       = Project.createNew(request.getWorkTitle());
        final ScorePartwise scorePartwise = new ScorePartwise();
        final PartList      partList      = new PartList();

        scorePartwise.setWorkTitle(request.getWorkTitle());
        scorePartwise.setCreator(request.getCreator());

        int P = 0;

        for (String instrument : request.getInstruments()) {
            P++;

            final ScorePart scorePart = new ScorePart.Builder()
                    .setId("P" + P)
                    .setPartName(ScorePart.Name.fromString(instrument))
                    .setPartAbbreviation(ScorePart.Abbreviation.getAbbreviationForName(instrument))
                    .setScoreInstrument(null)
                    .build();

            final Part part = new Part();
            part.setId("P" + P);

            for (int m = 0; m < request.getMeasures(); m++) {
                final Attributes attributes = new Attributes.Builder()
                        .setDivisions(2)
                        .setFifths(0)
                        .setStaves(1)
                        .setTime(request.getBeats(), request.getBeatType())
                        .build();

                final Measure measure = m > 0 ?
                        new Measure.Builder()
                                .setId(m)
                                .setAttributes(attributes)
                                .build()
                        :
                        new Measure.Builder()
                                .setId(m)
                                .build();

                part.getMeasures().add(measure);
            }

            partList.getScoreParts().add(scorePart);
            scorePartwise.getParts().add(part);
        }

        scorePartwise.setPartList(partList);
        project.getScoreProperty().set(scorePartwise);

        currentProject.set(project);

        return project;
    }

    public void closeProject() {
        if (currentProject.get().isSavedProperty().get()) {
            currentProject.set(null);
        }
    }

    public ObjectProperty<Project> currentProjectProperty() {
        return currentProject;
    }

    public ObjectProperty<ScorePartwise> scoreProperty() {
        return currentProject.getValue().getScoreProperty();
    }
}
