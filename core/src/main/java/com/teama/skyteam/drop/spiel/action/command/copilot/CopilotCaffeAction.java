package com.teama.skyteam.drop.spiel.action.command.copilot;

import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

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
        copilot.speedWaehlen(wuerfel, feldName);
        return ActionResult.success();
    }
}
