package com.muse.editor.redevelop.gui.util;

import javafx.scene.text.Font;

import java.io.InputStream;

public final class FontFactory {
    private static boolean fontLoaded = false;
    private static Font bravuraFont;

    private FontFactory() {}

    public static Font getBravura(double size) {
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
            InputStream is = FontFactory.class.getResourceAsStream("/com/muse/editor/font/BravuraText.otf");
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

    public static String getGClef() { return "\uD834\uDD1E"; }
    public static String getFClef() { return "\uD834\uDD22"; }
    public static String getCClef() { return "\uD834\uDD21"; }

    public static String getWholeNote() { return "\uD834\uDD5D"; }
    public static String getHalfNote() { return "\uD834\uDD5E"; }
    public static String getQuarterNote() { return "\uD834\uDD5F"; }
    public static String getEighthNote() { return "\uD834\uDD60"; }
    public static String getSixteenthNote() { return "\uD834\uDD61"; }
    public static String getWholeRest() { return "\uD834\uDD3B"; }
    public static String getHalfRest() { return "\uD834\uDD3C"; }
    public static String getQuarterRest() { return "\uD834\uDD3D"; }
    public static String getEighthRest() { return "\uD834\uDD3E"; }
    public static String getSixteenthRest() { return "\uD834\uDD3F"; }


    public static String getSharp() { return "\uD834\uDD2A"; }
    public static String getFlat() { return "\uD834\uDD2B"; }
    public static String getNatural() { return "\uD834\uDD2C"; }


    public static String getDynamicP() { return "\uD834\uDD8F"; }
    public static String getDynamicF() { return "\uD834\uDD91"; }
    public static String getDynamicMp() { return "\uD834\uDD90\uD834\uDD8F"; }
    public static String getDynamicMf() { return "\uD834\uDD90\uD834\uDD91"; }
}