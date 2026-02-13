package com.teama.skyteam.drop.domain.model.szenario;

/**
 * Die Klasse ReiseFeld repräsentiert ein Feld auf der Karte,
 * das entweder ein Flughafen oder ein gewöhnliches Feld sein kann.
 * Jedes Feld speichert die Anzahl der darauf befindlichen Flugzeuge
 * und ob es sich um einen Flughafen handelt.
 */

public class ReiseFeld {
    private final boolean istFlughafen;
    private int planes;

    ReiseFeld(int planes, boolean istFlughafen) {

        this.planes = planes;
        this.istFlughafen = istFlughafen;
    }


    /**
     * Entfernt ein Flugzeug vom Feld beim Radionutzen,
     * indem die Anzahl der Flugzeuge um eins verringert wird.
     */
    public void removeFlugzeug() {
        setAnzahlFlugzeug(getAnzahlFlugzeug() - 1);
    }

    public int getAnzahlFlugzeug() {
        return planes;
    }

    public void setAnzahlFlugzeug(int planes) {
        this.planes = planes;
    }

    /**
     * Überprüft, ob das Feld ein Flughafen ist.
     *
     * @return true, wenn das Feld ein Flughafen ist, sonst false.
     */
    public boolean istFlughafen() {
        return istFlughafen;
    }
}


