package com.teama.starpilots.drop.domain.action.command.copilot;

import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.action.command.ActionResult;
import com.teama.starpilots.drop.domain.action.command.PlayerAction;
import com.teama.starpilots.drop.application.logic.events.FlapsActivatedEvent;
import com.teama.starpilots.drop.domain.model.player.Copilot;
import com.teama.starpilots.drop.domain.model.tokens.Dice;

public class CopilotFlapsAction implements PlayerAction {
    private final Game game;
    private final Copilot copilot;
    private final Dice dice;

    public CopilotFlapsAction(Game game, Copilot copilot, Dice dice) {
        this.game = game;
        this.copilot = copilot;
        this.dice = dice;
    }

    @Override
    public ActionResult execute(){
        if (game.getPlane().deployFlaps(dice)) {
            copilot.assignToFlaps(dice);
            int count = game.getPlane().getFlapsState().getValue();
            int switchIndex = count - 9;
            return ActionResult.success()
                .withEvent(new FlapsActivatedEvent(switchIndex, count));
        }
        return ActionResult.fail("Flaps nicht möglich!");


//        if (gewaehlterWuerfel.getWert() <= 2) {
//            schalterGUI.aktivateFlapsSwitch(0);
//        } else if (gewaehlterWuerfel.getWert() <= 3) {
//            schalterGUI.aktivateFlapsSwitch(1);
//        } else if (gewaehlterWuerfel.getWert() <= 5) {
//            schalterGUI.aktivateFlapsSwitch(2);
//        } else {
//            schalterGUI.aktivateFlapsSwitch(3);
//        }
//        schalterGUI.moveFlapsIndicator(game.getPlane().getFlapsState().getValue());
    }
}
