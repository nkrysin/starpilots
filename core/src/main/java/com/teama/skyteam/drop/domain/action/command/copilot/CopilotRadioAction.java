package com.teama.skyteam.drop.domain.action.command.copilot;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.domain.model.player.Copilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class CopilotRadioAction implements PlayerAction {
    private final Copilot copilot;
    private final Dice dice;
    private final GameStateManager gameStateManager;

    public CopilotRadioAction(Copilot copilot, Dice dice, GameStateManager gameStateManager) {
        this.copilot = copilot;
        this.dice = dice;
        this.gameStateManager = gameStateManager;
    }

    @Override
    public ActionResult execute() {
        ActionResult radioResult = gameStateManager.handleRadio(dice);

        if (!radioResult.isSuccess()) {
            return radioResult;
        }

        copilot.assignToRadio(dice);
        return radioResult;
    }


}
