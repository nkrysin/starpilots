package com.teama.skyteam.drop.domain.model.tokens;

public class CoffeeToken {
    /**
     * Ändert den Wert eines Würfels basierend auf der angegebenen Aktion.
     *
     * @param aktion Die Aktion, die auf den Würfel angewendet werden soll (PLUS oder MINUS).
     * @param dice Der Würfel, dessen Wert geändert werden soll.
     * @return Der Würfel mit dem aktualisierten Wert.
     *
     * Funktionsweise:
     * Die Methode überprüft die übergebene Aktion und ändert entsprechend den Wert
     * des Würfels um +1 oder -1. Andere Aktionen bleiben unberücksichtigt.
     */
    public Dice modifyValue(CoffeeTokenAction aktion, Dice dice) {
        switch (aktion) {
            case PLUS:
                dice.modifyValue(CoffeeTokenAction.PLUS);
                return dice;
            case MINUS:
                dice.modifyValue(CoffeeTokenAction.MINUS);
                return dice;
            default:
                return dice;
        }

    }
}
