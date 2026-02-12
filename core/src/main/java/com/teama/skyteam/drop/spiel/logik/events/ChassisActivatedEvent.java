package com.teama.skyteam.drop.spiel.logik.events;

public class ChassisActivatedEvent implements GameEvent {
    private final int schalterIndex;
    private final int count;

    public ChassisActivatedEvent(int schalterIndex, int count) {
        this.schalterIndex = schalterIndex;
        this.count = count;
    }

    public int getSchalterIndex() { return schalterIndex; }
    public int getAnzahl() { return count; }

}
