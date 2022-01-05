package com.game.util;

public class LevelAndExpCounter {

    public static int CountLevel(Integer exp) {
        return (int)((Math.sqrt(2500 + 200 * exp) - 50) / 100);
    }

    public static int CountExpUntilNextLevel(Integer lvl, Integer exp) {
        return 50 * (lvl + 1) * (lvl + 2) - exp;
    }
}
