package com.teama.skyteam.drop.spiel.actionValidator;

import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.aktion.ActionName;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class StandartActionValidator implements ActionValidator {
    @Override
    public ValidationResult validatePilotAction(Spiel spiel, ActionName actionName, Wuerfel wuerfel){
        return switch (actionName) {
            case ActionName.PILOT_RADIO -> {
                if (!spiel.getFeldPilot(FeldName.RADIO_PILOT).isBelegt()) {
                    yield ValidationResult.fail("Fehler beim Radio der Piloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BREMSEN_2 -> {
                if (wuerfel.getWert() != 2) {
                    yield ValidationResult.fail("Bremsen 2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BREMSEN_4 -> {
                if (wuerfel.getWert() != 4) {
                    yield ValidationResult.fail("Bremsen 4 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_BREMSEN_6 -> {
                if (wuerfel.getWert() != 6) {
                    yield ValidationResult.fail("Bremsen 6 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_FAHRGESTELL_1_2 -> {
                if (wuerfel.getWert() != 1 && wuerfel.getWert() != 2) {
                    yield ValidationResult.fail("Fahrgestell 1-2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_FAHRGESTELL_3_4 -> {
                if (wuerfel.getWert() != 3 && wuerfel.getWert() != 4) {
                    yield ValidationResult.fail("Fahrgestell 3-4 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.PILOT_FAHRGESTELL_5_6 -> {
                if (wuerfel.getWert() != 5 && wuerfel.getWert() != 6) {
                    yield ValidationResult.fail("Fahrgestell 5-6 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            default -> ValidationResult.success();
        };
    }

    @Override
    public ValidationResult validateCopilotAction(Spiel spiel, ActionName actionName, Wuerfel wuerfel){
        return switch (actionName) {
            case ActionName.COPILOT_RADIO_1 -> {
                if (!spiel.getFeldPilot(FeldName.RADIO_COPILOT_1).isBelegt()) {
                    yield ValidationResult.fail("Fehler beim Radio 1 der Piloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_RADIO_2 -> {
                if (!spiel.getFeldPilot(FeldName.RADIO_COPILOT_2).isBelegt()) {
                    yield ValidationResult.fail("Fehler beim Radio 2 der Piloten!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_KLAPPEN_1_2 -> {
                if (wuerfel.getWert() != 1 && wuerfel.getWert() != 2) {
                    yield ValidationResult.fail("Klappen 1_2 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_KLAPPEN_2_3 -> {
                if (wuerfel.getWert() != 2 && wuerfel.getWert() != 3) {
                    yield ValidationResult.fail("Klappen 2_3 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_KLAPPEN_4_5 -> {
                if (wuerfel.getWert() != 4 && wuerfel.getWert() != 5) {
                    yield ValidationResult.fail("Klappen 4_5 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            case ActionName.COPILOT_KLAPPEN_5_6 -> {
                if (wuerfel.getWert() != 5 && wuerfel.getWert() != 6) {
                    yield ValidationResult.fail("Klappen 5_6 nicht möglich!");
                }
                yield ValidationResult.success();
            }
            default -> ValidationResult.success();
        };
    }
}
