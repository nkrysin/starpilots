package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.Wuerfel;



public class Pilot extends Spieler{

    private  Spiel spiel;

    public Pilot(String name, Spiel spiel) {
        super(name);
        this.spiel = spiel;
    }
    public Pilot(Spiel spiel) {
        super();
        this.spiel = spiel;
    }

    private void wuerfelAufFeldPlatzieren(String feldName, Wuerfel wuerfel, int indexWuerfel) {
        Feld feld = spiel.getFeldPilot(feldName);

        if (feld.wuerfelPlatzieren(wuerfel)) {
            getWuerfels().remove(indexWuerfel);
        } else {
            throw new IllegalArgumentException("Feld '" + feldName + "' ist schon belegt!");
        }
    }


    //TODO проверить можно ли использовать его без повторного написания тут?
    private boolean kaffeeAufFeldPruefenUndPlatzieren(String feldName, Wuerfel wuerfel, int indexWuerfel) {
        if (!spiel.getFeldPilot(feldName).isBelegt() && !spiel.getFeldCopilot(feldName).isBelegt()) {
            wuerfelAufFeldPlatzieren(feldName, wuerfel, indexWuerfel);
            KaffeToken token = new KaffeToken();
            spiel.kaffeTokenAnzahlErhoehen(token);
            return true;
        }
        return false;
    }

    @Override
    public void kaffeeWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        if (spiel.getKaffeTokenCount() >= 3) {
            throw new IllegalArgumentException("Sie können nicht mehr als 3 KaffeTokens haben!");
        }

        String[] kaffeFelder = {"kaffeFeld1", "kaffeFeld2", "kaffeFeld3"};

        for (String feldName : kaffeFelder) {
            if (kaffeeAufFeldPruefenUndPlatzieren(feldName, wuerfel, indexWuerfel)){
                break;
            }
        }
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
