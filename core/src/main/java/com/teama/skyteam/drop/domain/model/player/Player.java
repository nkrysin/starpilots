package com.teama.skyteam.drop.domain.model.player;

import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.game.GameConstants;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeTokenAktion;
import com.teama.skyteam.drop.domain.model.tokens.Dice;
import com.teama.skyteam.drop.domain.model.tokens.RerollToken;

import java.util.ArrayList;
/**
 * Die abstrakte Klasse Player repräsentiert die grundlegenden Möglichkeiten, die für Co-/Pilot ähnlich sind.
 * Sie enthält die grundlegenden Mechaniken, die Player mit Würfeln und speziellen Token interagieren lassen.
 */

public abstract class Player {

    private ArrayList<Dice> dice = new ArrayList<Dice>(GameConstants.MAX_WUERFEL);

    public Player() {
    }

    public ArrayList<Dice> getWuerfels() {
        return dice;
    }

    public Dice getWuerfel(Dice dice) {
        return this.dice.stream()
            .filter(w -> w.equals(dice))
            .findFirst()
            .orElse(null);
    }

    public boolean removeWuerfel(Dice dice) {
        return this.dice.remove(dice);
    }

    public void setWuerfels(ArrayList<Dice> dice) {
        this.dice = dice;
    }

    public int getAnzahlWuerfels() {
        return dice.size();
    }

    /**
     * Führt ein erneutes Würfeln für einen Zielspieler mithilfe eines RerollToken durch.
     *
     * @param token  Das Token, das das erneute Würfeln erlaubt.
     * @param target Der Zielspieler, dessen Würfel neu geworfen werden sollen.
     */
    public void neuWurfTokenBenutzen(RerollToken token, Player target) {
        target.setWuerfels(token.erneutWerfen(target));
    }

    /**
     * Führt ein erneutes Würfeln für zwei Zielspieler mithilfe eines `RerollToken` durch.
     *
     * @param token   Das Token, das das erneute Würfeln erlaubt.
     * @param target1 Der erste Zielspieler.
     * @param target2 Der zweite Zielspieler.
     */
    public void neuWurfTokenBenutzen(RerollToken token, Player target1, Player target2) {
        target1.setWuerfels(token.erneutWerfen(target1));
        target2.setWuerfels(token.erneutWerfen(target2));
    }

    /**
     * Verwendet ein `CoffeeToken`, um den Wert eines Würfels zu ändern.
     *
     * @param token         Das `CoffeeToken`, das die Änderung ermöglicht.
     * @param aktion        Die Aktion, die auf den Würfel angewendet werden soll (z. B. PLUS oder MINUS).
     * @throws IllegalArgumentException Wenn der Würfelwert bereits maximal (6) oder minimal (1) ist.
     */
    public void kaffeeTokenBenutzen(CoffeeToken token, CoffeeTokenAktion aktion, Dice dice) {
        int index = this.dice.indexOf(dice);
        Dice aktueller = this.dice.get(index);
        if (aktion == CoffeeTokenAktion.PLUS && aktueller.getWert() == GameConstants.MAX_WUERFEL_VALUE) {
            throw new IllegalArgumentException("Wuerfelwert ist schon maximal");
        } else if (aktion == CoffeeTokenAktion.MINUS && aktueller.getWert() == GameConstants.MIN_WUERFEL_VALUE) {
            throw new IllegalArgumentException("Wuerfelwert ist schon minimal");
        }
        Dice neuer = token.wertAendern(aktion, aktueller);
        this.dice.set(index, neuer);
    }

    //Abstrakte Methoden, die weiter in den Co-/Pilot Klassen implementiert sind.

    protected abstract void kaffeeWaehlen(Dice dice, FieldName kaffeeFeld);

    protected abstract void radioWaehlen(Dice dice);

    protected abstract void speedWaehlen(Dice dice);

    protected abstract void neigenWaehlen(Dice dice);
}
