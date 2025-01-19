package com.teama.skyteam.drop.flugzeug;

import java.util.HashMap;
import java.util.Map;

public class Fahrgestell {

    private final Map<Integer, Boolean> aktivierteFahrgestelle = new HashMap<>();

    private int akrivierteFahrgestelle = 0;

    public Fahrgestell() {
        for (int i = 1; i <= 3; i++) {
            aktivierteFahrgestelle.put(i, false);
        }
    }
    /**
     * Aktiviert das Fahrgestell mit der angegebenen Nummer.
     *
     * @param fahrgestellNummer die Nummer des Fahrgestells, das aktiviert werden soll.
     * @return true, wenn das Fahrgestell erfolgreich aktiviert wurde; false, wenn das Fahrgestell mit dieser Nummer bereits aktiviert ist.
     *
     * Die Methode überprüft, ob das Fahrgestell mit der angegebenen Nummer bereits aktiviert ist.
     * Falls es bereits aktiviert ist, wird false zurückgegeben.
     * Andernfalls wird das Fahrgestell aktiviert, der Zähler der aktivierten Fahrgestelle erhöht und true zurückgegeben.
     */
    public boolean fahrgestellAktivieren(int fahrgestellNummer) {
        if (aktivierteFahrgestelle.get(fahrgestellNummer)) {
            return false;
        }

        aktivierteFahrgestelle.put(fahrgestellNummer, true);
        akrivierteFahrgestelle++;
        return true;
    }

    public int getAkrivierteFahrgestelle() {
        return akrivierteFahrgestelle;
    }
}
