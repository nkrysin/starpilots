package com.teama.skyteam.drop.domain.model.player;

import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.game.GameConstants;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeTokenAction;
import com.teama.skyteam.drop.domain.model.tokens.Dice;
import com.teama.skyteam.drop.domain.model.tokens.RerollToken;

import java.util.ArrayList;
/**
 * Die abstrakte Klasse Player repräsentiert die grundlegenden Möglichkeiten, die für Co-/Pilot ähnlich sind.
 * Sie enthält die grundlegenden Mechaniken, die Player mit Würfeln und speziellen Token interagieren lassen.
 */

public abstract class Player {

    private ArrayList<Dice> allDice = new ArrayList<Dice>(GameConstants.MAX_DICE);

    public Player() {
    }

    public ArrayList<Dice> getAllDice() {
        return allDice;
    }

    public Dice getDice(Dice dice) {
        return this.allDice.stream()
            .filter(w -> w.equals(dice))
            .findFirst()
            .orElse(null);
    }

    public boolean removeDice(Dice dice) {
        return this.allDice.remove(dice);
    }

    public void setAllDice(ArrayList<Dice> dice) {
        this.allDice = dice;
    }

    public int getAllDiceCount() {
        return allDice.size();
    }

    /**
     * Führt ein erneutes Würfeln für einen Zielspieler mithilfe eines RerollToken durch.
     *
     * @param rerollToken  Das Token, das das erneute Würfeln erlaubt.
     * @param target Der Zielspieler, dessen Würfel neu geworfen werden sollen.
     */
    public void useRerollToken(RerollToken rerollToken, Player target) {
        target.setAllDice(rerollToken.reroll(target));
    }

    /**
     * Führt ein erneutes Würfeln für zwei Zielspieler mithilfe eines `RerollToken` durch.
     *
     * @param rerollToken   Das Token, das das erneute Würfeln erlaubt.
     * @param target1 Der erste Zielspieler.
     * @param target2 Der zweite Zielspieler.
     */
    public void useRerollToken(RerollToken rerollToken, Player target1, Player target2) {
        target1.setAllDice(rerollToken.reroll(target1));
        target2.setAllDice(rerollToken.reroll(target2));
    }

    /**
     * Verwendet ein `CoffeeToken`, um den Wert eines Würfels zu ändern.
     *
     * @param coffeeToken         Das `CoffeeToken`, das die Änderung ermöglicht.
     * @param action        Die Aktion, die auf den Würfel angewendet werden soll (z. B. PLUS oder MINUS).
     * @throws IllegalArgumentException Wenn der Würfelwert bereits maximal (6) oder minimal (1) ist.
     */
    public void useCoffeToken(CoffeeToken coffeeToken, CoffeeTokenAction action, Dice dice) {
        int index = this.allDice.indexOf(dice);
        Dice currentDice = this.allDice.get(index);
        if (action == CoffeeTokenAction.PLUS && currentDice.getValue() == GameConstants.MAX_DICE_VALUE) {
            throw new IllegalArgumentException("Wuerfelwert ist schon maximal");
        } else if (action == CoffeeTokenAction.MINUS && currentDice.getValue() == GameConstants.MIN_DICE_VALUE) {
            throw new IllegalArgumentException("Wuerfelwert ist schon minimal");
        }
        Dice newDice = coffeeToken.modifyValue(action, currentDice);
        this.allDice.set(index, newDice);
    }

    //Abstrakte Methoden, die weiter in den Co-/Pilot Klassen implementiert sind.

    protected abstract void assignToCoffee(Dice dice, FieldName kaffeeFeld);

    protected abstract void assignToRadio(Dice dice);

    protected abstract void assignToSpeed(Dice dice);

    protected abstract void assignToTilt(Dice dice);
}
