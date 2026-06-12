package com.muse.server.websocket;

public class ScoreMessage {
    public enum Op {
        JOIN_SESSION,
        LEAVE_SESSION,
        REPLACE_NOTE
    }

    private Op     op;
    private Long   projectId;
    private Long   sessionId;
    private String partId;
    private int    measureIndex;
    private int    noteIndex;
    private NoteDto note;

    public static class NoteDto {
        private int     id;
        private String  type;
        private boolean isRest;
        private char    step;
        private int     octave;
        private int     duration;
    }

    public Op getOp() {
        return op;
    }

    public void setOp(Op op) {
        this.op = op;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public int getMeasureIndex() {
        return measureIndex;
    }

    public void setMeasureIndex(int measureIndex) {
        this.measureIndex = measureIndex;
    }

    public int getNoteIndex() {
        return noteIndex;
    }

    public void setNoteIndex(int noteIndex) {
        this.noteIndex = noteIndex;
    }

    public NoteDto getNote() {
        return note;
    }

    public void setNote(NoteDto note) {
        this.note = note;
    }
}