package com.teama.skyteam.drop.domain.model.field;

import com.teama.skyteam.drop.domain.model.tokens.Dice;

/**
 * Die Klasse Field repräsentiert ein Spielfeld, auf dem ein Würfel platziert werden kann.
 * Jedes Field hat eine Eigenschaft, ob es belegt ist, und speichert den platzierten Würfel.
 */
public class Field {

    private boolean belegt;
    private Dice dice;

    public Field() {
        this.belegt = false;
        this.dice = null;
    }

    public boolean isBelegt() {
        return belegt;
    }

    public Dice getWuerfel() {
        return dice;
    }

    /**
     * Platziert einen Würfel auf dem Field, falls es nicht belegt ist.
     * @param w Der Würfel, der platziert werden soll.
     * @return true, wenn der Würfel erfolgreich platziert wurde, sonst false.
     */
    public boolean wuerfelPlatzieren(Dice w) {
        if (!belegt) {
            this.dice = w;
            this.belegt = true;
            return true;
        }
        return false;
    }

    /**
     * Setzt einen Würfel direkt auf das Field und aktualisiert den Belegungsstatus.
     * @param w Der Würfel, der auf das Field gesetzt werden soll.
     */
    public void setWuerfel(Dice w) {
        this.dice = w;
        this.belegt = (w!=null);
    }

    /**
     * Löscht den Würfel vom Field, aber markiert das Field weiterhin als belegt.
     * Dies ist für Felder gedacht, die nur einmal benutzt werden können.
     */
    public void clearOneUseFeld() {
        this.dice = null;
        this.belegt = true;
    }

    public void setBelegt() {
        this.belegt = true;
    }
}
