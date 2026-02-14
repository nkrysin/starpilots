package com.teama.skyteam.drop.domain.rules;

import com.teama.skyteam.drop.domain.model.plane.Flaps;
import com.teama.skyteam.drop.domain.model.game.Game;

public class StandartGameRules implements GameRules {
    /**
     * Überprüft, ob eine Ausfallbedingung im Game erreicht wurde.
     *
     * @return true, wenn eine Ausfallbedingung erfüllt ist, sonst false.
     */
    public boolean checkFailCondition(Game game) {
        boolean bedingungKollision = game.isCollision();
        boolean bedingungWenigerAlsSzenarioLange = game.getCurrentPosition() > game.getRouteCampaign().size();
        boolean bedingungHoehe = !game.getCurrentField().isAirport() && (game.getCurrentAltitude().getAltitude() == 0);
        boolean bedingungNeigung = (game.getPlane().getTilt() > 2 || game.getPlane().getTilt() < -2);
        return bedingungKollision || bedingungWenigerAlsSzenarioLange || bedingungHoehe || bedingungNeigung;
    }

    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     *
     * @return true, wenn die finale Runde erfüllt ist, sonst false.
     */
    public boolean checkFinalRound(Game game) {
        boolean bedingungFlughafen = game.getCurrentField().isAirport();
        boolean bedingungHoehe = (game.getCurrentAltitude().getAltitude() == 0);
        return bedingungFlughafen && bedingungHoehe;
    }

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     *
     * @return true, wenn die Gewinnbedingungen erfüllt sind, sonst false.
     */
    public boolean checkWinCondition(Game game) {
        boolean bedingungIstFrei = (game.getCurrentField().getPlaneCount() == 0);
        boolean bedingungNeigung = (game.getPlane().getTilt() == 0);
        boolean bedingungKlappen = (game.getPlane().getFlapsState() == Flaps.LEVEL4);
        boolean bedingungFahrgestelle = (game.getPlane().getLandingGearState().getActivatedLandingGearCount() == 3);
        boolean bedingungSpeed = (game.getPlane().getCurrentSpeed() < game.getPlane().getBrakesState().getValue());
        return bedingungIstFrei && bedingungNeigung && bedingungKlappen && bedingungFahrgestelle && bedingungSpeed;
    }
}
