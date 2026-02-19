package com.teama.skyteam.drop.domain.model.campaign;

/**
 * Die Klasse Altitude repräsentiert eine bestimmte Flughöhe mit zusätzlichen Eigenschaften,
 * wie ob ein erneutes Würfeln auf dieser Höhe erlaubt ist.
 * Sie bietet außerdem statische Methoden zur Erstellung von Szenarien basierend auf vordefinierten Typen (z. B. "GREEN" oder "RED").
 */

public class Altitude {
    private int altitude;
    private boolean hasRerollToken;

    private Altitude(int altitude, boolean hasRerollToken) {
        this.altitude = altitude;
        this.hasRerollToken = hasRerollToken;
    }

    public static Altitude of(int altitude, boolean hasRerollToken) {
        return new Altitude(altitude, hasRerollToken);
    }

    public int getAltitude() {
        return altitude;
    }

    public boolean hasRerollToken() {
        return hasRerollToken;
    }
}
