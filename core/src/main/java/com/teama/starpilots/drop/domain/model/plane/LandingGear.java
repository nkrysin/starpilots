/**
 * Verwaltert die Aktivierung von Fahrgestellen eines Flugzeugs.
 *
 * Die Klasse stellt sicher, dass jedes LandingGear nur einmal aktiviert werden kann
 * und gibt die Anzahl der aktivierten Fahrgestelle zurück.
 */
package com.teama.starpilots.drop.domain.model.plane;

import java.util.HashMap;
import java.util.Map;

public class LandingGear {

    private final Map<Integer, Boolean> activatedLandingGear = new HashMap<>();

    private int activatedLandingGearCount = 0;

    public LandingGear() {
        for (int i = 1; i <= 3; i++) {
            activatedLandingGear.put(i, false);
        }
    }
    /**
     * Aktiviert das LandingGear mit der angegebenen Nummer.
     *
     * @param fahrgestellNummer die Nummer des Fahrgestells, das aktiviert werden soll.
     * @return true, wenn das LandingGear erfolgreich aktiviert wurde; false, wenn das LandingGear mit dieser Nummer bereits aktiviert ist.
     *
     * Die Methode überprüft, ob das LandingGear mit der angegebenen Nummer bereits aktiviert ist.
     * Falls es bereits aktiviert ist, wird false zurückgegeben.
     * Andernfalls wird das LandingGear aktiviert, der Zähler der aktivierten Fahrgestelle erhöht und true zurückgegeben.
     */
    public boolean activateLandingGear(int fahrgestellNummer) {
        if (activatedLandingGear.get(fahrgestellNummer)) {
            return false;
        }

        activatedLandingGear.put(fahrgestellNummer, true);
        activatedLandingGearCount++;
        return true;
    }

    public int getActivatedLandingGearCount() {
        return activatedLandingGearCount;
    }
}
