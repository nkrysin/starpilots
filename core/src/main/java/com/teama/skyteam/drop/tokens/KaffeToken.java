package com.teama.skyteam.drop.tokens;

public class KaffeToken {
    public Wuerfel wertAendern(KaffeTokenAktion aktion, Wuerfel wuerfel) {
        switch (aktion) {
            case PLUS:
                wuerfel.wertAendern(KaffeTokenAktion.PLUS);
                return wuerfel;
            case MINUS:
                wuerfel.wertAendern(KaffeTokenAktion.MINUS);
                return wuerfel;
            default:
                return wuerfel;
        }

    }
}
