package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.model.spieler.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class PilotSpeedAction implements PlayerAction {
    private final Pilot pilot;
    private final Wuerfel wuerfel;

    public PilotSpeedAction(Pilot pilot, Wuerfel wuerfel) {
        this.pilot = pilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute() {
        pilot.speedWaehlen(wuerfel);
        return ActionResult.success();
    }
}
