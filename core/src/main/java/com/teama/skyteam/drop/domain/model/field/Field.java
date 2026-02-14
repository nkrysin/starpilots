package com.teama.skyteam.drop.domain.model.field;

import com.teama.skyteam.drop.domain.model.tokens.Dice;

/**
 * Die Klasse Field repräsentiert ein Spielfeld, auf dem ein Würfel platziert werden kann.
 * Jedes Field hat eine Eigenschaft, ob es belegt ist, und speichert den platzierten Würfel.
 */
public class Field {

    private boolean isOccupied;
    private Dice dice;

    public Field() {
        this.isOccupied = false;
        this.dice = null;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Dice getDice() {
        return dice;
    }

    /**
     * Platziert einen Würfel auf dem Field, falls es nicht belegt ist.
     * @param dice Der Würfel, der platziert werden soll.
     * @return true, wenn der Würfel erfolgreich platziert wurde, sonst false.
     */
    public boolean placeDice(Dice dice) {
        if (!isOccupied) {
            this.dice = dice;
            this.isOccupied = true;
            return true;
        }
        return false;
    }

    /**
     * Setzt einen Würfel direkt auf das Field und aktualisiert den Belegungsstatus.
     * @param dice Der Würfel, der auf das Field gesetzt werden soll.
     */
    public void setDice(Dice dice) {
        this.dice = dice;
        this.isOccupied = (dice!=null);
    }

    /**
     * Löscht den Würfel vom Field, aber markiert das Field weiterhin als belegt.
     * Dies ist für Felder gedacht, die nur einmal benutzt werden können.
     */
    public void clearOneUseFeld() {
        this.dice = null;
        this.isOccupied = true;
    }

    public void setIsOccupied() {
        this.isOccupied = true;
    }
}
