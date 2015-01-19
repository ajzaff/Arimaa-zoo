package com.alanjz.arimaa.core.moves;

public enum Direction {
    NORTH(8),
    EAST(1),
    SOUTH(-8),
    WEST(-1),
    NONE(0);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Direction fromString(String str) {
        switch(str) {
            case "n": return NORTH;
            case "e": return EAST;
            case "s": return SOUTH;
            case "w": return WEST;
            default: return NONE;
        }
    }
}
