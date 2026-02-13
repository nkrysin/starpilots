package com.teama.skyteam.drop.spiel.action.command.pilot;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spiel.logik.events.BrakesActivatedEvent;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class PilotBrakeAction implements PlayerAction {
    private final Spiel spiel;
    private final Pilot pilot;
    private final Wuerfel wuerfel;

    public PilotBrakeAction(Spiel spiel, Pilot pilot, Wuerfel wuerfel) {
        this.spiel = spiel;
        this.pilot = pilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute() {
        if (spiel.getFlugzeug().bremsen(wuerfel)) {
            pilot.bremsenWaehlen(wuerfel);
            int indicatorValue = spiel.getFlugzeug().getBremsenZustand().getValue();
            int schalterIndex = (indicatorValue - 2) / 2;

            return ActionResult.success()
                .withEvent(new BrakesActivatedEvent(schalterIndex, indicatorValue));
        }
        return ActionResult.fail("Bremsen nicht möglich!");

    }
}
