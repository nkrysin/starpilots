package com.teama.skyteam.drop.spiel.logik.rules;

import com.teama.skyteam.drop.flugzeug.Klappen;
import com.teama.skyteam.drop.spiel.Spiel;

public class StandartGameRules implements GameRules {
    /**
     * Überprüft, ob eine Ausfallbedingung im Spiel erreicht wurde.
     *
     * @return true, wenn eine Ausfallbedingung erfüllt ist, sonst false.
     */
    public boolean checkAusfallBedingung(Spiel spiel) {
        boolean bedingungKollision = spiel.isKollision();
        boolean bedingungWenigerAlsSzenarioLange = spiel.getCurrentPosition() > spiel.getRouteSzenario().size();
        boolean bedingungHoehe = !spiel.getCurrentField().istFlughafen() && (spiel.getCurrentHoehe().getHoehe() == 0);
        boolean bedingungNeigung = (spiel.getFlugzeug().getNeigung() > 2 || spiel.getFlugzeug().getNeigung() < -2);
        return bedingungKollision || bedingungWenigerAlsSzenarioLange || bedingungHoehe || bedingungNeigung;
    }

    /**
     * Überprüft, ob die finale Runde des Spiels erreicht wurde.
     *
     * @return true, wenn die finale Runde erfüllt ist, sonst false.
     */
    public boolean checkFinalRound(Spiel spiel) {
        boolean bedingungFlughafen = spiel.getCurrentField().istFlughafen();
        boolean bedingungHoehe = (spiel.getCurrentHoehe().getHoehe() == 0);
        return bedingungFlughafen && bedingungHoehe;
    }

    /**
     * Überprüft, ob die Gewinnbedingungen erfüllt sind.
     *
     * @return true, wenn die Gewinnbedingungen erfüllt sind, sonst false.
     */
    public boolean checkGewinnBedingung(Spiel spiel) {
        boolean bedingungIstFrei = (spiel.getCurrentField().getAnzahlFlugzeug() == 0);
        boolean bedingungNeigung = (spiel.getFlugzeug().getNeigung() == 0);
        boolean bedingungKlappen = (spiel.getFlugzeug().getKlappenZustand() == Klappen.LEVEL4);
        boolean bedingungFahrgestelle = (spiel.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle() == 3);
        boolean bedingungSpeed = (spiel.getFlugzeug().getSpeedAktuell() < spiel.getFlugzeug().getBremsenZustand().getValue());
        return bedingungIstFrei && bedingungNeigung && bedingungKlappen && bedingungFahrgestelle && bedingungSpeed;
    }
}
