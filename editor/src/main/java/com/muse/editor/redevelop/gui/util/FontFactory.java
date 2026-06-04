package com.muse.editor.redevelop.gui.util;

import javafx.scene.text.Font;

import java.io.InputStream;

public final class FontFactory {
    private static boolean fontLoaded = false;
    private static Font bravuraFont;

    private FontFactory() {}

    public static Font getBravura(int size) {
        if (!fontLoaded) {
            loadFont();
        }
        if (bravuraFont == null || bravuraFont.getFamily().equals("Arial")) {
            return Font.font("Segoe UI Symbol", size);
        }
        return Font.font(bravuraFont.getFamily(), size);
    }

    private static void loadFont() {
        try {
            InputStream is = com.muse.editor.develop.ui.util.FontFactory.class.getResourceAsStream("/com/muse/editor/font/BravuraText.otf");
            if (is == null) {
                System.out.println("Font file not found!");
                bravuraFont = Font.font("Segoe UI Symbol", 12);
            } else {
                bravuraFont = Font.loadFont(is, 12);
                fontLoaded = true;
            }
        } catch (Exception e) {
            System.err.println("Error loading font: " + e.getMessage());
            bravuraFont = Font.font("Segoe UI Symbol", 12);
        }
    }

    public static String getGClef() {
        return "\uD834\uDD1E";
    }

    public static String getFClef() {
        return "\uD834\uDD22";
    }
}
