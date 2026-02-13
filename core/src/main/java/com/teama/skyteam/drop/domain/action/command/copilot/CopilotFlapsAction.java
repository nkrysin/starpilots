package com.teama.skyteam.drop.domain.action.command.copilot;

import com.teama.skyteam.drop.domain.model.game.Spiel;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.application.logic.events.FlapsActivatedEvent;
import com.teama.skyteam.drop.domain.model.spieler.Copilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class CopilotFlapsAction implements PlayerAction {
    private final Spiel spiel;
    private final Copilot copilot;
    private final Wuerfel wuerfel;

    public CopilotFlapsAction(Spiel spiel, Copilot copilot, Wuerfel wuerfel) {
        this.spiel = spiel;
        this.copilot = copilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute(){
        if (spiel.getFlugzeug().klappenOeffnen(wuerfel)) {
            copilot.klappenWaehlen(wuerfel);
            int count = spiel.getFlugzeug().getKlappenZustand().getValue();
            int schalterIndex = count - 9;
            return ActionResult.success()
                .withEvent(new FlapsActivatedEvent(schalterIndex, count));
        }
        return ActionResult.fail("Klappen nicht möglich!");


//        if (gewaehlterWuerfel.getWert() <= 2) {
//            schalterGUI.activateKlappenSchalter(0);
//        } else if (gewaehlterWuerfel.getWert() <= 3) {
//            schalterGUI.activateKlappenSchalter(1);
//        } else if (gewaehlterWuerfel.getWert() <= 5) {
//            schalterGUI.activateKlappenSchalter(2);
//        } else {
//            schalterGUI.activateKlappenSchalter(3);
//        }
//        schalterGUI.moveKlappenIndicator(spiel.getFlugzeug().getKlappenZustand().getValue());
    }
}
