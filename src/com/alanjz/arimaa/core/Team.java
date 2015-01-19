package com.alanjz.arimaa.core;

public enum Team {
    GOLD('g'),
    SILVER('s');

    private Team opponent;
    private final char teamLetter;

    Team(char teamLetter) {
        this.teamLetter = teamLetter;
    }

    public char toChar() {
        return teamLetter;
    }

    public Team getOpponent() {
        if (opponent == null) {
            if (this == GOLD) {
                opponent = SILVER;
            } else {
                opponent = GOLD;
            }
        }
        return opponent;
    }

    public static Team fromString(String str) {
        switch(str) {
            case "g": return GOLD;
            case "s": return SILVER;
            default:
                if(str.isEmpty()) return GOLD;
                else if(Character.isUpperCase(str.charAt(0))) return GOLD;
                else return SILVER;
        }
    }
}
