package com.muse.editor.develop.ui.util;

public final class SheetMetrics {

    private SheetMetrics() {}

    public static final double STAFF_SPACE = 10.0;

    public static final int LINE_COUNT = 5;
    public static final double LINE_THICKNESS = 1.2;

    public static final double STAFF_HEIGHT = (LINE_COUNT - 1) *  STAFF_SPACE;
    public static final double STAFF_MARGIN_V = STAFF_SPACE * 3;
    public static final double STAFF_AREA_HEIGHT = STAFF_HEIGHT + STAFF_MARGIN_V * 2;

    public static final double NOTE_HEAD_WIDTH = STAFF_SPACE * 1.2;
    public static final double NOTE_HEAD_HEIGHT = STAFF_SPACE * 0.85;
    public static final double STEM_LENGTH      = STAFF_SPACE * 3.5;
    public static final double STEM_THICKNESS   = 1.5;

    public static final double ACCIDENTAL_WIDTH  = STAFF_SPACE * 1.5;

    public static final double MEASURE_MIN_WIDTH  = STAFF_SPACE * 10;
    public static final double MEASURE_PADDING_L  = STAFF_SPACE * 1.5;
    public static final double MEASURE_PADDING_R  = STAFF_SPACE * 1.5;
    public static final double NOTE_MIN_SPACING   = STAFF_SPACE * 3.0;

    public static final double CLEF_WIDTH         = STAFF_SPACE * 3.5;
    public static final double CLEF_FONT_SIZE     = STAFF_SPACE * 3;

    public static final double TIME_SIG_WIDTH     = STAFF_SPACE * 2.5;
    public static final double TIME_SIG_FONT_SIZE = STAFF_SPACE * 3.5;

    public static final double KEY_SIG_SIGN_WIDTH = STAFF_SPACE * 1.2;

    public static final double BAR_LINE_THICKNESS = 1.2;
}
