package com.muse.editor.ui.util;

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

    public static String getGClef() {
        return "\uD834\uDD1E";
    }

    public static String getFClef() {
        return "\uD834\uDD22";
    }

    public static String getCClef() {
        return "\uE05C";
    }

    public static String getWholeNote() {
        return "\uD834\uDD5D";
    }

    public static String getHalfNote() {
        return "\uD834\uDD5E";
    }

    public static String getQuarterNote() {
        return "\uD834\uDD5F";
    }

    public static String getEighthNote() {
        return "\uD834\uDD60";
    }

    public static String getSixteenthNote() {
        return "\uD834\uDD61";
    }

    public static String getThirtySecondNote() {
        return "\uD834\uDD62";
    }

    public static String getSixtyFourthNote() {
        return "\uD834\uDD63";
    }

    public static String getQuarterNoteSimple() {
        return "\u2669";
    }

    public static String getEighthNoteSimple() {
        return "\u266A";
    }

    public static String getBeamedEighthNotes() {
        return "\u266B";
    }

    public static String getBeamedSixteenthNotes() {
        return "\u266C";
    }

    public static String getWholeRest() {
        return "\uD834\uDD3B";
    }

    public static String getHalfRest() {
        return "\uD834\uDD3C";
    }

    public static String getQuarterRest() {
        return "\uD834\uDD3D";
    }

    public static String getEighthRest() {
        return "\uD834\uDD3E";
    }

    public static String getSixteenthRest() {
        return "\uD834\uDD3F";
    }

    public static String getThirtySecondRest() {
        return "\uD834\uDD40";
    }

    public static String getSixtyFourthRest() {
        return "\uD834\uDD41";
    }

    public static String getSharp() {
        return "\u266F";
    }

    public static String getFlat() {
        return "\u266D";
    }

    public static String getNatural() {
        return "\u266E";
    }

    public static String getDoubleSharp() {
        return "\uD834\uDD2A";
    }

    public static String getDoubleFlat() {
        return "\uD834\uDD2B";
    }

    public static String getDot() {
        return "\uD834\uDD6D";
    }

    public static String getDotSimple() {
        return ".";
    }

    public static String getTimeSignatureCommon() {
        return "\uD834\uDD86";
    }

    public static String getTimeSignatureCut() {
        return "\uD834\uDD87";
    }

    public static String getTimeSignatureCommonText() {
        return "C";
    }

    public static String getTimeSignatureCutText() {
        return "C|";
    }

    public enum Metre {
        TWO_FOUR, THREE_FOUR, FOUR_FOUR,
        SIX_EIGHT, TWELVE_EIGHT;
    }

    public static String getTimeSignature(Metre metre) {
        return switch (metre) {
            case TWO_FOUR    -> "\uE09E\uE082\uE09F\uE084";
            case THREE_FOUR  -> "\uE09E\uE083\uE09F\uE084";
            case FOUR_FOUR   -> "\uE09E\uE084\uE09F\uE084";
            case SIX_EIGHT   -> "\uE09E\uE086\uE09F\uE088";
            case TWELVE_EIGHT -> "\uE09E\uE081\uE082\uE09F\uE088";
            case null -> throw new IllegalArgumentException();
        };
    }

    public enum Dynamic {
        PPP, PP, P, MP, MF, F, FF, FFF, SFZ, FP
    }

    public static String getDynamic(Dynamic dynamic) {
        return switch (dynamic) {
            case PPP -> "ppp";
            case PP -> "pp";
            case P -> "p";
            case MP -> "mp";
            case MF -> "mf";
            case F -> "f";
            case FF -> "ff";
            case FFF -> "fff";
            case SFZ -> "sfz";
            case FP -> "fp";
        };
    }

    public static String getBarlineSingle() {
        return "|";
    }

    public static String getBarlineDouble() {
        return "\u2016";
    }

    public static String getBarlineFinal() {
        return "\uD834\uDD01";
    }

    public static String getBarlineRepeatStart() {
        return "\uD834\uDD00";
    }

    public static String getBarlineRepeatEnd() {
        return "\uD834\uDD02";
    }


}
