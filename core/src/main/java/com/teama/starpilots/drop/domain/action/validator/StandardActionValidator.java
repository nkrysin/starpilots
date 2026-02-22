package com.teama.starpilots.drop.domain.action.validator;

import com.teama.starpilots.drop.domain.model.field.FieldName;
import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.action.type.ActionName;
import com.teama.starpilots.drop.domain.model.tokens.Dice;

public class StandardActionValidator implements ActionValidator {
    @Override
    public ValidationResult validatePilotAction(Game game, ActionName actionName, Dice dice){
        return switch (actionName) {
            case ActionName.PILOT_RADIO -> {
                if (game.getFieldPilot(FieldName.RADIO_PILOT).isOccupied()) {

                    yield ValidationResult.fail("Fehler beim Radio der Piloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BRAKES_2 -> {
                if (dice.getValue() != 2) {
                    yield ValidationResult.fail("Brakes 2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BRAKES_4 -> {
                if (dice.getValue() != 4) {
                    yield ValidationResult.fail("Brakes 4 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BRAKES_6 -> {
                if (dice.getValue() != 6) {
                    yield ValidationResult.fail("Brakes 6 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_LANDING_GEAR_1_2 -> {
                if (dice.getValue() != 1 && dice.getValue() != 2) {
                    yield ValidationResult.fail("LandingGear 1-2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_LANDING_GEAR_3_4 -> {
                if (dice.getValue() != 3 && dice.getValue() != 4) {
                    yield ValidationResult.fail("LandingGear 3-4 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_LANDING_GEAR_5_6 -> {
                if (dice.getValue() != 5 && dice.getValue() != 6) {
                    yield ValidationResult.fail("LandingGear 5-6 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            default -> ValidationResult.success();
        };
    }

    @Override
    public ValidationResult validateCopilotAction(Game game, ActionName actionName, Dice dice){
        return switch (actionName) {
            case ActionName.COPILOT_RADIO_1 -> {
                if (game.getFieldCopilot(FieldName.RADIO_COPILOT_1).isOccupied()) {
                    yield ValidationResult.fail("Fehler beim Radio 1 der Copiloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_RADIO_2 -> {
                if (game.getFieldCopilot(FieldName.RADIO_COPILOT_2).isOccupied()) {
                    yield ValidationResult.fail("Fehler beim Radio 2 der Copiloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_FLAPS_1_2 -> {
                if (dice.getValue() != 1 && dice.getValue() != 2) {
                    yield ValidationResult.fail("Flaps 1_2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_FLAPS_2_3 -> {
                if (dice.getValue() != 2 && dice.getValue() != 3) {
                    yield ValidationResult.fail("Flaps 2_3 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_FLAPS_4_5 -> {
                if (dice.getValue() != 4 && dice.getValue() != 5) {
                    yield ValidationResult.fail("Flaps 4_5 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_FLAPS_5_6 -> {
                if (dice.getValue() != 5 && dice.getValue() != 6) {
                    yield ValidationResult.fail("Flaps 5_6 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            default -> ValidationResult.success();
        };
    }
}
