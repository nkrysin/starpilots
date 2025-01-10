package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

public class Copilot extends Spieler {

    private Spiel spiel;

    public Copilot(Spiel spiel) {
        super();
        this.spiel = spiel;
    }

    private void wuerfelAufFeldPlatzieren(String feldName, Wuerfel wuerfel, int indexWuerfel) {
        Feld feld = spiel.getFeldCopilot(feldName);

        if (feld.wuerfelPlatzieren(wuerfel)) {
            getWuerfels().remove(indexWuerfel);
            if ("Kaffee1".equals(feldName) || "Kaffee2".equals(feldName) || "Kaffee3".equals(feldName)) {
                spiel.getFeldPilot(feldName).setBelegt();
            }
        } else {
            throw new IllegalArgumentException("Feld '" + feldName + "' ist schon belegt!");
        }
    }

    private void kaffeeAufFeldPruefenUndPlatzieren(String feldName, Wuerfel wuerfel, int indexWuerfel) {
        if (!spiel.getFeldPilot(feldName).isBelegt() && !spiel.getFeldCopilot(feldName).isBelegt()) {
            wuerfelAufFeldPlatzieren(feldName, wuerfel, indexWuerfel);
            KaffeToken token = new KaffeToken();
            spiel.kaffeTokenAnzahlErhoehen(token);
        }
    }

    @Override
    public void kaffeeWaehlen(Wuerfel wuerfel, int indexWuerfel, String kaffee) {

        if (spiel.getKaffeTokenCount() >= 3) {
            throw new IllegalArgumentException("Sie können nicht mehr als 3 KaffeTokens haben!");
        }

        kaffeeAufFeldPruefenUndPlatzieren(kaffee, wuerfel, indexWuerfel);

    }


    @Override
    public void radioWaehlen(Wuerfel wuerfel, int indexWuerfel) {

        if (!spiel.getFeldCopilot("radioFeld1").isBelegt()) {
            wuerfelAufFeldPlatzieren("radioFeld1", wuerfel, indexWuerfel);
        } else if (!spiel.getFeldCopilot("radioFeld2").isBelegt()) {
            wuerfelAufFeldPlatzieren("radioFeld2", wuerfel, indexWuerfel);
        } else {
            throw new IllegalArgumentException("Beide Radiofelder sind bereits belegt!");
        }
    }

    @Override
    public void speedWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren("speedFeld", wuerfel, indexWuerfel);
    }

    @Override
    public void neigenWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren("neigenFeld", wuerfel, indexWuerfel);
    }

    public void klappenWaehlen(Wuerfel wuerfel, int indexWuerfel) {

        switch (wuerfel.getWert()) {
            case 1:
                wuerfelAufFeldPlatzieren("klappenFeld1_2", wuerfel, indexWuerfel);
                break;
            case 2:
                if (!spiel.getFeldCopilot("klappenFeld1_2").isBelegt()) {
                    wuerfelAufFeldPlatzieren("klappenFeld1_2", wuerfel, indexWuerfel);
                    break;
                }
                wuerfelAufFeldPlatzieren("klappenFeld2_3", wuerfel, indexWuerfel);
                break;
            case 3:
                wuerfelAufFeldPlatzieren("klappenFeld2_3", wuerfel, indexWuerfel);
                break;
            case 4:
                wuerfelAufFeldPlatzieren("klappenFeld4_5", wuerfel, indexWuerfel);
                break;
            case 5:
                if (!spiel.getFeldCopilot("klappenFeld4_5").isBelegt()) {
                    wuerfelAufFeldPlatzieren("klappenFeld4_5", wuerfel, indexWuerfel);
                    break;
                }
                wuerfelAufFeldPlatzieren("klappenFeld5_6", wuerfel, indexWuerfel);
                break;
            case 6:
                wuerfelAufFeldPlatzieren("klappenFeld5_6", wuerfel, indexWuerfel);
                break;

            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!");
        }
    }
}
