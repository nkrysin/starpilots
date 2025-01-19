package com.teama.skyteam.drop.tokens;

import java.util.Random;

public class Wuerfel {
    private int wert;

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

}
