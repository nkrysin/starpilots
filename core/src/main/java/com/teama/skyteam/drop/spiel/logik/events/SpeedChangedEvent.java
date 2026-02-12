package com.teama.skyteam.drop.spiel.logik.events;

public class SpeedChangedEvent implements GameEvent {
    int steps;

    public SpeedChangedEvent(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
