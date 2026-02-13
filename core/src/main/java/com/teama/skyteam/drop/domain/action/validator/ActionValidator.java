package com.teama.skyteam.drop.domain.action.validator;

import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.action.type.ActionName;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public interface ActionValidator {
    ValidationResult validatePilotAction(Game game, ActionName actionName, Dice dice);
    ValidationResult validateCopilotAction(Game game, ActionName actionName, Dice dice);
}
