package com.teama.skyteam.drop.domain.model.player;

import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.model.game.GameConstants;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

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
    private void wuerfelAufFeldPlatzieren(FieldName fieldName, Dice dice) {
        Field field = game.getFeldCopilot(fieldName);

        if (field.wuerfelPlatzieren(dice)) {
            removeWuerfel(dice);
            switch (fieldName) {
                case KAFFEE_1, KAFFEE_2, KAFFEE_3 ->
                    game.getFeldPilot(fieldName).setBelegt();
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
    private void kaffeeAufFeldPruefenUndPlatzieren(FieldName fieldName, Dice dice) {
        if (!game.getFeldPilot(fieldName).isBelegt() && !game.getFeldCopilot(fieldName).isBelegt()) {
            wuerfelAufFeldPlatzieren(fieldName, dice);
            CoffeeToken token = new CoffeeToken();
            game.kaffeeTokenAnzahlErhoehen(token);
        }
    }

    /**
     * Implementiert die Logik zum Platzieren eines Würfels auf einem Kaffee-Field.
     * @param dice Der zu platzierende Würfel.
     * @param kaffeeFeld Der Name des Kaffee-Feldes.
     * @throws IllegalArgumentException Wenn die maximale Anzahl an Kaffee-Tokens erreicht ist.
     */
    @Override
    public void kaffeeWaehlen(Dice dice, FieldName kaffeeFeld) {

        if (game.getKaffeeTokenCount() >= GameConstants.MAX_KAFFEE_TOKENS) {
            throw new IllegalArgumentException("Sie können nicht mehr als 3 KaffeeTokens haben!");
        }

        kaffeeAufFeldPruefenUndPlatzieren(kaffeeFeld, dice);

    }

    /**
     * Platziert einen Würfel auf einem der beiden Radio-Felder des Copiloten.
     * @param dice Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn beide Radio-Felder bereits belegt sind.
     */
    @Override
    public void radioWaehlen(Dice dice) {

        if (!game.getFeldCopilot(FieldName.RADIO_COPILOT_1).isBelegt()) {
            wuerfelAufFeldPlatzieren(FieldName.RADIO_COPILOT_1, dice);
        } else if (!game.getFeldCopilot(FieldName.RADIO_COPILOT_2).isBelegt()) {
            wuerfelAufFeldPlatzieren(FieldName.RADIO_COPILOT_2, dice);
        } else {
            throw new IllegalArgumentException("Beide Radiofelder sind bereits belegt!");
        }
    }

    /**
     * Platziert einen Würfel auf dem Speed-Field des Copiloten.
     * @param dice Der zu platzierende Würfel.
     */
    @Override
    public void speedWaehlen(Dice dice) {
        wuerfelAufFeldPlatzieren(FieldName.SPEED, dice);
    }

    /**
     * Platziert einen Würfel auf dem Neigungs-Field des Copiloten.
     * @param dice Der zu platzierende Würfel.
     */
    @Override
    public void neigenWaehlen(Dice dice) {
        wuerfelAufFeldPlatzieren(FieldName.NEIGEN, dice);
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Flaps-Field des Copiloten basierend auf dem Wert des Würfels.
     * @param dice Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn der Würfelwert nicht zu einem Flaps-Field passt.
     */
    public void klappenWaehlen(Dice dice) {

        switch (dice.getWert()) {
            case 1:
                wuerfelAufFeldPlatzieren(FieldName.KLAPPEN_1_2, dice);
                break;
            case 2:
                if (!game.getFeldCopilot(FieldName.KLAPPEN_1_2).isBelegt()) {
                    wuerfelAufFeldPlatzieren(FieldName.KLAPPEN_1_2, dice);
                    break;
                }
                wuerfelAufFeldPlatzieren(FieldName.KLAPPEN_2_3, dice);
                break;
            case 3:
                wuerfelAufFeldPlatzieren(FieldName.KLAPPEN_2_3, dice);
                break;
            case 4:
                wuerfelAufFeldPlatzieren(FieldName.KLAPPEN_4_5, dice);
                break;
            case 5:
                if (!game.getFeldCopilot(FieldName.KLAPPEN_4_5).isBelegt()) {
                    wuerfelAufFeldPlatzieren(FieldName.KLAPPEN_4_5, dice);
                    break;
                }
                wuerfelAufFeldPlatzieren(FieldName.KLAPPEN_5_6, dice);
                break;
            case 6:
                wuerfelAufFeldPlatzieren(FieldName.KLAPPEN_5_6, dice);
                break;

            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Field platziert werden!");
        }
    }
}
