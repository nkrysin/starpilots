package com.teama.starpilots.drop.domain.action.command.pilot;

import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.action.command.ActionResult;
import com.teama.starpilots.drop.domain.action.command.PlayerAction;
import com.teama.starpilots.drop.application.logic.events.BrakesActivatedEvent;
import com.teama.starpilots.drop.domain.model.player.Pilot;
import com.teama.starpilots.drop.domain.model.tokens.Dice;

public class PilotBrakeAction implements PlayerAction {
    private final Game game;
    private final Pilot pilot;
    private final Dice dice;

    public PilotBrakeAction(Game game, Pilot pilot, Dice dice) {
        this.game = game;
        this.pilot = pilot;
        this.dice = dice;
    }

    @Override
    public ActionResult execute() {
        if (game.getPlane().bremsen(dice)) {
            pilot.assignToBrakes(dice);
            int indicatorValue = game.getPlane().getBrakesState().getValue();
            int schalterIndex = (indicatorValue - 2) / 2;

            return ActionResult.success()
                .withEvent(new BrakesActivatedEvent(schalterIndex, indicatorValue));
        }
        return ActionResult.fail("Brakes nicht möglich!");

    }
}
