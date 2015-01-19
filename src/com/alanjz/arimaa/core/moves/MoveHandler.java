package com.alanjz.arimaa.core.moves;

import com.alanjz.arimaa.core.IllegalGameActionException;
import com.alanjz.arimaa.core.Piece;
import com.alanjz.arimaa.core.Square;
import com.alanjz.arimaa.core.Team;
import com.alanjz.arimaa.core.moves.effects.StepEffectBuilder;
import com.alanjz.arimaa.core.numerics.Bits;
import com.alanjz.arimaa.core.position.PieceState;
import com.alanjz.arimaa.engine.AEIEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.alanjz.arimaa.core.Team.GOLD;
import static com.alanjz.arimaa.core.Team.SILVER;

public class MoveHandler {

    private Team activeTeam;
    private int turnNumber;
    private final List<AnnotatedStep> turnMoves;
    private final Stack<AnnotatedStep> annotatedSteps;
    private final Stack<AnnotatedStep> undoneMoves;
    private final PieceState goldPieces;
    private final PieceState silverPieces;

    public MoveHandler(PieceState goldPieces, PieceState silverPieces) {
        this.activeTeam = GOLD;
        this.goldPieces = goldPieces;
        this.silverPieces = silverPieces;
        this.annotatedSteps = new Stack<AnnotatedStep>();
        this.undoneMoves = new Stack<AnnotatedStep>();
        this.turnMoves = new ArrayList<AnnotatedStep>(4);
        this.turnNumber = 1;
    }

    protected AnnotatedStep getAnnotatedStep(Step step) {
        AnnotatedStep lastMove = getLastMove();
        AnnotatedStep.Type stepType;
        PieceState teamPieces = getActivePieces(); // which team's pieces are we moving?
        Piece sourcePiece = (step.hasSource()? teamPieces.get(step.getSource()) : null);
        boolean isActivePiece = teamPieces == getActivePieces();

        // ~~~~~~~~ TARGET CHECKING ~~~~~~~~
        // The target square must be empty.
        // If the target is not empty, this move is illegal.
        if(!getGoldPieces().isEmpty(step.getTarget()) || !getSilverPieces().isEmpty(step.getTarget())) {
            return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                    "move target is not empty.", sourcePiece, activeTeam, step);
        }

        // ~~~~~~~~ SETUP MOVE CHECKING ~~~~~~~~
        // Each team gets a chance to set up their pieces before play.
        // Gold sets up first, and then silver follows.
        // The setup is done exactly the same way as making any other move.
        // Check if it is time to set up and if so, do it.
        try {
            if(setup(step)) {
                SetupStep setupMove = (SetupStep) step;
                return new AnnotatedStep(AnnotatedStep.Type.SETUP, setupMove.getPiece(), activeTeam, step);
            }
        }
        catch (IllegalGameActionException e) {
            return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                    e.getMessage(), null, activeTeam, step);
        }

        // ~~~~~~~~ SOURCE CHECKING ~~~~~~~~
        // Test if the move is legal.
        // This effectively checks the origin to make sure something is there.
        // If no piece is at the origin, this is an illegal move make.
        if(sourcePiece == null) {
            teamPieces = getInactivePieces();
            sourcePiece = teamPieces.get(step.getSource());
            if(sourcePiece == null) {
                return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                        "source square is empty.", null, activeTeam, step);
            }
        }
        // ~~~~~~~~ TURN LENGTH CHECK ~~~~~~~~
        // A player is allowed a maximum of 4 moves per turn.
        if(getTurnMoves().size() >= 4) {
            return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                    "not enough steps left in this turn.", sourcePiece, activeTeam, step);
        }
        // ~~~~~~~~ CARDINAL CHECKING ~~~~~~~~
        // Arimaa allows for only cardinal direction moves.
        int stepDistance = step.getTarget().ordinal() - step.getSource().ordinal();
        if(Math.abs(stepDistance) != 8 && Math.abs(stepDistance) != 1) {
            return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                    "can't move fromInteger " + step.getSource().name() + " to " + step.getTarget().name() + " in one move.",
                    sourcePiece, activeTeam, step);
        }
        // ~~~~~~~~ FROZEN CHECKING ~~~~~~~~
        // Test if a piece is frozen before moving.
        // If a piece is frozen, it cannot move.
        long sourceMask = 1l << step.getSource().ordinal();
        if((teamPieces.getFrozen() & sourceMask) != 0) {
            return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                    "the piece on " + step.getSource().name() + " is frozen.", sourcePiece, activeTeam, step);
        }
        // ~~~~~~~~ RABBIT VALIDATION ~~~~~~~~
        // Rabbits can only move forward.
        if(sourcePiece == Piece.RABBIT && isActivePiece) {
            if((getActiveTeam() == GOLD && stepDistance == -8) ||
                    (getActiveTeam() == SILVER && stepDistance == 8)) {
                return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                        "rabbits cannot step backwards.", sourcePiece, activeTeam, step);
            }
        }
        // ~~~~~~~~ PUSH COMPLETION CHECKING ~~~~~~~~
        // If the last move was a push, this move must move a pushing piece.
        // If it does not move a valid pushing piece to the last move origin, it is illegal.
        boolean hasLastMove = lastMove != null;
        if(hasLastMove && lastMove.isPush() &&
                (!sourcePiece.canBully(lastMove.getPiece()) ||
                        !step.getTarget().equals(lastMove.getStep().getSource()))) {
            return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                    "must complete push or pull.", sourcePiece, activeTeam, step);
        }
        // ~~~~~~~~ PUSH/PULL VALIDATION ~~~~~~~~
        // If this move uses non active pieces, it is either a push or pull move.
        // If it is a pull move, then the last move had its origin at this move's target.
        // Additionally, the last piece must be able to bully this piece.
        // If it is a push move, there must be an adjacent bully.
        // If the is a move which does not neatly fit either of these, it is illegal.
        boolean lastMoveDepartsTarget = hasLastMove && lastMove.getStep().getSource() == step.getTarget();
        boolean canBully = hasLastMove && lastMove.getPiece().canBully(sourcePiece);
        boolean adjacentBullies = teamPieces.getBullies(step.getSource(), getActivePieces()) != 0;
        boolean isPush = false;
        boolean isPull = false;
        if(!isActivePiece) {
            if(lastMoveDepartsTarget && canBully) {
                isPull = true;
            }
            else if(adjacentBullies) {
                isPush = true;
            }
            else {
                return new AnnotatedStep(AnnotatedStep.Type.ILLEGAL,
                        "Cannot move " + getActiveTeam().getOpponent() + " piece without push or pull.",
                        sourcePiece, activeTeam, step);
            }
        }

        // Decide the movement type
        if(isPush) {
            stepType = AnnotatedStep.Type.PUSH;
        }
        else if(isPull) {
            stepType = AnnotatedStep.Type.PULL;
        }
        else {
            stepType = AnnotatedStep.Type.MOVE;
        }

        StepEffectBuilder effectBuilder = new StepEffectBuilder();

        // ~~~~~~~ UNFROZEN SOURCE PIECES ~~~~~~~
        PieceState otherPieces = (teamPieces == getActivePieces()? getInactivePieces() : getActivePieces());
        long otherPiecesBelowSource = otherPieces.getAllPiecesBelow(sourcePiece);
        long otherFrozen = otherPieces.getFrozen() & otherPiecesBelowSource;
        long sourceNeighborMask = PieceState.getNeighborMask(step.getSource());
        long otherSourceFrozen = otherFrozen & sourceNeighborMask;
        long unfrozenSourceMask = 0;
        while (otherSourceFrozen != 0) {
            int lsb = Bits.bitScanForward(otherSourceFrozen);
            long squareMask = 1l << lsb;
            long neighbors = PieceState.getNeighborMask(squareMask);
            Piece piece = otherPieces.get(Square.fromInteger(lsb));
            long teamFreezers = teamPieces.getAllPiecesAbove(piece);
            if((neighbors & teamFreezers) == 0) {
                unfrozenSourceMask |= squareMask;
            }
            otherSourceFrozen &= (otherSourceFrozen-1);
        }
        effectBuilder.setUnfrozenSourceMask(unfrozenSourceMask);

        // ~~~~~~~ UNFROZEN TARGET PIECES ~~~~~~~
        long teamFrozen = teamPieces.getFrozen();
        long targetNeighborMask = PieceState.getNeighborMask(step.getTarget());
        long teamTargetFrozen = teamFrozen & targetNeighborMask;
        effectBuilder.setUnfrozenTargetMask(teamTargetFrozen);

        // ~~~~~~~ FROZEN TARGET PIECES ~~~~~~~
        long unfrozenOtherTargetPieces = otherPiecesBelowSource & targetNeighborMask & ~otherPieces.getFrozen();
        long frozenTargetMask = 0;
        long allOtherPieces = otherPieces.getAllPieces();
        while(unfrozenOtherTargetPieces != 0) {
            int lsb = Bits.bitScanForward(unfrozenOtherTargetPieces);
            long squareMask = 1l << lsb;
            long neighbors = PieceState.getNeighborMask(squareMask);
            if((neighbors & allOtherPieces) == 0) {
                frozenTargetMask |= squareMask;
            }
            unfrozenOtherTargetPieces &= (unfrozenOtherTargetPieces-1);
        }
        effectBuilder.setFrozenTargetMask(frozenTargetMask);

        // ~~~~~~~ FROZEN SOURCE PIECES ~~~~~~~
        long teamSourcePieces = teamPieces.getAllPieces() & sourceNeighborMask;
        long sourceFrozenMask = 0;
        while(teamSourcePieces != 0) {
            int lsb = Bits.bitScanForward(teamSourcePieces);
            Piece piece = teamPieces.get(Square.fromInteger(lsb));
            long squareMask = 1l << lsb;
            long neighbors = PieceState.getNeighborMask(squareMask);
            long otherPiecesGraterThan = otherPieces.getAllPiecesAbove(piece);
            if((neighbors & otherPiecesGraterThan) != 0) {
                sourceFrozenMask |= squareMask;
            }
            teamSourcePieces &= (teamSourcePieces-1);
        }
        effectBuilder.setFrozenSourceMask(sourceFrozenMask);

        // ~~~~~~~ CAPTURE CHECKER ~~~~~~~
        boolean targetIsTrap = step.getTarget().isTrap();
        if(!isActivePiece && targetIsTrap) {
            if((targetNeighborMask & allOtherPieces) == 0) {
                effectBuilder.setCapture(true);
            }
        }

        // Return the annotated step.
        return new AnnotatedStep(stepType, effectBuilder.build(), sourcePiece, activeTeam, step);
    }

    private boolean setup(Step step) throws IllegalGameActionException {
        boolean isSourcelessMove = !step.hasSource();
        if(isGoldSetupTurn() || isSilverSetupTurn()) {
            if(!isSourcelessMove) {
                throw new IllegalGameActionException("moves cannot be played on a setup turn.");
            }
            SetupStep setupMove = (SetupStep) step;
            Piece piece = setupMove.getPiece();
            int count = getActivePieces().getCount(piece);
            int maximumCount = PieceState.getMaximumCount(piece);
            if(count >= maximumCount) {
                throw new IllegalGameActionException("maximum " + piece.name() + " count for " + getActiveTeam().name() + ".");
            }
            return true;
        }
        else if(isSourcelessMove) {
            throw new IllegalGameActionException("move has target square `" + step.getSource() + "' but no source.");
        }
        return false;
    }

    public boolean canUnmakeStep() {
        return hasLastStep();
    }

    public void performStep(Step step) throws IllegalGameActionException {
        AnnotatedStep annotatedStep = getAnnotatedStep(step);
        if(!annotatedStep.isLegal()) {
            String comment = "Illegal step by " + getActiveTeam().name() +
                    (annotatedStep.hasComment()?
                        ": " + annotatedStep.getComment() : ".");
            throw new IllegalGameActionException(comment);
        }
        long targetMask = 1l << step.getTarget().ordinal();
        if(annotatedStep.isBully()) {
            getInactivePieces().clear(step.getSource());
            if(!annotatedStep.getEffect().isCapture()) {
                long inactivePiecesPiece = getInactivePieces().get(annotatedStep.getPiece());
                getInactivePieces().set(annotatedStep.getPiece(), inactivePiecesPiece | targetMask);

                // Update piece count.
                int count = getInactivePieces().getCount(annotatedStep.getPiece());
                getInactivePieces().setCount(annotatedStep.getPiece(), count - 1);
            }
        }
        else if(annotatedStep.isSetup()) {
            Piece piece = annotatedStep.getPiece();
            long pieceMask = getActivePieces().get(piece);
            getActivePieces().set(piece, pieceMask | targetMask);
            getActivePieces().setCount(piece, getActivePieces().getCount(piece) + 1);
        }
        else {
            long activePiecesPiece = getActivePieces().get(annotatedStep.getPiece());
            getActivePieces().clear(step.getSource());
            getActivePieces().set(annotatedStep.getPiece(), activePiecesPiece | targetMask);
        }

        // Update frozen.
        if(!annotatedStep.isSetup()) {
            getActivePieces().setFrozen(getActivePieces().getFrozen() | annotatedStep.getEffect().getFrozenSourceMask());
            getActivePieces().setFrozen(getActivePieces().getFrozen() ^ annotatedStep.getEffect().getUnfrozenTargetMask());
            getInactivePieces().setFrozen(getInactivePieces().getFrozen() | annotatedStep.getEffect().getFrozenTargetMask());
            getInactivePieces().setFrozen(getInactivePieces().getFrozen() ^ annotatedStep.getEffect().getUnfrozenSourceMask());
        }

        // Add annotated move to step stack.
        getTurnMoves().add(annotatedStep);
    }

    public void unmakeStep() throws IllegalGameActionException {
    }

    public void remakeStep() throws IllegalGameActionException {
    }

    public void reset() {
        getAnnotatedSteps().clear();
        getTurnMoves().clear();
        truncateGame();
    }

    public void truncateGame() {
        undoneMoves.clear();
    }

    public boolean hasLastStep() {
        return getLastMove() != null;
    }

    public AnnotatedStep getLastMove() {
        boolean turnEmpty = getTurnMoves().isEmpty();
        boolean annotatedEmpty = getAnnotatedSteps().empty();
        if(turnEmpty && annotatedEmpty) return null;
        if(turnEmpty) return getAnnotatedSteps().peek();
        return getTurnMoves().get(getTurnMoves().size() - 1);
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public String getTurnName() {
        return "" + getTurnNumber() + getActiveTeam().toChar();
    }

    public PieceState getActivePieces() {
        return getActiveTeam() == GOLD? getGoldPieces() : getSilverPieces();
    }

    public PieceState getInactivePieces() {
        return getActiveTeam() == SILVER? getGoldPieces() : getSilverPieces();
    }

    public PieceState getGoldPieces() {
        return goldPieces;
    }

    public PieceState getSilverPieces() {
        return silverPieces;
    }

    protected Stack<AnnotatedStep> getUndoneMoves() {
        return undoneMoves;
    }

    protected Stack<AnnotatedStep> getAnnotatedSteps() {
        return annotatedSteps;
    }

    public List<AnnotatedStep> getTurnMoves() {
        return turnMoves;
    }

    public void setActiveTeam(Team activeTeam) {
        this.activeTeam = activeTeam;
    }

    public Team getActiveTeam() {
        return activeTeam;
    }

    public boolean isGoldSetupTurn() {
        return getTurnNumber() == 1 && getActiveTeam() == GOLD;
    }

    public boolean isSilverSetupTurn() {
        return getTurnNumber() == 1 && getActiveTeam() == SILVER;
    }

    public void endTurn() throws IllegalGameActionException {

        boolean acceptableAction = hasLastStep() &&
                getLastMove().getTeam() == getActiveTeam();

        if(!acceptableAction) {
            throw new IllegalGameActionException(getActiveTeam().name() + " must make a move before ending turn.");
        }

        if(getLastMove().isBully()) {
            throw new IllegalGameActionException(getActiveTeam().name() + " must complete before ending turn.");
        }

        // Check if setup is legal.
        if(isGoldSetupTurn() && !getGoldPieces().hasAllPieces() ||
                isSilverSetupTurn() && !getSilverPieces().hasAllPieces()) {
            throw new IllegalGameActionException(getActiveTeam().name() + " setup must use all 16 pieces.");
        }

        // Move all turn moves onto the move list.
        for(AnnotatedStep m : getTurnMoves()) {
            getAnnotatedSteps().push(m);
        }
        clearTurnMoves();

        // Update turn number.
        if(getActiveTeam() == SILVER) {
            turnNumber++;
        }

        // Flip the active team.
        setActiveTeam(getActiveTeam() == GOLD? SILVER : GOLD);
    }

    protected void clearTurnMoves() {
        getTurnMoves().clear();
    }
}
