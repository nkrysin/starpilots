package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.Wuerfel;



public class Pilot extends Spieler{

    private  Spiel spiel;

    public Pilot(Spiel spiel) {
        super();
        this.spiel = spiel;
    }

    private void wuerfelAufFeldPlatzieren(String feldName, Wuerfel wuerfel, int indexWuerfel) {
        Feld feld = spiel.getFeldPilot(feldName);

        if (feld.wuerfelPlatzieren(wuerfel)) {
            getWuerfels().remove(indexWuerfel);
            if ("Kaffee1".equals(feldName) || "Kaffee2".equals(feldName) || "Kaffee3".equals(feldName)) {
                spiel.getFeldCopilot(feldName).setBelegt();

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
    public void kaffeeWaehlen(Wuerfel wuerfel, int indexWuerfel, String kaffeeFeld) {
        if (spiel.getKaffeTokenCount() >= 3) {
            throw new IllegalArgumentException("Sie können nicht mehr als 3 KaffeTokens haben!");
        }

        kaffeeAufFeldPruefenUndPlatzieren(kaffeeFeld, wuerfel, indexWuerfel);

    }

    @Override
    public void radioWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren("radioFeld", wuerfel, indexWuerfel);
    }

    @Override
    public void speedWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren("speedFeld", wuerfel, indexWuerfel);
    }

    @Override
    public void neigenWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren("neigenFeld", wuerfel, indexWuerfel);
    }

    public void bremsenWaehlen(Wuerfel wuerfel, int indexWuerfel) {

        switch (wuerfel.getWert()) {
            case 2:
                wuerfelAufFeldPlatzieren("bremsenFeld2", wuerfel, indexWuerfel);
                break;
            case 4:
                wuerfelAufFeldPlatzieren("bremsenFeld4", wuerfel, indexWuerfel);
                break;
            case 6:
                wuerfelAufFeldPlatzieren("bremsenFeld6", wuerfel, indexWuerfel);
                break;
            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!");
        }
    }

    public void fahrgestellWaehlen(Wuerfel wuerfel, int indexWuerfel) {

        switch (wuerfel.getWert()) {
            case 1,2:
                wuerfelAufFeldPlatzieren("fahrgestellFeld1_2", wuerfel, indexWuerfel);
                break;
            case 3,4:
                wuerfelAufFeldPlatzieren("fahrgestellFeld3_4", wuerfel, indexWuerfel);
                break;
            case 5,6:
                wuerfelAufFeldPlatzieren("fahrgestellFeld5_6", wuerfel, indexWuerfel);
                break;
            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!");
        }
    }
}
