package com.teama.skyteam.drop.spiel.logik.events;

public class FlapsActivatedEvent implements GameEvent{
    private final int schalterIndex;
    private final int state;

    public FlapsActivatedEvent(int schalterIndex, int state) {
        this.schalterIndex = schalterIndex;
        this.state = state;
    }

    public int getSchalterIndex() {
        return schalterIndex;
    }

    public int getState() {
        return state;
    }
}
