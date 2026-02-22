package com.teama.starpilots.drop.domain.action.validator;

import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.action.type.ActionName;
import com.teama.starpilots.drop.domain.model.tokens.Dice;

public interface ActionValidator {
    ValidationResult validatePilotAction(Game game, ActionName actionName, Dice dice);
    ValidationResult validateCopilotAction(Game game, ActionName actionName, Dice dice);
}
