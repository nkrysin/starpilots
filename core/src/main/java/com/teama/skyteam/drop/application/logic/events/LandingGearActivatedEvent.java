package com.teama.skyteam.drop.application.logic.events;

public class LandingGearActivatedEvent implements GameEvent {
    private final int schalterIndex;
    private final int count;

    public LandingGearActivatedEvent(int schalterIndex, int count) {
        this.schalterIndex = schalterIndex;
        this.count = count;
    }

    public int getSchalterIndex() { return schalterIndex; }
    public int getAnzahl() { return count; }

}
