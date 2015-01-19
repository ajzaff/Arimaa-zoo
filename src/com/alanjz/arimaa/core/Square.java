package com.alanjz.arimaa.core;

import com.alanjz.arimaa.core.moves.Direction;

public enum Square {
    A1,B1,C1,D1,E1,F1,G1,H1,
    A2,B2,C2,D2,E2,F2,G2,H2,
    A3,B3,C3,D3,E3,F3,G3,H3,
    A4,B4,C4,D4,E4,F4,G4,H4,
    A5,B5,C5,D5,E5,F5,G5,H5,
    A6,B6,C6,D6,E6,F6,G6,H6,
    A7,B7,C7,D7,E7,F7,G7,H7,
    A8,B8,C8,D8,E8,F8,G8,H8;

    private Boolean trap;

    public boolean isTrap() {
        if(trap == null) {
            trap = this == C3 || this == F3 || this == C6 || this == F6;
        }
        return trap;
    }

    public boolean canAdd(Direction d) {
        switch(d) {
            case NORTH: return ordinal()+8 <= H8.ordinal();
            case EAST: return (ordinal()+1) % 8 != 0;
            case SOUTH: return ordinal()-8 >= A1.ordinal();
            case WEST: return ordinal() % 8 != 0;
            case NONE: return true;
            default: return false;
        }
    }

    public Square add(Direction d) {
        if(!canAdd(d)) return null;
        return values()[ordinal() + d.getValue()];
    }

    public Square getAdjacentTrap() {
        Square north = add(Direction.NORTH);
        Square east = add(Direction.EAST);
        Square south = add(Direction.SOUTH);
        Square west = add(Direction.WEST);

        if(north != null && north.isTrap()) return north;
        if(east != null && east.isTrap()) return east;
        if(south != null && south.isTrap()) return south;
        if(west != null && west.isTrap()) return west;

        return null;
    }

    public static Square fromString(String str) {
        if(str.length() != 2) return null;
        int file = (str.charAt(0) - 'a');
        int rank = (str.charAt(1) - '1');
        return values()[file + 8*rank];
    }

    public static Square fromInteger(int i) {
        return values()[i];
    }
}
