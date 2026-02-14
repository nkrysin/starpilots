package com.teama.skyteam.drop.domain.model.campaign.altitude;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * Erstellt ein Campaign von Flughöhen basierend auf einem angegebenen Typ.
     *
     * @param type Der Typ des Szenarios ("GREEN" oder "RED").
     * @return Eine Liste von Flughöhen für das Campaign.
     * @throws IllegalArgumentException Wenn der Typ ungültig ist.
     */

    public static List<Altitude> createAltitudeCampaign(String type) {
        switch (type.toUpperCase()) {
            case "GREEN":
                return createGreenCampaign();
            case "RED":
                return createRedCampaign();
            default:
                throw new IllegalArgumentException("Ungültiger Typ: " + type + ". Gültige Typen sind ‚GREEN‘ oder ‚RED‘");
        }
    }

    /**
     * Erstellt das "GREEN"-Campaign, das eine Liste von Flughöhen gemäß den "GREEN"-Höhevalues zurückgibt.
     *
     * @return Eine Liste von Flughöhen für das "GREEN"-Campaign.
     */
    private static List<Altitude> createGreenCampaign() {
        List<Altitude> greenCampaign = new ArrayList<>();
        for (Green green : Green.values()) {
            greenCampaign.add(new Altitude(green.getAltitude(), green.hasRerollToken()));
        }
        return greenCampaign;
    }

    /**
     * Erstellt das "RED"-Campaign, das eine Liste von Flughöhen gemäß den "RED"-Höhevalues zurückgibt.
     *
     * @return Eine Liste von Flughöhen für das "RED"-Campaign.
     */
    private static List<Altitude> createRedCampaign() {
        List<Altitude> redCampaign = new ArrayList<>();
        for (Red red : Red.values()) {
            redCampaign.add(new Altitude(red.getAltitude(), red.hasRerollToken()));
        }
        return redCampaign;
    }

    public int getAltitude() {
        return altitude;
    }

    public boolean hasRerollToken() {
        return hasRerollToken;
    }
}
