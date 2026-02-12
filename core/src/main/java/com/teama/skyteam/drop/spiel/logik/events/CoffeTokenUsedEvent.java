package com.teama.skyteam.drop.spiel.logik.events;

import com.teama.skyteam.drop.spieler.Spieler;

public class CoffeTokenUsedEvent implements GameEvent {
    private final Spieler spieler;
    private final int wuerfelIndex;
    private final int newValue;
    private final int remainingTokens;

    public CoffeTokenUsedEvent(Spieler spieler, int wuerfelIndex, int newValue, int remainingTokens) {
        this.spieler = spieler;
        this.wuerfelIndex = wuerfelIndex;
        this.newValue = newValue;
        this.remainingTokens = remainingTokens;
    }

    public Spieler getSpieler() { return spieler; }
    public int getWuerfelIndex() { return wuerfelIndex; }
    public int getNewValue() { return newValue; }
    public int getRemainingTokens() { return remainingTokens; }
}

