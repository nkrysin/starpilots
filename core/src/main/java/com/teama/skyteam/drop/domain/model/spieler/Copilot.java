package com.teama.skyteam.drop.domain.model.spieler;

import com.teama.skyteam.drop.domain.model.feld.Feld;
import com.teama.skyteam.drop.domain.model.feld.FeldName;
import com.teama.skyteam.drop.domain.model.game.Spiel;
import com.teama.skyteam.drop.domain.model.game.SpielConstants;
import com.teama.skyteam.drop.domain.model.tokens.KaffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.Wuerfel;

/**
 * Die Klasse Copilot erweitert die abstrakte Klasse Spieler.
 * Sie implementiert spezifische Logiken für den Copiloten im Spiel,
 * wie das Platzieren von Würfeln auf den Spielfeldern des Copiloten.
 */
public class Copilot extends Spieler {

    private Spiel spiel;

    public Copilot(Spiel spiel) {
        super();
        this.spiel = spiel;
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Spielfeld des Copiloten.
     * Wenn das Feld ein Kaffee-Feld ist, wird es gleichzeitig für den Piloten blockiert.
     *
     * @param feldName Der Name des Zielfeldes.
     * @param wuerfel Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn das Feld bereits belegt ist.
     */
    private void wuerfelAufFeldPlatzieren(FeldName feldName, Wuerfel wuerfel) {
        Feld feld = spiel.getFeldCopilot(feldName);

        if (feld.wuerfelPlatzieren(wuerfel)) {
            removeWuerfel(wuerfel);
            switch (feldName) {
                case KAFFEE_1, KAFFEE_2, KAFFEE_3 ->
                    spiel.getFeldPilot(feldName).setBelegt();
            }
        } else {
            throw new IllegalArgumentException("Feld '" + feldName + "' ist schon belegt!");
        }
    }


    /**
     * Überprüft, ob das Kaffee-Feld für beide Spieler frei ist,
     * und platziert einen Würfel darauf, falls verfügbar.
     * Gleichzeitig wird die Anzahl der Kaffee-Tokens erhöht.
     *
     * @param feldName Der Name des Zielfeldes.
     * @param wuerfel Der zu platzierende Würfel.
     */
    private void kaffeeAufFeldPruefenUndPlatzieren(FeldName feldName, Wuerfel wuerfel) {
        if (!spiel.getFeldPilot(feldName).isBelegt() && !spiel.getFeldCopilot(feldName).isBelegt()) {
            wuerfelAufFeldPlatzieren(feldName, wuerfel);
            KaffeeToken token = new KaffeeToken();
            spiel.kaffeeTokenAnzahlErhoehen(token);
        }
    }

    /**
     * Implementiert die Logik zum Platzieren eines Würfels auf einem Kaffee-Feld.
     * @param wuerfel Der zu platzierende Würfel.
     * @param kaffeeFeld Der Name des Kaffee-Feldes.
     * @throws IllegalArgumentException Wenn die maximale Anzahl an Kaffee-Tokens erreicht ist.
     */
    @Override
    public void kaffeeWaehlen(Wuerfel wuerfel, FeldName kaffeeFeld) {

        if (spiel.getKaffeeTokenCount() >= SpielConstants.MAX_KAFFEE_TOKENS) {
            throw new IllegalArgumentException("Sie können nicht mehr als 3 KaffeeTokens haben!");
        }

        kaffeeAufFeldPruefenUndPlatzieren(kaffeeFeld, wuerfel);

    }

    /**
     * Platziert einen Würfel auf einem der beiden Radio-Felder des Copiloten.
     * @param wuerfel Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn beide Radio-Felder bereits belegt sind.
     */
    @Override
    public void radioWaehlen(Wuerfel wuerfel) {

        if (!spiel.getFeldCopilot(FeldName.RADIO_COPILOT_1).isBelegt()) {
            wuerfelAufFeldPlatzieren(FeldName.RADIO_COPILOT_1, wuerfel);
        } else if (!spiel.getFeldCopilot(FeldName.RADIO_COPILOT_2).isBelegt()) {
            wuerfelAufFeldPlatzieren(FeldName.RADIO_COPILOT_2, wuerfel);
        } else {
            throw new IllegalArgumentException("Beide Radiofelder sind bereits belegt!");
        }
    }

    /**
     * Platziert einen Würfel auf dem Speed-Feld des Copiloten.
     * @param wuerfel Der zu platzierende Würfel.
     */
    @Override
    public void speedWaehlen(Wuerfel wuerfel) {
        wuerfelAufFeldPlatzieren(FeldName.SPEED, wuerfel);
    }

    /**
     * Platziert einen Würfel auf dem Neigungs-Feld des Copiloten.
     * @param wuerfel Der zu platzierende Würfel.
     */
    @Override
    public void neigenWaehlen(Wuerfel wuerfel) {
        wuerfelAufFeldPlatzieren(FeldName.NEIGEN, wuerfel);
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Klappen-Feld des Copiloten basierend auf dem Wert des Würfels.
     * @param wuerfel Der zu platzierende Würfel.
     * @throws IllegalArgumentException Wenn der Würfelwert nicht zu einem Klappen-Feld passt.
     */
    public void klappenWaehlen(Wuerfel wuerfel) {

        switch (wuerfel.getWert()) {
            case 1:
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_1_2, wuerfel);
                break;
            case 2:
                if (!spiel.getFeldCopilot(FeldName.KLAPPEN_1_2).isBelegt()) {
                    wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_1_2, wuerfel);
                    break;
                }
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_2_3, wuerfel);
                break;
            case 3:
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_2_3, wuerfel);
                break;
            case 4:
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_4_5, wuerfel);
                break;
            case 5:
                if (!spiel.getFeldCopilot(FeldName.KLAPPEN_4_5).isBelegt()) {
                    wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_4_5, wuerfel);
                    break;
                }
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_5_6, wuerfel);
                break;
            case 6:
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_5_6, wuerfel);
                break;

            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!");
        }
    }
}
