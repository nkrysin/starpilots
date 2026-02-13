package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.domain.model.spieler.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class PilotRadioAction implements PlayerAction {
    private final Pilot pilot;
    private final Wuerfel wuerfel;
    private final GameStateManager gameStateManager;

    public PilotRadioAction(Pilot pilot, Wuerfel wuerfel, GameStateManager gameStateManager) {
        this.pilot = pilot;
        this.wuerfel = wuerfel;
        this.gameStateManager = gameStateManager;
    }

    @Override
    public ActionResult execute() {
        ActionResult radioResult = gameStateManager.handleRadio(wuerfel);

        if (!radioResult.isSuccess()) {
            return radioResult;
        }

        pilot.radioWaehlen(wuerfel);
        return radioResult;
    }


}
