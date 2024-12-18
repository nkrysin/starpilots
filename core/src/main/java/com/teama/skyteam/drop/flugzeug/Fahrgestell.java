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
