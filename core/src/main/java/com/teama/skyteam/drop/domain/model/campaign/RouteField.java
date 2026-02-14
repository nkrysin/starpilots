package com.teama.skyteam.drop.domain.model.campaign;

/**
 * Die Klasse RouteField repräsentiert ein Field auf der Karte,
 * das entweder ein Flughafen oder ein gewöhnliches Field sein kann.
 * Jedes Field speichert die Anzahl der darauf befindlichen Flugzeuge
 * und ob es sich um einen Flughafen handelt.
 */

public class RouteField {
    private final boolean isAirport;
    private int planeCount;

    RouteField(int planeCount, boolean isAirport) {

        this.planeCount = planeCount;
        this.isAirport = isAirport;
    }


    /**
     * Entfernt ein Plane vom Field beim Radionutzen,
     * indem die Anzahl der Flugzeuge um eins verringert wird.
     */
    public void removePlane() {
        setPlaneCount(getPlaneCount() - 1);
    }

    public int getPlaneCount() {
        return planeCount;
    }

    public void setPlaneCount(int planes) {
        this.planeCount = planes;
    }

    /**
     * Überprüft, ob das Field ein Flughafen ist.
     *
     * @return true, wenn das Field ein Flughafen ist, sonst false.
     */
    public boolean isAirport() {
        return isAirport;
    }
}


