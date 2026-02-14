package com.teama.skyteam.drop.domain.action.command.copilot;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.model.player.Copilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class CopilotSpeedAction implements PlayerAction {
    private final Copilot copilot;
    private final Dice dice;

    public CopilotSpeedAction(Copilot copilot, Dice dice) {
        this.copilot = copilot;
        this.dice = dice;
    }

    @Override
    public ActionResult execute() {
        copilot.assignToSpeed(dice);
        return ActionResult.success();
    }
}
