package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.model.feld.FeldName;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.model.spieler.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class PilotCaffeAction implements PlayerAction {
    private final Pilot pilot;
    private final Wuerfel wuerfel;
    private final FeldName feldName;

    public PilotCaffeAction(Pilot pilot, Wuerfel wuerfel, FeldName feldName) {
        this.pilot = pilot;
        this.wuerfel = wuerfel;
        this.feldName = feldName;
    }

    @Override
    public ActionResult execute() {
        pilot.kaffeeWaehlen(wuerfel, feldName);
        return ActionResult.success();
    }
}
