package com.teama.skyteam.drop.domain.model.campaign;

/**
 * Die Klasse RouteField repräsentiert ein Field auf der Karte,
 * das entweder ein Flughafen oder ein gewöhnliches Field sein kann.
 * Jedes Field speichert die Anzahl der darauf befindlichen Flugzeuge
 * und ob es sich um einen Flughafen handelt.
 */

public class RouteField {
    private final boolean istFlughafen;
    private int planes;

    RouteField(int planes, boolean istFlughafen) {

        this.planes = planes;
        this.istFlughafen = istFlughafen;
    }


    /**
     * Entfernt ein Plane vom Field beim Radionutzen,
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
     * Überprüft, ob das Field ein Flughafen ist.
     *
     * @return true, wenn das Field ein Flughafen ist, sonst false.
     */
    public boolean istFlughafen() {
        return istFlughafen;
    }
}


