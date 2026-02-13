package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.model.spieler.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class PilotTiltAction implements PlayerAction {
    private final Pilot pilot;
    private final Wuerfel wuerfel;

    public PilotTiltAction(Pilot pilot, Wuerfel wuerfel) {
        this.pilot = pilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute() {
        pilot.neigenWaehlen(wuerfel);
        return ActionResult.success();
    }
}
