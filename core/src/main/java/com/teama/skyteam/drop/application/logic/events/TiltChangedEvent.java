package com.teama.skyteam.drop.application.logic.events;

public class TiltChangedEvent implements GameEvent {
    private final float targetRotation;

    public TiltChangedEvent(float targetRotation) {
        this.targetRotation = targetRotation;
    }

    public float getTargetRotation() { return targetRotation; }

}
