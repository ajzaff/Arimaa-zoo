package com.alanjz.arimaa.core;

public enum Piece {
    ELEPHANT, CAMEL, HORSE, DOG, CAT, RABBIT;

    public boolean canBully(Piece piece) {
        return ordinal() < piece.ordinal();
    }

    public static Piece fromString(String piece) {
        switch(piece) {
            case "E":
            case "e": return ELEPHANT;
            case "M":
            case "m": return CAMEL;
            case "H":
            case "h": return HORSE;
            case "D":
            case "d": return DOG;
            case "C":
            case "c": return CAT;
            case "R":
            case "r": return RABBIT;
            default: return null;
        }
    }
}
