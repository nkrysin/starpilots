package com.teama.skyteam.drop.spiel.action.command.copilot;

import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class CopilotFlapsAction implements PlayerAction {
    private final Copilot copilot;
    private final Wuerfel wuerfel;

    public CopilotFlapsAction(Copilot copilot, Wuerfel wuerfel) {
        this.copilot = copilot;
        this.wuerfel = wuerfel;
    }

    @Override
    public ActionResult execute(){
        copilot.klappenWaehlen(wuerfel);
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
