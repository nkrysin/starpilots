package com.teama.skyteam.drop.application.logic.events;


public class BrakesActivatedEvent implements GameEvent {
    private final int switchIndex; // переключатель (0, 1, 2)
    private final int indicatorValue; // На какое значение переместить индикатор

    public BrakesActivatedEvent(int switchIndex, int indicatorValue) {
        this.switchIndex = switchIndex;
        this.indicatorValue = indicatorValue;
    }

    public int getSwitchIndex() { return switchIndex; }
    public int getIndicatorValue() { return indicatorValue; }

}
