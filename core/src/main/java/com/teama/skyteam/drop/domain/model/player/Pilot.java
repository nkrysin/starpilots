package com.teama.skyteam.drop.domain.model.player;

import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.model.game.GameConstants;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.Dice;


/**
 * Die Klasse Pilot erweitert die abstrakte Klasse Player.
 * Sie implementiert spezifische Logiken, die einem Piloten im Game zugeordnet sind,
 * wie das Platzieren von Würfeln auf verschiedenen Spielfeldern.
 */

public class Pilot extends Player {

    private Game game;

    public Pilot(Game game) {
        super();
        this.game = game;
    }

    /**
     * Platziert einen Würfel auf einem Spielfeld des Piloten.
     * Falls das Field Kaffee ist, wird es gleichzeitig für den Copiloten blockiert.
     *
     * @param fieldName Der Name des Zielfeldes.
     * @param dice  Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn das Field bereits belegt ist.
     */
    private void placeDiceOnField(FieldName fieldName, Dice dice) {
        Field field = game.getFieldPilot(fieldName);

        if (field.placeDice(dice)) {
            removeDice(dice);
            switch (fieldName) {
                case COFFEE_1, COFFEE_2, COFFEE_3 ->
                    game.getFieldCopilot(fieldName).setIsOccupied();
            }

        } else {
            throw new IllegalArgumentException("Field '" + fieldName + "' ist schon belegt!");
        }
    }

    /**
     * Überprüft, ob das Kaffee-Field für beide Player frei ist,
     * und platziert einen Würfel darauf, falls es verfügbar ist.
     * Zusätzlich wird die Anzahl der Kaffee-Tokens erhöht.
     *
     * @param fieldName Der Name des Zielfeldes.
     * @param dice  Der zu platzierende Würfel.
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
     * Platziert einen Würfel auf dem Radio-Field.
     * @param dice Der zu platzierende Würfel.
     */
    @Override
    public void assignToRadio(Dice dice) {
        placeDiceOnField(FieldName.RADIO_PILOT, dice);
    }

    /**
     * Platziert einen Würfel auf dem Speed-Field.
     * @param dice Der zu platzierende Würfel.
     */
    @Override
    public void assignToSpeed(Dice dice) {
        placeDiceOnField(FieldName.SPEED, dice);
    }

    /**
     * Platziert einen Würfel auf dem Neigungs-Field.
     * @param dice Der zu platzierende Würfel.
     */
    @Override
    public void assignToTilt(Dice dice) {
        placeDiceOnField(FieldName.TILT, dice);
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Brakes-Field basierend auf dem Wert des Würfels.
     * @param dice Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn der Würfelwert nicht zu einem Brakes-Field passt.
     */
    public void assignToBrakes(Dice dice) {

        switch (dice.getValue()) {
            case 2:
                placeDiceOnField(FieldName.BRAKES_2, dice);
                break;
            case 4:
                placeDiceOnField(FieldName.BRAKES_4, dice);
                break;
            case 6:
                placeDiceOnField(FieldName.BRAKES_6, dice);
                break;
            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Field platziert werden!");
        }
    }

    /**
     * Platziert einen Würfel auf einem spezifischen LandingGear-Field basierend auf dem Wert des Würfels.
     * @param dice Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn der Würfelwert nicht zu einem LandingGear-Field passt.
     */
    public void assignToLandingGear(Dice dice) {

        switch (dice.getValue()) {
            case 1,2:
                placeDiceOnField(FieldName.LANDING_GEAR_1_2, dice);
                break;
            case 3,4:
                placeDiceOnField(FieldName.LANDING_GEAR_3_4, dice);
                break;
            case 5,6:
                placeDiceOnField(FieldName.LANDING_GEAR_5_6, dice);
                break;
            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Field platziert werden!");
        }
    }
}
