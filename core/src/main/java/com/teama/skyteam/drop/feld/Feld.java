package com.teama.skyteam.drop.feld;

import com.teama.skyteam.drop.tokens.Wuerfel;

/**
 * Die Klasse Feld repräsentiert ein Spielfeld, auf dem ein Würfel platziert werden kann.
 * Jedes Feld hat eine Eigenschaft, ob es belegt ist, und speichert den platzierten Würfel.
 */
public class Feld {

    private boolean belegt;
    private Wuerfel wuerfel;

    public Feld() {
        this.belegt = false;
        this.wuerfel = null;
    }

    public boolean isBelegt() {
        return belegt;
    }

    public Wuerfel getWuerfel() {
        return wuerfel;
    }

    /**
     * Platziert einen Würfel auf dem Feld, falls es nicht belegt ist.
     * @param w Der Würfel, der platziert werden soll.
     * @return true, wenn der Würfel erfolgreich platziert wurde, sonst false.
     */
    public boolean wuerfelPlatzieren(Wuerfel w) {
        if (!belegt) {
            this.wuerfel = w;
            this.belegt = true;
            return true;
        }
        return false;
    }

    /**
     * Setzt einen Würfel direkt auf das Feld und aktualisiert den Belegungsstatus.
     * @param w Der Würfel, der auf das Feld gesetzt werden soll.
     */
    public void setWuerfel(Wuerfel w) {
        this.wuerfel = w;
        this.belegt = (w!=null);
    }

    /**
     * Löscht den Würfel vom Feld, aber markiert das Feld weiterhin als belegt.
     * Dies ist für Felder gedacht, die nur einmal benutzt werden können.
     */
    public void clearOneUseFeld() {
        this.wuerfel = null;
        this.belegt = true;
    }

    public void setBelegt() {
        this.belegt = true;
    }
}
