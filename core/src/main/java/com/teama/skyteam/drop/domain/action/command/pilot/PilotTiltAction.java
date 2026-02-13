package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class PilotTiltAction implements PlayerAction {
    private final Pilot pilot;
    private final Dice dice;

    public PilotTiltAction(Pilot pilot, Dice dice) {
        this.pilot = pilot;
        this.dice = dice;
    }

    @Override
    public ActionResult execute() {
        pilot.neigenWaehlen(dice);
        return ActionResult.success();
    }
}
