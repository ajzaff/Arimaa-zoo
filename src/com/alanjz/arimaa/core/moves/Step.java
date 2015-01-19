package com.alanjz.arimaa.core.moves;

import com.alanjz.arimaa.core.Piece;
import com.alanjz.arimaa.core.Square;
import com.alanjz.arimaa.core.Team;
import com.alanjz.arimaa.engine.AEIEngine;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Step {

    private final Square source;
    private final Square target;

    Step(Square source, Square target) {
        this.source = source;
        this.target = target;
    }

    public boolean hasSource() {
        return getSource() != null;
    }

    public Square getSource() {
        return source;
    }

    public Square getTarget() {
        return target;
    }

    public static Step getStep(Square source, Square target) {
        return new Step(source, target);
    }

    public static Step getStep(Piece piece, Square target) {
        return new SetupStep(piece, target);
    }

    private static final Pattern stepPattern =
            Pattern.compile("([EMHDCRemhdcr])([abcdefgh][12345678])([neswx])?");

    public static Step fromString(String step, Team activeTeam) throws IllegalArgumentException {
        Matcher match = stepPattern.matcher(step);
        if(match.matches() && match.groupCount() >= 2) {
            Piece piece = Piece.fromString(match.group(1));
            Team team = Team.fromString(match.group(1));

            if(team != activeTeam) {
                throw new IllegalArgumentException("It is not " + activeTeam.getOpponent().name() + "'s turn.");
            }

            Square square1 = Square.fromString(match.group(2));

            if(match.group(3) == null) {
                return getStep(piece, square1);
            }

            Direction d = Direction.fromString(match.group(3));
            AEIEngine.log(AEIEngine.LogLevel.DEBUG, d.name());
            Square square2;
            if(d == Direction.NONE) {
                square2 = square1.getAdjacentTrap();
                if(square2 == null) {
                    throw new IllegalArgumentException("Illegal capture at " + square1.name() + ".");
                }
            }
            else {
                square2 = square1.add(d);
                if(square2 == null) {
                    throw new IllegalArgumentException("Illegal step from " + square1.name() + " " + d.name() + ".");
                }
            }

            return getStep(square1, square2);
        }

        throw new IllegalArgumentException("Malformed step `" + step + "'.");
    }
}
