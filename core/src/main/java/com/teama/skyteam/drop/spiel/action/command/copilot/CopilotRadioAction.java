package com.teama.skyteam.drop.spiel.action.command.copilot;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spiel.gamestate.GameStateManager;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class CopilotRadioAction implements PlayerAction {
    private final Copilot copilot;
    private final Wuerfel wuerfel;
    private final GameStateManager gameStateManager;

    public CopilotRadioAction(Copilot copilot, Wuerfel wuerfel, GameStateManager gameStateManager) {
        this.copilot = copilot;
        this.wuerfel = wuerfel;
        this.gameStateManager = gameStateManager;
    }

    @Override
    public ActionResult execute() {
        ActionResult radioResult = gameStateManager.handleRadio(wuerfel);

        if (!radioResult.isSuccess()) {
            return radioResult;
        }

        copilot.radioWaehlen(wuerfel);
        return radioResult;
    }


}
