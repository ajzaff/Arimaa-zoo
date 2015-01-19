package com.alanjz.arimaa.core.moves.effects;

public class StepEffectBuilder {

    private long frozenSourceMask;
    private long unfrozenSourceMask;
    private long frozenTargetMask;
    private long unfrozenTargetMask;
    private boolean capture;

    public boolean isCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
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

    public StepEffect build() {
        return new StepEffect(getFrozenSourceMask(), getFrozenTargetMask(),
                getUnfrozenSourceMask(), getUnfrozenTargetMask(), isCapture());
    }
}
