package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class PilotRadioAction implements PlayerAction {
    private final Pilot pilot;
    private final Dice dice;
    private final GameStateManager gameStateManager;

    public PilotRadioAction(Pilot pilot, Dice dice, GameStateManager gameStateManager) {
        this.pilot = pilot;
        this.dice = dice;
        this.gameStateManager = gameStateManager;
    }

    @Override
    public ActionResult execute() {
        ActionResult radioResult = gameStateManager.handleRadio(dice);

        if (!radioResult.isSuccess()) {
            return radioResult;
        }

        pilot.radioWaehlen(dice);
        return radioResult;
    }


}
