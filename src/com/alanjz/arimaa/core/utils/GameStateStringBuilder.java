package com.alanjz.arimaa.core.utils;

import com.alanjz.arimaa.core.Piece;
import com.alanjz.arimaa.core.Square;
import com.alanjz.arimaa.core.position.GameState;

public final class GameStateStringBuilder {
    public static String toString(GameState gs) {
        StringBuilder builder = new StringBuilder(" +-----------------+\n");
        for(int i=7; i >= 0; i--) {
            builder.append(i+1);
            builder.append("| ");
            for(int j=0; j < 8; j++) {
                int num = 8 * i + j;
                Square location = Square.fromInteger(num);
                Piece piece = gs.getGoldPieces().get(location);
                if(piece == null) {
                    piece = gs.getSilverPieces().get(location);
                    if(piece == null) {
                        if(location.isTrap()) {
                            builder.append("x ");
                        }
                        else {
                            builder.append("  ");
                        }
                    }
                    else {
                        builder.append(Character.toLowerCase(pieceToChar(piece)));
                        builder.append(' ');
                    }
                }
                else {
                    builder.append(pieceToChar(piece));
                    builder.append(' ');
                }
                if((num+1) % 8 == 0) {
                    builder.append("|\n");
                }
            }
        }
        builder.append(" +-----------------+\n");
        builder.append("   a b c d e f g h");
        return builder.toString();
    }

    protected static char pieceToChar(Piece piece) {
        switch(piece) {
            case ELEPHANT: return 'E';
            case CAMEL: return 'M';
            case HORSE: return 'H';
            case DOG: return 'D';
            case CAT: return 'C';
            case RABBIT: return 'R';
        }
        return ' ';
    }
}
