package com.teama.skyteam.drop.domain.action.command.copilot;

import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.application.logic.events.FlapsActivatedEvent;
import com.teama.skyteam.drop.domain.model.player.Copilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

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
        if (game.getFlugzeug().klappenOeffnen(dice)) {
            copilot.klappenWaehlen(dice);
            int count = game.getFlugzeug().getKlappenZustand().getValue();
            int schalterIndex = count - 9;
            return ActionResult.success()
                .withEvent(new FlapsActivatedEvent(schalterIndex, count));
        }
        return ActionResult.fail("Flaps nicht möglich!");


//        if (gewaehlterWuerfel.getWert() <= 2) {
//            schalterGUI.activateKlappenSchalter(0);
//        } else if (gewaehlterWuerfel.getWert() <= 3) {
//            schalterGUI.activateKlappenSchalter(1);
//        } else if (gewaehlterWuerfel.getWert() <= 5) {
//            schalterGUI.activateKlappenSchalter(2);
//        } else {
//            schalterGUI.activateKlappenSchalter(3);
//        }
//        schalterGUI.moveKlappenIndicator(game.getFlugzeug().getKlappenZustand().getValue());
    }
}
