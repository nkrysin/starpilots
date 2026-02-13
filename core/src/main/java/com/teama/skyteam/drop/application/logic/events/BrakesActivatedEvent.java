package com.teama.skyteam.drop.application.logic.events;


public class BrakesActivatedEvent implements GameEvent {
    private final int schalterIndex; // переключатель (0, 1, 2)
    private final int indicatorValue; // На какое значение переместить индикатор

    public BrakesActivatedEvent(int schalterIndex, int indicatorValue) {
        this.schalterIndex = schalterIndex;
        this.indicatorValue = indicatorValue;
    }

    public int getSchalterIndex() { return schalterIndex; }
    public int getIndicatorValue() { return indicatorValue; }

}
