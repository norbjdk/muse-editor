package com.muse.editor.gui.util;

import com.muse.editor.gui.component.music.SheetComponent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SnapshotUtil {
    private static final SnapshotUtil instance = new SnapshotUtil();

    private VBox sheetPage;

    public static SnapshotUtil getInstance() {
        return instance;
    }

    public void init(VBox sheetPage) {
        this.sheetPage = sheetPage;
    }

    public VBox getSheetPage() {
        return sheetPage;
    }

    private SnapshotUtil() {}

    public WritableImage snapSheet() {
        return sheetPage.snapshot(new SnapshotParameters(), null);
    }

    public File saveToTempFile(WritableImage image) throws IOException {
        File tempFile = Files.createTempFile("sheet_snapshot_", ".png").toFile();

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getPixelReader().getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }

        ImageIO.write(bufferedImage, "png", tempFile);

        return tempFile;
    }
}
