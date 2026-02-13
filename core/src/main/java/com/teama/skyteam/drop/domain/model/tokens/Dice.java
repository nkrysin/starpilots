package com.teama.skyteam.drop.domain.model.tokens;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Dice {
    private int wert;
    private final UUID id = UUID.randomUUID();

    public Dice() {
        wuerfeln();
    }

    public int getWert() {
        return wert;
    }

    // Private Methode: Würfelt einen neuen Wert (1 bis 6) und speichert ihn.
    private void wuerfeln() {
        Random random = new Random();
        int randomNumber = random.nextInt(6) + 1;
        this.wert = randomNumber;
    }

    // Methode, um den Wert des Würfels basierend auf einer Aktion zu ändern.
    void wertAendern(CoffeeTokenAktion aktion) {
        switch (aktion) {
            case PLUS:
                this.wert = getWert() + 1;
                break;
            case MINUS:
                this.wert = getWert() - 1;
                break;
            default:
                throw new IllegalArgumentException("Unerwartete Aktion: " + aktion);
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
