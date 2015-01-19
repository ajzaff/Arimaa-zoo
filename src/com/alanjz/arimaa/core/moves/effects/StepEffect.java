package com.alanjz.arimaa.core.moves.effects;

public class StepEffect {

    public static StepEffect NONE = new StepEffect();

    private long frozenSourceMask; // abandoning my own piece, making it frozen.
    private long unfrozenSourceMask; // letting an enemy hostage go.
    private long frozenTargetMask; // freezing an enemy piece at the target.
    private long unfrozenTargetMask; // coming to the aid of a friendly piece, making it unfrozen.
    private boolean capture; // does this move capture a piece, or itself on a trap?

    public StepEffect(long frozenSourceMask, long unfrozenSourceMask,
                      long frozenTargetMask, long unfrozenTargetMask, boolean capture) {
        this.frozenSourceMask = frozenSourceMask;
        this.unfrozenSourceMask = unfrozenSourceMask;
        this.frozenTargetMask = frozenTargetMask;
        this.unfrozenTargetMask = unfrozenTargetMask;
        this.capture = capture;
    }

    protected StepEffect() {
        this(0, 0, 0, 0, false);
    }

    public long getFrozenSourceMask() {
        return frozenSourceMask;
    }

    public void setFrozenSourceMask(long frozenSourceMask) {
        this.frozenSourceMask = frozenSourceMask;
    }

    public long getUnfrozenSourceMask() {
        return unfrozenSourceMask;
    }

    public void setUnfrozenSourceMask(long unfrozenSourceMask) {
        this.unfrozenSourceMask = unfrozenSourceMask;
    }

    public long getFrozenTargetMask() {
        return frozenTargetMask;
    }

    public void setFrozenTargetMask(long frozenTargetMask) {
        this.frozenTargetMask = frozenTargetMask;
    }

    public long getUnfrozenTargetMask() {
        return unfrozenTargetMask;
    }

    public void setUnfrozenTargetMask(long unfrozenTargetMask) {
        this.unfrozenTargetMask = unfrozenTargetMask;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public boolean isCapture() {
        return capture;
    }
}
