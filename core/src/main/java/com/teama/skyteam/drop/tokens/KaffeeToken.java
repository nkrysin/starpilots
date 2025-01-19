package com.teama.skyteam.drop.tokens;

public class KaffeeToken {
    /**
     * Ändert den Wert eines Würfels basierend auf der angegebenen Aktion.
     *
     * @param aktion Die Aktion, die auf den Würfel angewendet werden soll (PLUS oder MINUS).
     * @param wuerfel Der Würfel, dessen Wert geändert werden soll.
     * @return Der Würfel mit dem aktualisierten Wert.
     *
     * Funktionsweise:
     * Die Methode überprüft die übergebene Aktion und ändert entsprechend den Wert
     * des Würfels um +1 oder -1. Andere Aktionen bleiben unberücksichtigt.
     */
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
