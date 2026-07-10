package com.muse.editor.core.midi;

import java.io.File;
import java.io.IOException;

public class MidiService {
    private static final MidiService instance = new MidiService();

    public static MidiService getInstance() {
        return instance;
    }

    private MidiService() {}

    public enum MIDI_RUNNER {
        MUSESCORE_STUDIO
    }

    public void run(MIDI_RUNNER by, final File file) {
        switch (by) {
            case MUSESCORE_STUDIO -> musescore(file);
            case null, default -> throw new IllegalArgumentException("No matching runner");
        }
    }

    private void musescore(File file) {
        final String app_path  = "C:\\Program Files\\MuseScore 4\\bin\\MuseScore4.exe";
        final String file_path = file.toPath().toString();

        final ProcessBuilder processBuilder = new ProcessBuilder(app_path, file_path);

        try {
            processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
