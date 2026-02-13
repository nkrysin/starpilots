package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

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
        pilot.kaffeeWaehlen(dice, fieldName);
        return ActionResult.success();
    }
}
