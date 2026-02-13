package com.teama.skyteam.drop.application.logic.events;

public class SpeedChangedEvent implements GameEvent {
    int steps;

    public SpeedChangedEvent(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
