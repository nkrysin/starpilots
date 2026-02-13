package com.teama.skyteam.drop.domain.action.factory;

import com.teama.skyteam.drop.domain.action.command.copilot.*;
import com.teama.skyteam.drop.domain.action.command.pilot.*;
import com.teama.skyteam.drop.domain.model.feld.FeldName;
import com.teama.skyteam.drop.domain.model.game.Spiel;
import com.teama.skyteam.drop.domain.action.command.PlayerAction;
import com.teama.skyteam.drop.domain.action.type.ActionName;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.domain.model.spieler.Copilot;
import com.teama.skyteam.drop.domain.model.spieler.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public class ActionFactory {
    private final Spiel spiel;
    private final GameStateManager gameStateManager;

    public ActionFactory(Spiel spiel, GameStateManager gameStateManager) {
        this.spiel = spiel;
        this.gameStateManager = gameStateManager;
    }

    public PlayerAction createPilotAction(ActionName action, Pilot pilot, Wuerfel wuerfel) {
        switch (action) {
            case ActionName.PILOT_RADIO:
                return new PilotRadioAction(pilot, wuerfel, gameStateManager);
            case ActionName.PILOT_SPEED:
                return new PilotSpeedAction(pilot, wuerfel);
            case ActionName.PILOT_NEIGUNG:
                return new PilotTiltAction(pilot, wuerfel);

                case ActionName.PILOT_BREMSEN_2:
            case ActionName.PILOT_BREMSEN_4:
            case ActionName.PILOT_BREMSEN_6:
                return new PilotBrakeAction(spiel, pilot, wuerfel);

            case ActionName.PILOT_FAHRGESTELL_1_2:
            case ActionName.PILOT_FAHRGESTELL_3_4:
            case ActionName.PILOT_FAHRGESTELL_5_6:
                return new PilotChassisAction(spiel, pilot, wuerfel);

            case ActionName.PILOT_KAFFEE_1:
                return new PilotCaffeAction(pilot, wuerfel, FeldName.KAFFEE_1);
            case ActionName.PILOT_KAFFEE_2:
                return new PilotCaffeAction(pilot, wuerfel, FeldName.KAFFEE_2);
            case ActionName.PILOT_KAFFEE_3:
                return new PilotCaffeAction(pilot, wuerfel, FeldName.KAFFEE_3);
            default:
                throw new IllegalArgumentException("Illegal action: " + action);
        }
    };

    public PlayerAction createCopilotAction(ActionName action, Copilot copilot, Wuerfel wuerfel) {
        switch (action) {
            case ActionName.COPILOT_RADIO_1:
            case ActionName.COPILOT_RADIO_2:
                return new CopilotRadioAction(copilot, wuerfel, gameStateManager);
            case ActionName.COPILOT_SPEED:
                return new CopilotSpeedAction(copilot, wuerfel);
            case ActionName.COPILOT_NEIGUNG:
                return new CopilotTiltAction(copilot, wuerfel);
            case ActionName.COPILOT_KLAPPEN_1_2:
            case ActionName.COPILOT_KLAPPEN_2_3:
            case ActionName.COPILOT_KLAPPEN_4_5:
            case ActionName.COPILOT_KLAPPEN_5_6:
                return new CopilotFlapsAction(spiel, copilot, wuerfel);
            case ActionName.COPILOT_KAFFEE_1:
                return new CopilotCaffeAction(copilot, wuerfel, FeldName.KAFFEE_1);
            case ActionName.COPILOT_KAFFEE_2:
                return new CopilotCaffeAction(copilot, wuerfel, FeldName.KAFFEE_2);
            case ActionName.COPILOT_KAFFEE_3:
                return new CopilotCaffeAction(copilot, wuerfel, FeldName.KAFFEE_3);
            default:
                throw new IllegalArgumentException("Illegal action: " + action);
        }
    };

}
