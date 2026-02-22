package com.teama.starpilots.drop.domain.model.tokens;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Dice {
    private int value;
    private final UUID id = UUID.randomUUID();

    public Dice() {
        roll();
    }

    public int getValue() {
        return value;
    }

    // Private Methode: Würfelt einen neuen Wert (1 bis 6) und speichert ihn.
    private void roll() {
        Random random = new Random();
        int randomNumber = random.nextInt(6) + 1;
        this.value = randomNumber;
    }

    // Methode, um den Wert des Würfels basierend auf einer Aktion zu ändern.
    void modifyValue(CoffeeTokenAction action) {
        switch (action) {
            case PLUS:
                this.value = getValue() + 1;
                break;
            case MINUS:
                this.value = getValue() - 1;
                break;
            default:
                throw new IllegalArgumentException("Unerwartete Aktion: " + action);
        }
    }

    public UUID getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dice)) return false;
        Dice dice = (Dice) o;
        return id.equals(dice.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
