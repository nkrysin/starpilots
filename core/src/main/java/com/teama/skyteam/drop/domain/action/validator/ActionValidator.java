package com.teama.skyteam.drop.domain.action.validator;

import com.teama.skyteam.drop.domain.model.game.Spiel;
import com.teama.skyteam.drop.domain.action.type.ActionName;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

public interface ActionValidator {
    ValidationResult validatePilotAction(Spiel spiel, ActionName actionName, Wuerfel wuerfel);
    ValidationResult validateCopilotAction(Spiel spiel, ActionName actionName, Wuerfel wuerfel);
}
