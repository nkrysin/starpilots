package com.teama.skyteam.drop.domain.rules;

import com.teama.skyteam.drop.domain.model.game.Game;

public interface GameRules {
    /**
     * Überprüft, ob eine Ausfallbedingung im Game erreicht wurde.
     */
    boolean checkAusfallBedingung(Game game);


    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     */
    boolean checkFinalRound(Game game);

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     */
    boolean checkGewinnBedingung(Game game);
}
