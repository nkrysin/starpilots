package com.teama.skyteam.drop.domain.action.command.pilot;

import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.application.logic.events.LandingGearActivatedEvent;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class PilotLandingGearAction implements PlayerAction {
    private final Game game;
    private final Pilot pilot;
    private final Dice dice;

    public PilotLandingGearAction(Game game, Pilot pilot, Dice dice) {
        this.game = game;
        this.pilot = pilot;
        this.dice = dice;
    }

    @Override
    public ActionResult execute() {
        if (game.getPlane().deployLandingGear(dice)) {
            pilot.assignToLandingGear(dice);
            int count = game.getPlane().getLandingGearState().getActivatedLandingGearCount();
            int schalterIndex = (count - 1);
            return ActionResult.success()
                .withEvent(new LandingGearActivatedEvent(schalterIndex, count));
        }
        return ActionResult.fail("LandingGear nicht möglich!");


        //schalterGUI.activateLandingGearSwitch(0); // 1-2 - 0, 3-4 - 1, 5-6 - 2,
        //schalterGUI.moveLandingGearIndicator(game.getPlane().getLandingGearState().getAkrivierteFahrgestelle());

    }
}
