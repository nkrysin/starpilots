package com.teama.skyteam.drop.domain.model.campaign.altitude;

import java.util.ArrayList;
import java.util.List;
/**
 * Die Klasse Altitude repräsentiert eine bestimmte Flughöhe mit zusätzlichen Eigenschaften,
 * wie ob ein erneutes Würfeln auf dieser Höhe erlaubt ist.
 * Sie bietet außerdem statische Methoden zur Erstellung von Szenarien basierend auf vordefinierten Typen (z. B. "GREEN" oder "RED").
 */

public class Altitude {
    private int hoehe;
    private boolean hatErneuWerfen;

    private Altitude(int hoehe, boolean hatErneuWerfen) {
        this.hoehe = hoehe;
        this.hatErneuWerfen = hatErneuWerfen;
    }

    /**
     * Erstellt ein Campaign von Flughöhen basierend auf einem angegebenen Typ.
     *
     * @param type Der Typ des Szenarios ("GREEN" oder "RED").
     * @return Eine Liste von Flughöhen für das Campaign.
     * @throws IllegalArgumentException Wenn der Typ ungültig ist.
     */

    public static List<Altitude> createHoeheSzenario(String type) {
        switch (type.toUpperCase()) {
            case "GREEN":
                return createGreenSzenario();
            case "RED":
                return createRedSzenario();
            default:
                throw new IllegalArgumentException("Ungültiger Typ: " + type + ". Gültige Typen sind ‚GREEN‘ oder ‚RED‘");
        }
    }

    /**
     * Erstellt das "GREEN"-Campaign, das eine Liste von Flughöhen gemäß den "GREEN"-Höhevalues zurückgibt.
     *
     * @return Eine Liste von Flughöhen für das "GREEN"-Campaign.
     */
    private static List<Altitude> createGreenSzenario() {
        List<Altitude> greenSzenario = new ArrayList<>();
        for (Green green : Green.values()) {
            greenSzenario.add(new Altitude(green.getHoehe(), green.hatErneuWerfen()));
        }
        return greenSzenario;
    }

    /**
     * Erstellt das "RED"-Campaign, das eine Liste von Flughöhen gemäß den "RED"-Höhevalues zurückgibt.
     *
     * @return Eine Liste von Flughöhen für das "RED"-Campaign.
     */
    private static List<Altitude> createRedSzenario() {
        List<Altitude> redSzenario = new ArrayList<>();
        for (Red red : Red.values()) {
            redSzenario.add(new Altitude(red.getHoehe(), red.hatErneuWerfen()));
        }
        return redSzenario;
    }

    public int getHoehe() {
        return hoehe;
    }

    public boolean hatErneuWerfen() {
        return hatErneuWerfen;
    }
}
