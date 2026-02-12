package com.teama.skyteam.drop.spiel.action.command.pilot;

import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class PilotChassisAction implements PlayerAction {
    private final Pilot pilot;
    private final Wuerfel wuerfel;

    public PilotChassisAction(Pilot pilot, Wuerfel wuerfel) {
        this.pilot = pilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute() {
        pilot.fahrgestellWaehlen(wuerfel);
        //schalterGUI.activateFahrgestellSchalter(0); // 1-2 - 0, 3-4 - 1, 5-6 - 2,
        //schalterGUI.moveFahrgestellIndicator(spiel.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle());
        return ActionResult.success();
    }
}
