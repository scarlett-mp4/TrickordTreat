package me.skarless;

import me.skarless.utils.TrickordTreat;

import java.io.IOException;

public class Main {

    // Bot Settings
    public static String TOKEN = "YOUR TOKEN HERE";
    public static boolean DEBUG = true;
    public static int DELAY_MIN = 10;
    public static int DELAY_MAX = 20;

    // In the beginning, there was public static void main(String[] args)
    public static void main(String[] args) throws IOException {
        new TrickordTreat();
    }

}
