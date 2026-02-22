package com.teama.starpilots.drop.domain.action.command.copilot;

import com.teama.starpilots.drop.domain.model.field.FieldName;
import com.teama.starpilots.drop.domain.action.command.ActionResult;
import com.teama.starpilots.drop.domain.action.command.PlayerAction;
import com.teama.starpilots.drop.domain.model.player.Copilot;
import com.teama.starpilots.drop.domain.model.tokens.Dice;

public class CopilotCaffeAction implements PlayerAction {
    private final Copilot copilot;
    private final Dice dice;
    private final FieldName fieldName;

    public CopilotCaffeAction(Copilot copilot, Dice dice, FieldName fieldName) {
        this.copilot = copilot;
        this.dice = dice;
        this.fieldName = fieldName;
    }

    @Override
    public ActionResult execute() {
        copilot.assignToCoffee(dice, fieldName);
        return ActionResult.success();
    }
}
