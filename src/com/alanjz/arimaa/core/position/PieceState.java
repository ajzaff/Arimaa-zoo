package com.alanjz.arimaa.core.position;

import com.alanjz.arimaa.core.IllegalGameActionException;
import com.alanjz.arimaa.core.Piece;
import com.alanjz.arimaa.core.Square;
import com.alanjz.arimaa.core.numerics.Bits;

import static com.alanjz.arimaa.core.Piece.*;

public class PieceState {
    protected long elephant;
    protected long camel;
    protected long horses;
    protected long dogs;
    protected long cats;
    protected long rabbits;
    protected long frozen;

    protected int elephantCount;
    protected int camelCount;
    protected int horseCount;
    protected int dogCount;
    protected int catCount;
    protected int rabbitCount;

    public boolean hasAllPieces() {
        return getElephantCount() == 1 &&
                getCamelCount() == 1 &&
                getHorseCount() == 2 &&
                getDogCount() == 2 &&
                getCatCount() == 2 &&
                getRabbitCount() == 8;
    }

    public static int getMaximumCount(Piece piece) {
        switch(piece) {
            case ELEPHANT: return 1;
            case CAMEL: return 1;
            case HORSE: return 2;
            case DOG: return 2;
            case CAT: return 2;
            case RABBIT: return 8;
        }
        return 0;
    }

    public int getCount(Piece piece) {
        switch(piece) {
            case ELEPHANT: return getElephantCount();
            case CAMEL: return getCamelCount();
            case HORSE: return getHorseCount();
            case DOG: return getDogCount();
            case CAT: return getCatCount();
            case RABBIT: return getRabbitCount();
        }
        return 0;
    }

    public void setCount(Piece piece, int value) {
        switch(piece) {
            case ELEPHANT: setElephantCount(value); break;
            case CAMEL: setCamelCount(value); break;
            case HORSE: setHorseCount(value); break;
            case DOG: setDogCount(value); break;
            case CAT: setCatCount(value); break;
            case RABBIT: setRabbitCount(value); break;
        }
    }

    public int getElephantCount() {
        return elephantCount;
    }

    public void setElephantCount(int elephantCount) {
        this.elephantCount = elephantCount;
    }

    public int getCamelCount() {
        return camelCount;
    }

    public void setCamelCount(int camelCount) {
        this.camelCount = camelCount;
    }

    public int getHorseCount() {
        return horseCount;
    }

    public void setHorseCount(int horseCount) {
        this.horseCount = horseCount;
    }

    public int getDogCount() {
        return dogCount;
    }

    public void setDogCount(int dogCount) {
        this.dogCount = dogCount;
    }

    public int getCatCount() {
        return catCount;
    }

    public void setCatCount(int catCount) {
        this.catCount = catCount;
    }

    public int getRabbitCount() {
        return rabbitCount;
    }

    public void setRabbitCount(int rabbitCount) {
        this.rabbitCount = rabbitCount;
    }

    public void setElephant(long elephant) {
        this.elephant = elephant;
    }

    public void setCamel(long camel) {
        this.camel = camel;
    }

    public void setHorses(long horses) {
        this.horses = horses;
    }

    public void setDogs(long dogs) {
        this.dogs = dogs;
    }

    public void setCats(long cats) {
        this.cats = cats;
    }

    public void setRabbits(long rabbits) {
        this.rabbits = rabbits;
    }

    public long getFrozen() {
        return frozen;
    }

    public void setFrozen(long frozen) {
        this.frozen = frozen;
    }

    public void set(Piece type, long value) {
        if(type == ELEPHANT) setElephant(value);
        if(type == CAMEL) setCamel(value);
        if(type == HORSE) setHorses(value);
        if(type == DOG) setDogs(value);
        if(type == CAT) setCats(value);
        if(type == RABBIT) setRabbits(value);
    }

    public void clear() {
        setElephant(0);
        setCamel(0);
        setHorses(0);
        setDogs(0);
        setCats(0);
        setRabbits(0);
        setFrozen(0);
    }

    public void clear(Square location) {
        long clearMask = ~(1l << location.ordinal());
        setElephant(getElephant() & clearMask);
        setCamel(getCamel() & clearMask);
        setHorses(getHorses() & clearMask);
        setDogs(getDogs() & clearMask);
        setCats(getCats() & clearMask);
        setRabbits(getRabbits() & clearMask);
    }

    public long getElephant() {
        return elephant;
    }

    public long getCamel() {
        return camel;
    }

    public long getHorses() {
        return horses;
    }

    public long getDogs() {
        return dogs;
    }

    public long getCats() {
        return cats;
    }

    public long getRabbits() {
        return rabbits;
    }

    public boolean isEmpty() {
        return getAllPieces() == 0;
    }

    public boolean isEmpty(Square location) {
        long squareMask = 1l << location.ordinal();
        return (getAllPieces() & squareMask) == 0;
    }

    public Piece get(Square location) {
        long squareMask = 1l << location.ordinal();
        if((getElephant() & squareMask) != 0) return ELEPHANT;
        if((getCamel() & squareMask) != 0) return CAMEL;
        if((getHorses() & squareMask) != 0) return HORSE;
        if((getDogs() & squareMask) != 0) return DOG;
        if((getCats() & squareMask) != 0) return CAT;
        if((getRabbits() & squareMask) != 0) return RABBIT;
        return null;
    }

    public long get(Piece type) {
        if(type == ELEPHANT) return getElephant();
        if(type == CAMEL) return getCamel();
        if(type == HORSE) return getHorses();
        if(type == DOG) return getDogs();
        if(type == CAT) return getCats();
        if(type == RABBIT) return getRabbits();
        return 0;
    }

    public static long getNeighborMask(long squareMask) {
        return (squareMask & ~Bits.fileMask[0]) >>> 1 |
                (squareMask & ~Bits.rankMask[7]) << 8 |
                (squareMask & ~Bits.fileMask[7]) << 1 |
                (squareMask & ~Bits.rankMask[0]) >>> 8;
    }

    public static long getNeighborMask(Square location) {
        return getNeighborMask(1l << location.ordinal());
    }

    public long getBullies(Square location, PieceState enemy) {
        if(isEmpty(location)) return 0;

        Piece piece = get(location);
        if(piece == ELEPHANT) return 0;

        return getAllPiecesAbove(piece) & getNeighborMask(location);
    }

    public long getAllPiecesAbove(Piece piece) {
        if(piece == ELEPHANT) return 0;
        long pieces = 0;
        switch(piece) {
            case RABBIT:
                pieces |= getCats();
            case CAT:
                pieces |= getDogs();
            case DOG:
                pieces |= getHorses();
            case HORSE:
                pieces |= getCamel();
            case CAMEL:
                pieces |= getElephant();
        }
        return pieces;
    }

    public long getAllPiecesBelow(Piece piece) {
        if(piece == RABBIT) return 0;
        long pieces = 0;
        switch(piece) {
            case ELEPHANT:
                pieces |= getHorses();
            case CAMEL:
                pieces |= getHorses();
            case HORSE:
                pieces |= getDogs();
            case DOG:
                pieces |= getCats();
            case CAT:
                pieces |= getRabbits();
        }
        return pieces;
    }

    public long getAllPieces() {
        return getElephant() | getCamel() | getHorses() | getDogs() | getCats() | getRabbits();
    }

    public static PieceState newEmptyInstance() {
        return new PieceState();
    }
}
