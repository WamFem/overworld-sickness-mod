package com.ramithemri.overworldsickness.client;

public class ZombifiedHeartsManager {
    private static final int MAX_ZOMBIFIED_HEARTS = 10;
    private static int zombifiedHearts = 0;
    private static boolean sicknessActive = false;

    public static void setZombifiedHearts(int hearts) {
        zombifiedHearts = Math.max(0, Math.min(hearts, MAX_ZOMBIFIED_HEARTS));
    }

    public static int getZombifiedHearts() {
        return zombifiedHearts;
    }

    public static void setSicknessActive(boolean active) {
        sicknessActive = active;
        if (!active) {
            zombifiedHearts = 0;
        }
    }

    public static boolean isSicknessActive() {
        return sicknessActive;
    }
}
