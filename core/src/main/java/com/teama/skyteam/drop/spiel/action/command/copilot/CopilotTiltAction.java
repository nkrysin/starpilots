package com.teama.skyteam.drop.spiel.action.command.copilot;

import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class CopilotTiltAction implements PlayerAction {
    private final Copilot copilot;
    private final Wuerfel wuerfel;

    public CopilotTiltAction(Copilot copilot, Wuerfel wuerfel) {
        this.copilot = copilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute() {
        copilot.neigenWaehlen(wuerfel);
        return ActionResult.success();
    }
}
