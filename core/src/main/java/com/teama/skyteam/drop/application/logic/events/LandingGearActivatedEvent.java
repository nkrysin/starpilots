package com.teama.skyteam.drop.application.logic.events;

public class LandingGearActivatedEvent implements GameEvent {
    private final int switchIndex;
    private final int count;

    public LandingGearActivatedEvent(int switchIndex, int count) {
        this.switchIndex = switchIndex;
        this.count = count;
    }

    public int getSwitchIndex() { return switchIndex; }
    public int getCount() { return count; }

}
