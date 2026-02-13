package com.teama.skyteam.drop.spiel.action.command.pilot;

import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spiel.action.command.PlayerAction;
import com.teama.skyteam.drop.spiel.gamestate.GameStateManager;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class PilotRadioAction implements PlayerAction {
    private final Spiel spiel;
    private final Pilot pilot;
    private final Wuerfel wuerfel;
    private final GameStateManager gameStateManager;

    public PilotRadioAction(Spiel spiel, Pilot pilot, Wuerfel wuerfel, GameStateManager gameStateManager) {
        this.spiel = spiel;
        this.pilot = pilot;
        this.wuerfel = wuerfel;
        this.gameStateManager = gameStateManager;
    }

    @Override
    public ActionResult execute() {
        ActionResult radioResult = gameStateManager.handleRadio(wuerfel);

        if (!radioResult.isSuccess()) {
            return radioResult;
        }

        pilot.radioWaehlen(wuerfel);
        return radioResult;
    }


}
