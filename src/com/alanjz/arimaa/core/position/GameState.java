package com.alanjz.arimaa.core.position;

import com.alanjz.arimaa.core.Team;
import com.alanjz.arimaa.core.moves.MoveHandler;
import com.alanjz.arimaa.core.utils.GameStateStringBuilder;

public class GameState {
    private final PieceState goldPieces;
    private final PieceState silverPieces;
    private final MoveHandler moveHandler;

    GameState(PieceState goldPieces, PieceState silverPieces) {
        this.goldPieces = goldPieces;
        this.silverPieces = silverPieces;
        this.moveHandler = new MoveHandler(goldPieces, silverPieces);
    }

    public void reset() {
        getGoldPieces().clear();
        getSilverPieces().clear();
        getMoveHandler().reset();
    }

    public Team getActiveTeam() {
        return getMoveHandler().getActiveTeam();
    }

    public PieceState getGoldPieces() {
        return goldPieces;
    }

    public PieceState getSilverPieces() {
        return silverPieces;
    }

    public MoveHandler getMoveHandler() {
        return moveHandler;
    }

    public static GameState newEmptyInstance() {
        return new GameState(PieceState.newEmptyInstance(), PieceState.newEmptyInstance());
    }

    @Override
    public String toString() {
        return GameStateStringBuilder.toString(this);
    }
}
