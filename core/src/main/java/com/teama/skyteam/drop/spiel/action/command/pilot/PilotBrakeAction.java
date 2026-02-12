package com.teama.skyteam.drop.spiel.action.command.pilot;

import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class PilotBrakeAction implements PlayerAction {
    private final Pilot pilot;
    private final Wuerfel wuerfel;

    public PilotBrakeAction(Pilot pilot, Wuerfel wuerfel) {
        this.pilot = pilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute() {
        pilot.bremsenWaehlen(wuerfel);
        // schalterGUI.activateBremsenSchalter(0); // для 2 - 0, 4 - 1, 6 - 2
        // schalterGUI.moveBremsenIndicator(spiel.getFlugzeug().getBremsenZustand().getValue());
        return ActionResult.success();
    }
}
