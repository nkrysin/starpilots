package com.teama.skyteam.drop.spiel.logik.rules;

import com.teama.skyteam.drop.spiel.Spiel;

public interface GameRules {
    /**
     * Überprüft, ob eine Ausfallbedingung im Spiel erreicht wurde.
     */
    boolean checkAusfallBedingung(Spiel spiel);


    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     */
    boolean checkFinalRound(Spiel spiel);

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     */
    boolean checkGewinnBedingung(Spiel spiel);
}
