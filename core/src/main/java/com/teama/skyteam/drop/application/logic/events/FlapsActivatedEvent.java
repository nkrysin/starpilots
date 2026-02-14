package com.teama.skyteam.drop.application.logic.events;

public class FlapsActivatedEvent implements GameEvent{
    private final int switchIndex;
    private final int state;

    public FlapsActivatedEvent(int switchIndex, int state) {
        this.switchIndex = switchIndex;
        this.state = state;
    }

    public int getSwitchIndex() {
        return switchIndex;
    }

    public int getState() {
        return state;
    }
}
