package com.muse.editor.util;

public class Debug {
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";

    public static void check(String message) {
        System.out.println(muse() + PURPLE + message + RESET);
    }

    public static void pass(String message) {
        System.out.println(muse() + GREEN + message + RESET);
    }

    public static void fail(String message) {
        System.out.println(muse() + RED + message + RESET);
    }

    private static String muse() {
        return YELLOW + "[MUSE] " + RESET;
    }
}
