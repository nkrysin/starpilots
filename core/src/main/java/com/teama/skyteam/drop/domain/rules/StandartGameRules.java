package com.teama.skyteam.drop.domain.rules;

import com.teama.skyteam.drop.domain.model.flugzeug.Flaps;
import com.teama.skyteam.drop.domain.model.game.Game;

public class StandartGameRules implements GameRules {
    /**
     * Überprüft, ob eine Ausfallbedingung im Game erreicht wurde.
     *
     * @return true, wenn eine Ausfallbedingung erfüllt ist, sonst false.
     */
    public boolean checkAusfallBedingung(Game game) {
        boolean bedingungKollision = game.isKollision();
        boolean bedingungWenigerAlsSzenarioLange = game.getCurrentPosition() > game.getRouteSzenario().size();
        boolean bedingungHoehe = !game.getCurrentField().istFlughafen() && (game.getCurrentHoehe().getHoehe() == 0);
        boolean bedingungNeigung = (game.getFlugzeug().getTilt() > 2 || game.getFlugzeug().getTilt() < -2);
        return bedingungKollision || bedingungWenigerAlsSzenarioLange || bedingungHoehe || bedingungNeigung;
    }

    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     *
     * @return true, wenn die finale Runde erfüllt ist, sonst false.
     */
    public boolean checkFinalRound(Game game) {
        boolean bedingungFlughafen = game.getCurrentField().istFlughafen();
        boolean bedingungHoehe = (game.getCurrentHoehe().getHoehe() == 0);
        return bedingungFlughafen && bedingungHoehe;
    }

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     *
     * @return true, wenn die Gewinnbedingungen erfüllt sind, sonst false.
     */
    public boolean checkGewinnBedingung(Game game) {
        boolean bedingungIstFrei = (game.getCurrentField().getAnzahlFlugzeug() == 0);
        boolean bedingungNeigung = (game.getFlugzeug().getTilt() == 0);
        boolean bedingungKlappen = (game.getFlugzeug().getKlappenZustand() == Flaps.LEVEL4);
        boolean bedingungFahrgestelle = (game.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle() == 3);
        boolean bedingungSpeed = (game.getFlugzeug().getCurrentSpeed() < game.getFlugzeug().getBrakesState().getValue());
        return bedingungIstFrei && bedingungNeigung && bedingungKlappen && bedingungFahrgestelle && bedingungSpeed;
    }
}
