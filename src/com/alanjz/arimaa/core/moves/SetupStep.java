package com.alanjz.arimaa.core.moves;

import com.alanjz.arimaa.core.Piece;
import com.alanjz.arimaa.core.Square;

public class SetupStep extends Step {
    private final Piece piece;

    public SetupStep(Piece piece, Square target) {
        super(null, target);
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}
