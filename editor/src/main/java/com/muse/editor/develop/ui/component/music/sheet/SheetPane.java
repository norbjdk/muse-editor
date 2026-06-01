package com.muse.editor.develop.ui.component.music.sheet;

import com.muse.editor.develop.core.edit.Instrument;
import com.muse.editor.develop.core.model.score.Part;
import com.muse.editor.develop.core.project.Project;
import com.muse.editor.develop.ui.model.Presentable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

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

    public void initComponents() {
        pageContainer = new VBox();

        titleLabel = new Label();
        subtitleLabel = new Label();
        composerLabel = new Label();
    }

    public void setupStyle() {
        this.getStylesheets().add(Objects.requireNonNull(
                getClass().getResource("/com/muse/editor/styles/music.css")).toExternalForm()
        );

        this.getStyleClass().add("sheet-pane");
        pageContainer.getStyleClass().add("page-container");

        titleLabel.getStyleClass().add("title-label");
        subtitleLabel.getStyleClass().add("subtitle-label");
        composerLabel.getStyleClass().add("composer-label");
    }

    public void setupLayout() {
        setFitToWidth(true);
        StackPane wrapper = new StackPane(pageContainer);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(20, 40, 20, 40));
        setContent(wrapper);
    }

    public void setupEventListeners() {
        if (project == null) return;

        project.getScorePartwise().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                Platform.runLater(() -> rebuild(part));
            }
        });
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

        System.out.println("Part ID:" + part.getId());

        if (part != null) {
            rebuild(part);
        }
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
            final FlowPane measures = buildMeasures();
            page.getChildren().add(measures);
        }

        return page;
    }

    protected VBox buildHeader() {
        final VBox header = new VBox(4);

        header.setPadding(new Insets(0, 0, 20, 0));

        titleLabel.setText(project.getScorePartwise().get().getWorkTitle());
        subtitleLabel.setText(project.getScorePartwise().get().getAlbum());
        composerLabel.setText(project.getScorePartwise().get().getCreator());

        header.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                composerLabel
        );

        return header;
    }

    protected abstract FlowPane buildMeasures();

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
