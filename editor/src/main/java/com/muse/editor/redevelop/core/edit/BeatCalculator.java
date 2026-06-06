package com.muse.editor.redevelop.core.edit;

import com.muse.editor.redevelop.core.model.music.Measure;
import com.muse.editor.redevelop.core.model.music.Note;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BeatCalculator {
    private BeatCalculator() {}

    public static int noteValue(Note.Type type) {
        return switch (type) {
            case Whole      -> 16;
            case Half       -> 8;
            case Quarter    -> 4;
            case Eighth     -> 2;
            case Semiquaver -> 1;
        };
    }

    public static Note.Type valueToType(int value) {
        return switch (value) {
            case 16 -> Note.Type.Whole;
            case 8  -> Note.Type.Half;
            case 4  -> Note.Type.Quarter;
            case 2  -> Note.Type.Eighth;
            case 1  -> Note.Type.Semiquaver;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public static int measureCapacity(int beats, int beatType) {
        return (noteValue(Note.Type.Whole) / beatType) * beats;
    }

    public static int measureLowestValue(int beats, int beatType) {
        return 1 / measureCapacity(beats, beatType);
    }

    public static int measureHighestValue(int beats, int beatType) {
        return measureCapacity(beats, beatType);
    }

    public static int usedCapacity(List<Note> notes) {
        return notes.stream()
                .mapToInt(n -> noteValue(n.getType()))
                .sum();
    }

    public static int leftDifference(Note.Type oldNote, Note.Type newNote) {
        return noteValue(oldNote) - noteValue(newNote);
    }

    public static boolean canBeUsed(Note.Type oldNote, Note.Type newNote) {
        return noteValue(oldNote) >= noteValue(newNote);
    }

    public static boolean canInsert(Measure measure, Note.Type type) {
        int capacity = measureCapacity(
                measure.getAttributes().getBeats(),
                measure.getAttributes().getBeatType()
        );

        int used = usedCapacity(measure.getNotes());
        return (capacity - used) >= noteValue(type);
    }

    public static Optional<Note.Type> largestFittingRest(Measure measure) {
        int remaining = measureCapacity(
                measure.getAttributes().getBeats(),
                measure.getAttributes().getBeatType()
        ) - usedCapacity(measure.getNotes());



        return Arrays.stream(Note.Type.values())
                .sorted(Comparator.comparingInt(BeatCalculator::noteValue).reversed())
                .filter(t -> noteValue(t) <= remaining)
                .findFirst();
    }
}
