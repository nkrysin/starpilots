package com.teama.skyteam.drop.domain.action.validator;

import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.action.type.ActionName;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class StandardActionValidator implements ActionValidator {
    @Override
    public ValidationResult validatePilotAction(Game game, ActionName actionName, Dice dice){
        return switch (actionName) {
            case ActionName.PILOT_RADIO -> {
                if (game.getFeldPilot(FieldName.RADIO_PILOT).isBelegt()) {

                    yield ValidationResult.fail("Fehler beim Radio der Piloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BREMSEN_2 -> {
                if (dice.getWert() != 2) {
                    yield ValidationResult.fail("Brakes 2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BREMSEN_4 -> {
                if (dice.getWert() != 4) {
                    yield ValidationResult.fail("Brakes 4 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BREMSEN_6 -> {
                if (dice.getWert() != 6) {
                    yield ValidationResult.fail("Brakes 6 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_FAHRGESTELL_1_2 -> {
                if (dice.getWert() != 1 && dice.getWert() != 2) {
                    yield ValidationResult.fail("LandingGear 1-2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_FAHRGESTELL_3_4 -> {
                if (dice.getWert() != 3 && dice.getWert() != 4) {
                    yield ValidationResult.fail("LandingGear 3-4 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_FAHRGESTELL_5_6 -> {
                if (dice.getWert() != 5 && dice.getWert() != 6) {
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
                if (game.getFeldCopilot(FieldName.RADIO_COPILOT_1).isBelegt()) {
                    yield ValidationResult.fail("Fehler beim Radio 1 der Copiloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_RADIO_2 -> {
                if (game.getFeldCopilot(FieldName.RADIO_COPILOT_2).isBelegt()) {
                    yield ValidationResult.fail("Fehler beim Radio 2 der Copiloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_KLAPPEN_1_2 -> {
                if (dice.getWert() != 1 && dice.getWert() != 2) {
                    yield ValidationResult.fail("Flaps 1_2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_KLAPPEN_2_3 -> {
                if (dice.getWert() != 2 && dice.getWert() != 3) {
                    yield ValidationResult.fail("Flaps 2_3 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_KLAPPEN_4_5 -> {
                if (dice.getWert() != 4 && dice.getWert() != 5) {
                    yield ValidationResult.fail("Flaps 4_5 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_KLAPPEN_5_6 -> {
                if (dice.getWert() != 5 && dice.getWert() != 6) {
                    yield ValidationResult.fail("Flaps 5_6 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            default -> ValidationResult.success();
        };
    }
}
