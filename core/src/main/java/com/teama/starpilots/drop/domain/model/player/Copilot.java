package com.teama.starpilots.drop.domain.model.player;

import com.teama.starpilots.drop.domain.model.field.Field;
import com.teama.starpilots.drop.domain.model.field.FieldName;
import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.model.game.GameConstants;
import com.teama.starpilots.drop.domain.model.tokens.CoffeeToken;
import com.teama.starpilots.drop.domain.model.tokens.Dice;

/**
 * Die Klasse Copilot erweitert die abstrakte Klasse Player.
 * Sie implementiert spezifische Logiken für den Copiloten im Game,
 * wie das Platzieren von Würfeln auf den Spielfeldern des Copiloten.
 */
public class Copilot extends Player {

    private Game game;

    public Copilot(Game game) {
        super();
        this.game = game;
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Spielfeld des Copiloten.
     * Wenn das Field ein Kaffee-Field ist, wird es gleichzeitig für den Piloten blockiert.
     *
     * @param fieldName Der Name des Zielfeldes.
     * @param dice Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn das Field bereits belegt ist.
     */
    private void placeDiceOnField(FieldName fieldName, Dice dice) {
        Field field = game.getFieldCopilot(fieldName);

        if (field.placeDice(dice)) {
            removeDice(dice);
            switch (fieldName) {
                case COFFEE_1, COFFEE_2, COFFEE_3 ->
                    game.getFieldPilot(fieldName).setIsOccupied();
            }
        } else {
            throw new IllegalArgumentException("Field '" + fieldName + "' ist schon belegt!");
        }
    }


    /**
     * Überprüft, ob das Kaffee-Field für beide Player frei ist,
     * und platziert einen Würfel darauf, falls verfügbar.
     * Gleichzeitig wird die Anzahl der Kaffee-Tokens erhöht.
     *
     * @param fieldName Der Name des Zielfeldes.
     * @param dice Der zu platzierende Würfel.
     */
    private void validateAndPlaceCoffeOnField(FieldName fieldName, Dice dice) {
        if (!game.getFieldPilot(fieldName).isOccupied() && !game.getFieldCopilot(fieldName).isOccupied()) {
            placeDiceOnField(fieldName, dice);
            CoffeeToken token = new CoffeeToken();
            game.incrementCoffeeTokenCount(token);
        }
    }

    /**
     * Implementiert die Logik zum Platzieren eines Würfels auf einem Kaffee-Field.
     * @param dice Der zu platzierende Würfel.
     * @param kaffeeFeld Der Name des Kaffee-Feldes.
     * @throws IllegalArgumentException Wenn die maximale Anzahl an Kaffee-Tokens erreicht ist.
     */
    @Override
    public void assignToCoffee(Dice dice, FieldName kaffeeFeld) {

        if (game.getCoffeeTokenCount() >= GameConstants.MAX_COFFEE_TOKENS) {
            throw new IllegalArgumentException("Sie können nicht mehr als 3 KaffeeTokens haben!");
        }

        validateAndPlaceCoffeOnField(kaffeeFeld, dice);

    }

    /**
     * Platziert einen Würfel auf einem der beiden Radio-Felder des Copiloten.
     * @param dice Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn beide Radio-Felder bereits belegt sind.
     */
    @Override
    public void assignToRadio(Dice dice) {

        if (!game.getFieldCopilot(FieldName.RADIO_COPILOT_1).isOccupied()) {
            placeDiceOnField(FieldName.RADIO_COPILOT_1, dice);
        } else if (!game.getFieldCopilot(FieldName.RADIO_COPILOT_2).isOccupied()) {
            placeDiceOnField(FieldName.RADIO_COPILOT_2, dice);
        } else {
            throw new IllegalArgumentException("Beide Radiofelder sind bereits belegt!");
        }
    }

    /**
     * Platziert einen Würfel auf dem Speed-Field des Copiloten.
     * @param dice Der zu platzierende Würfel.
     */
    @Override
    public void assignToSpeed(Dice dice) {
        placeDiceOnField(FieldName.SPEED, dice);
    }

    /**
     * Platziert einen Würfel auf dem Neigungs-Field des Copiloten.
     * @param dice Der zu platzierende Würfel.
     */
    @Override
    public void assignToTilt(Dice dice) {
        placeDiceOnField(FieldName.TILT, dice);
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Flaps-Field des Copiloten basierend auf dem Wert des Würfels.
     * @param dice Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn der Würfelwert nicht zu einem Flaps-Field passt.
     */
    public void assignToFlaps(Dice dice) {

        switch (dice.getValue()) {
            case 1:
                placeDiceOnField(FieldName.FLIPS_1_2, dice);
                break;
            case 2:
                if (!game.getFieldCopilot(FieldName.FLIPS_1_2).isOccupied()) {
                    placeDiceOnField(FieldName.FLIPS_1_2, dice);
                    break;
                }
                placeDiceOnField(FieldName.FLIPS_2_3, dice);
                break;
            case 3:
                placeDiceOnField(FieldName.FLIPS_2_3, dice);
                break;
            case 4:
                placeDiceOnField(FieldName.FLIPS_4_5, dice);
                break;
            case 5:
                if (!game.getFieldCopilot(FieldName.FLIPS_4_5).isOccupied()) {
                    placeDiceOnField(FieldName.FLIPS_4_5, dice);
                    break;
                }
                placeDiceOnField(FieldName.FLIPS_5_6, dice);
                break;
            case 6:
                placeDiceOnField(FieldName.FLIPS_5_6, dice);
                break;

            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Field platziert werden!");
        }
    }
}
