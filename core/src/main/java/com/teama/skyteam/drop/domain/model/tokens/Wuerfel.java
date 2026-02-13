package com.teama.skyteam.drop.domain.model.tokens;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Wuerfel {
    private int wert;
    private final UUID id = UUID.randomUUID();

    public Wuerfel() {
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
    void wertAendern(KaffeeTokenAktion aktion) {
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
        if (!(o instanceof Wuerfel)) return false;
        Wuerfel wuerfel = (Wuerfel) o;
        return id.equals(wuerfel.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
