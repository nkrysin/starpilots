package com.teama.skyteam.drop.domain.action.command.copilot;

import com.teama.skyteam.drop.domain.model.feld.FeldName;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.model.spieler.Copilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class CopilotCaffeAction implements PlayerAction {
    private final Copilot copilot;
    private final Wuerfel wuerfel;
    private final FeldName feldName;

    public CopilotCaffeAction(Copilot copilot, Wuerfel wuerfel, FeldName feldName) {
        this.copilot = copilot;
        this.wuerfel = wuerfel;
        this.feldName = feldName;
    }

    @Override
    public ActionResult execute() {
        copilot.kaffeeWaehlen(wuerfel, feldName);
        return ActionResult.success();
    }
}
