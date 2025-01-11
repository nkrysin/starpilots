package com.teama.skyteam.drop.tokens;

public class KaffeeToken {
    public Wuerfel wertAendern(KaffeeTokenAktion aktion, Wuerfel wuerfel) {
        switch (aktion) {
            case PLUS:
                wuerfel.wertAendern(KaffeeTokenAktion.PLUS);
                return wuerfel;
            case MINUS:
                wuerfel.wertAendern(KaffeeTokenAktion.MINUS);
                return wuerfel;
            default:
                return wuerfel;
        }

    }
}
