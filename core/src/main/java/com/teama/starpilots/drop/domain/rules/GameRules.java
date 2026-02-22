package com.teama.starpilots.drop.domain.rules;

import com.teama.starpilots.drop.domain.model.game.Game;

public interface GameRules {
    /**
     * Überprüft, ob eine Ausfallbedingung im Game erreicht wurde.
     */
    boolean checkFailCondition(Game game);


    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     */
    boolean checkFinalRound(Game game);

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     */
    boolean checkWinCondition(Game game);
}
