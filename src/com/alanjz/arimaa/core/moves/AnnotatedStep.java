package com.alanjz.arimaa.core.moves;

import com.alanjz.arimaa.core.Piece;
import com.alanjz.arimaa.core.Team;
import com.alanjz.arimaa.core.moves.effects.StepEffect;

public class AnnotatedStep {

    public enum Type {
        SETUP,
        MOVE,
        PUSH,
        PULL,
        ILLEGAL
    }

    private final Type type;
    private final StepEffect effect;
    private final Piece piece;
    private final Team team;
    private final Step step;
    private String comment;

    public AnnotatedStep(Type type, StepEffect effect, String comment, Piece piece, Team team, Step step) {
        this.type = type;
        this.effect = effect;
        this.comment = comment;
        this.piece = piece;
        this.team = team;
        this.step = step;
    }

    public AnnotatedStep(Type type, String comment, Piece piece, Team team, Step step) {
        this(type, StepEffect.NONE, comment, piece, team, step);
    }

    public AnnotatedStep(Type type, StepEffect effect, Piece piece, Team team, Step step) {
        this(type, effect, null, piece, team, step);
    }

    public AnnotatedStep(Type type, Piece piece, Team team, Step step) {
        this(type, StepEffect.NONE, null, piece, team, step);
    }

    public boolean isLegal() {
        return getType() != Type.ILLEGAL;
    }

    public boolean isSetup() {
        return getType() == Type.SETUP;
    }

    public boolean isBully() {
        return isPush() || isPull();
    }

    public boolean isPush() {
        return getType() == Type.PUSH;
    }

    public boolean isPull() {
        return getType() == Type.PULL;
    }

    public boolean hasComment() {
        return getComment() != null;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Type getType() {
        return type;
    }

    public StepEffect getEffect() {
        return effect;
    }

    public Piece getPiece() {
        return piece;
    }

    public Team getTeam() {
        return team;
    }

    public Step getStep() {
        return step;
    }
}
