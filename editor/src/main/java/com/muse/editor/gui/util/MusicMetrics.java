package com.muse.editor.gui.util;

public class MusicMetrics {
    public static final double CLEF_CANVAS_WIDTH = 35.0;
    public static final double CLEF_CANVAS_HEIGHT = 70.0;
    public static final int CLEF_FONT_SIZE = 48;

    public static final double METRE_CANVAS_WIDTH = 30.0;
    public static final double METRE_CANVAS_HEIGHT = 60.0;
    public static final int METRE_FONT_SIZE = 48;

    public static final double NOTE_CANVAS_WIDTH = 30.0;
    public static final double NOTE_CANVAS_HEIGHT = 60.0;
    public static final int NOTE_FONT_SIZE = 50;

    public static final double BASE_MEASURE_WIDTH = 120.0;
    public static final int VISIBLE_LINES_NUMBER = 5;
    public static final int INVISIBLE_LINES_NUMBER = 3;
    public static final int TOTAL_LINES_NUMBER = VISIBLE_LINES_NUMBER + INVISIBLE_LINES_NUMBER;
    public static final int TOTAL_BLANK_SPACES_NUMBER = 8;
    public static final int TOTAL_STAFF_ELEMENTS = TOTAL_LINES_NUMBER + TOTAL_BLANK_SPACES_NUMBER;

    public static final double LINE_HEIGHT = 0.7;
    public static final double BLANK_SPACE_HEIGHT = 10.0;

    public static final double MEASURE_TOP_Y = 2 * BLANK_SPACE_HEIGHT + LINE_HEIGHT;
    public static final double MEASURE_BOTTOM_Y = 6 * BLANK_SPACE_HEIGHT + 5 * LINE_HEIGHT;

    public static final int BAR_LINE_STROKE = 1;
}
