package com.teama.skyteam.drop.domain.action.command.copilot;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.model.spieler.Copilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class CopilotSpeedAction implements PlayerAction {
    private final Copilot copilot;
    private final Wuerfel wuerfel;

    public CopilotSpeedAction(Copilot copilot, Wuerfel wuerfel) {
        this.copilot = copilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute() {
        copilot.speedWaehlen(wuerfel);
        return ActionResult.success();
    }
}
