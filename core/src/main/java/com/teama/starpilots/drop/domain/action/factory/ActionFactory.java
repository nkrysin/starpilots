package com.teama.starpilots.drop.domain.action.factory;

import com.teama.starpilots.drop.domain.action.command.copilot.*;
import com.teama.starpilots.drop.domain.action.command.pilot.*;
import com.teama.starpilots.drop.domain.model.field.FieldName;
import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.action.command.PlayerAction;
import com.teama.starpilots.drop.domain.action.type.ActionName;
import com.teama.starpilots.drop.application.gamestate.GameStateManager;
import com.teama.starpilots.drop.domain.model.player.Copilot;
import com.teama.starpilots.drop.domain.model.player.Pilot;
import com.teama.starpilots.drop.domain.model.tokens.Dice;

public class ActionFactory {
    private final Game game;
    private final GameStateManager gameStateManager;

    public ActionFactory(Game game, GameStateManager gameStateManager) {
        this.game = game;
        this.gameStateManager = gameStateManager;
    }

    public PlayerAction createPilotAction(ActionName action, Pilot pilot, Dice dice) {
        switch (action) {
            case ActionName.PILOT_RADIO:
                return new PilotRadioAction(pilot, dice, gameStateManager);
            case ActionName.PILOT_SPEED:
                return new PilotSpeedAction(pilot, dice);
            case ActionName.PILOT_TILT:
                return new PilotTiltAction(pilot, dice);

            case ActionName.PILOT_BRAKES_2:
            case ActionName.PILOT_BRAKES_4:
            case ActionName.PILOT_BRAKES_6:
                return new PilotBrakeAction(game, pilot, dice);

            case ActionName.PILOT_LANDING_GEAR_1_2:
            case ActionName.PILOT_LANDING_GEAR_3_4:
            case ActionName.PILOT_LANDING_GEAR_5_6:
                return new PilotLandingGearAction(game, pilot, dice);

            case ActionName.PILOT_COFFEE_1:
                return new PilotCaffeAction(pilot, dice, FieldName.COFFEE_1);
            case ActionName.PILOT_COFFEE_2:
                return new PilotCaffeAction(pilot, dice, FieldName.COFFEE_2);
            case ActionName.PILOT_COFFEE_3:
                return new PilotCaffeAction(pilot, dice, FieldName.COFFEE_3);
            default:
                throw new IllegalArgumentException("Illegal action: " + action);
        }
    };

    public PlayerAction createCopilotAction(ActionName action, Copilot copilot, Dice dice) {
        switch (action) {
            case ActionName.COPILOT_RADIO_1:
            case ActionName.COPILOT_RADIO_2:
                return new CopilotRadioAction(copilot, dice, gameStateManager);
            case ActionName.COPILOT_SPEED:
                return new CopilotSpeedAction(copilot, dice);
            case ActionName.COPILOT_TILT:
                return new CopilotTiltAction(copilot, dice);
            case ActionName.COPILOT_FLAPS_1_2:
            case ActionName.COPILOT_FLAPS_2_3:
            case ActionName.COPILOT_FLAPS_4_5:
            case ActionName.COPILOT_FLAPS_5_6:
                return new CopilotFlapsAction(game, copilot, dice);
            case ActionName.COPILOT_COFFEE_1:
                return new CopilotCaffeAction(copilot, dice, FieldName.COFFEE_1);
            case ActionName.COPILOT_COFFEE_2:
                return new CopilotCaffeAction(copilot, dice, FieldName.COFFEE_2);
            case ActionName.COPILOT_COFFEE_3:
                return new CopilotCaffeAction(copilot, dice, FieldName.COFFEE_3);
            default:
                throw new IllegalArgumentException("Illegal action: " + action);
        }
    };

}
