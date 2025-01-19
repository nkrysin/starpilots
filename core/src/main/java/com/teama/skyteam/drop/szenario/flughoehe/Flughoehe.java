package com.teama.skyteam.drop.szenario.flughoehe;

import java.util.ArrayList;
import java.util.List;
/**
 * Die Klasse Flughoehe repräsentiert eine bestimmte Flughöhe mit zusätzlichen Eigenschaften,
 * wie ob ein erneutes Würfeln auf dieser Höhe erlaubt ist.
 * Sie bietet außerdem statische Methoden zur Erstellung von Szenarien basierend auf vordefinierten Typen (z. B. "GREEN" oder "RED").
 */

public class Flughoehe {
    private int hoehe;
    private boolean hatErneuWerfen;

    private Flughoehe(int hoehe, boolean hatErneuWerfen) {
        this.hoehe = hoehe;
        this.hatErneuWerfen = hatErneuWerfen;
    }

    /**
     * Erstellt ein Szenario von Flughöhen basierend auf einem angegebenen Typ.
     *
     * @param type Der Typ des Szenarios ("GREEN" oder "RED").
     * @return Eine Liste von Flughöhen für das Szenario.
     * @throws IllegalArgumentException Wenn der Typ ungültig ist.
     */

    public static List<Flughoehe> createHoeheSzenario(String type) {
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
     * Erstellt das "GREEN"-Szenario, das eine Liste von Flughöhen gemäß den "GREEN"-Höhevalues zurückgibt.
     *
     * @return Eine Liste von Flughöhen für das "GREEN"-Szenario.
     */
    private static List<Flughoehe> createGreenSzenario() {
        List<Flughoehe> greenSzenario = new ArrayList<>();
        for (Green green : Green.values()) {
            greenSzenario.add(new Flughoehe(green.getHoehe(), green.hatErneuWerfen()));
        }
        return greenSzenario;
    }

    /**
     * Erstellt das "RED"-Szenario, das eine Liste von Flughöhen gemäß den "RED"-Höhevalues zurückgibt.
     *
     * @return Eine Liste von Flughöhen für das "RED"-Szenario.
     */
    private static List<Flughoehe> createRedSzenario() {
        List<Flughoehe> redSzenario = new ArrayList<>();
        for (Red red : Red.values()) {
            redSzenario.add(new Flughoehe(red.getHoehe(), red.hatErneuWerfen()));
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
