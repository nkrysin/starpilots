package com.teama.skyteam.drop.spiel.actionValidator;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.aktion.ActionName;
import com.teama.skyteam.drop.tokens.Wuerfel;

public interface ActionValidator {
    ValidationResult validatePilotAction(Spiel spiel, ActionName actionName, Wuerfel wuerfel);
    ValidationResult validateCopilotAction(Spiel spiel, ActionName actionName, Wuerfel wuerfel);
}
