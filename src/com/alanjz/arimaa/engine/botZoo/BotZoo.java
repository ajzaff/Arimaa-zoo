package com.alanjz.arimaa.engine.botZoo;

import com.alanjz.arimaa.core.IllegalGameActionException;
import com.alanjz.arimaa.core.Team;
import com.alanjz.arimaa.core.moves.Step;
import com.alanjz.arimaa.core.position.GameState;
import com.alanjz.arimaa.core.utils.GameStateStringBuilder;
import com.alanjz.arimaa.engine.AEIEngine;
import com.alanjz.arimaa.engine.EngineCommand;

public class BotZoo extends AEIEngine {

    private GameState gameState;

    public BotZoo() {
        super("Zoo", "Alan Zaffetti", "1.0.1 UNRELEASED");
        gameState = GameState.newEmptyInstance();

        addEngineCommand(new EngineCommand("print", 0, (params) -> {
            // print
            System.out.println(GameStateStringBuilder.toString(gameState));
        }));
    }

    public static void main(String[] args) {
        new BotZoo();
    }

    @Override
    public void stop() {

    }

    @Override
    public void newGame() {
        getGameState().reset();
    }

    @Override
    public void setPosition(Team activeTeam, String allPieces) {

    }

    @Override
    public void makeMove(Step[] steps) {
        try {
            for (Step step : steps) {
                gameState.getMoveHandler().performStep(step);
            }
            gameState.getMoveHandler().endTurn();
        }
        catch (IllegalGameActionException e) {
            log(LogLevel.ERROR, e.getMessage());
        }
    }

    @Override
    public void resign() {

    }

    @Override
    public void takeBackMove() {

    }

    @Override
    public Team getActiveTeam() {
        return getGameState().getActiveTeam();
    }

    protected GameState getGameState() {
        return gameState;
    }
}