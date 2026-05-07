package com.muse.editor.ui.component.music;

import com.muse.editor.core.edit.Instrument;
import com.muse.editor.core.model.score.Part;
import com.muse.editor.core.project.Project;
import com.muse.editor.ui.model.Presentable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public abstract class SheetPane extends ScrollPane implements Presentable {
    protected final double PAGE_WIDTH = 794;
    protected final double PAGE_MARGIN_H = 72;
    protected final double PAGE_MARGIN_V = 60;

    protected final Instrument instrument;

    protected Project project;
    protected Part    part;

    protected VBox pageContainer = null;

    protected Label titleLabel;
    protected Label subtitleLabel;
    protected Label composerLabel;

    protected SheetPane(Instrument instrument) {
        this.instrument = instrument;
        this.present();
    }

    public void bindProject(Project project) {
        this.project = project;

        for (Part prt : project.getScorePartwise().get().getParts()) {
            project.getScorePartwise().get().getPartList().getScoreParts().forEach(scorePart -> {
                if (prt.getId().equals(scorePart.getId())) {
                    part = prt;
                }
            });
            break;
        }
        rebuild(part);
    }

    protected void rebuild(Part part) {
        pageContainer.getChildren().clear();

        if (part == null) return;

        final VBox page = buildPage();

        pageContainer.getChildren().add(page);
    }

    protected abstract void publishChange();

    private VBox buildPage() {
        final VBox page = new VBox(90);

        page.setPadding(new Insets(
                        PAGE_MARGIN_V,
                        PAGE_MARGIN_H,
                        PAGE_MARGIN_V,
                        PAGE_MARGIN_H
                )
        );

        final String title = project.getScorePartwise().get().getWorkTitle();

        if (title != null && !title.isBlank()) {
            final VBox header = buildHeader();
            page.getChildren().add(header);
        }

        if (part != null && !part.getMeasures().isEmpty()) {
            final VBox measures = buildMeasures();
            page.getChildren().add(measures);
        }

        return page;
    }

    protected abstract VBox buildHeader();

    protected abstract VBox buildMeasures();

    public double getPAGE_WIDTH() {
        return PAGE_WIDTH;
    }

    public double getPAGE_MARGIN_H() {
        return PAGE_MARGIN_H;
    }

    public double getPAGE_MARGIN_V() {
        return PAGE_MARGIN_V;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public VBox getPageContainer() {
        return pageContainer;
    }

    public void setPageContainer(VBox pageContainer) {
        this.pageContainer = pageContainer;
    }

    public Part getPart() {
        return part;
    }
}
