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

    private void wuerfeln() {
        Random random = new Random();
        int randomNumber = random.nextInt(6) + 1;
        this.wert = randomNumber;
    }

    void wertAendern(KaffeTokenAktion aktion) {
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
