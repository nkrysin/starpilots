package com.teama.starpilots.drop.domain.action.command.pilot;

import com.teama.starpilots.drop.domain.model.field.FieldName;
import com.teama.starpilots.drop.domain.action.command.ActionResult;
import com.teama.starpilots.drop.domain.action.command.PlayerAction;
import com.teama.starpilots.drop.domain.model.player.Pilot;
import com.teama.starpilots.drop.domain.model.tokens.Dice;

public class PilotCaffeAction implements PlayerAction {
    private final Pilot pilot;
    private final Dice dice;
    private final FieldName fieldName;

    public PilotCaffeAction(Pilot pilot, Dice dice, FieldName fieldName) {
        this.pilot = pilot;
        this.dice = dice;
        this.fieldName = fieldName;
    }

    @Override
    public ActionResult execute() {
        pilot.assignToCoffee(dice, fieldName);
        return ActionResult.success();
    }
}
