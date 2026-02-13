package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.model.game.Spiel;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.application.logic.events.ChassisActivatedEvent;
import com.teama.skyteam.drop.domain.model.spieler.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class PilotChassisAction implements PlayerAction {
    private final Spiel spiel;
    private final Pilot pilot;
    private final Wuerfel wuerfel;

    public PilotChassisAction(Spiel spiel, Pilot pilot, Wuerfel wuerfel) {
        this.spiel = spiel;
        this.pilot = pilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute() {
        if (spiel.getFlugzeug().fahrgestellAusfahren(wuerfel)) {
            pilot.fahrgestellWaehlen(wuerfel);
            int count = spiel.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle();
            int schalterIndex = (count - 1);
            return ActionResult.success()
                .withEvent(new ChassisActivatedEvent(schalterIndex, count));
        }
        return ActionResult.fail("Fahrgestell nicht möglich!");


        //schalterGUI.activateFahrgestellSchalter(0); // 1-2 - 0, 3-4 - 1, 5-6 - 2,
        //schalterGUI.moveFahrgestellIndicator(spiel.getFlugzeug().getFahrgestellZustand().getAkrivierteFahrgestelle());

    }
}
