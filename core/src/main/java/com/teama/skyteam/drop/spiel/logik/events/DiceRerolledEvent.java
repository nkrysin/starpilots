package com.teama.skyteam.drop.spiel.logik.events;

import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.List;

public class DiceRerolledEvent implements GameEvent {
    private final Spieler spieler;
    private final List<Wuerfel> newDice;

    public DiceRerolledEvent(Spieler spieler, List<Wuerfel> newDice) {
        this.spieler = spieler;
        this.newDice = newDice;
    }

    public Spieler getSpieler() { return spieler; }
    public List<Wuerfel> getNewDice() { return newDice; }

}
