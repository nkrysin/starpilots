package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.feld.Feld;
import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.SpielConstants;
import com.teama.skyteam.drop.tokens.KaffeeToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

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
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     * @throws IllegalArgumentException Wenn das Feld bereits belegt ist.
     */
    private void wuerfelAufFeldPlatzieren(FeldName feldName, Wuerfel wuerfel, int indexWuerfel) {
        Feld feld = spiel.getFeldCopilot(feldName);

        if (feld.wuerfelPlatzieren(wuerfel)) {
            getWuerfels().remove(indexWuerfel);
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
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     */
    private void kaffeeAufFeldPruefenUndPlatzieren(FeldName feldName, Wuerfel wuerfel, int indexWuerfel) {
        if (!spiel.getFeldPilot(feldName).isBelegt() && !spiel.getFeldCopilot(feldName).isBelegt()) {
            wuerfelAufFeldPlatzieren(feldName, wuerfel, indexWuerfel);
            KaffeeToken token = new KaffeeToken();
            spiel.kaffeeTokenAnzahlErhoehen(token);
        }
    }

    /**
     * Implementiert die Logik zum Platzieren eines Würfels auf einem Kaffee-Feld.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     * @param kaffeeFeld Der Name des Kaffee-Feldes.
     * @throws IllegalArgumentException Wenn die maximale Anzahl an Kaffee-Tokens erreicht ist.
     */
    @Override
    public void kaffeeWaehlen(Wuerfel wuerfel, int indexWuerfel, FeldName kaffeeFeld) {

        if (spiel.getKaffeeTokenCount() >= SpielConstants.MAX_KAFFEE_TOKENS) {
            throw new IllegalArgumentException("Sie können nicht mehr als 3 KaffeeTokens haben!");
        }

        kaffeeAufFeldPruefenUndPlatzieren(kaffeeFeld, wuerfel, indexWuerfel);

    }

    /**
     * Platziert einen Würfel auf einem der beiden Radio-Felder des Copiloten.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     * @throws IllegalArgumentException Wenn beide Radio-Felder bereits belegt sind.
     */
    @Override
    public void radioWaehlen(Wuerfel wuerfel, int indexWuerfel) {

        if (!spiel.getFeldCopilot(FeldName.RADIO_COPILOT_1).isBelegt()) {
            wuerfelAufFeldPlatzieren(FeldName.RADIO_COPILOT_1, wuerfel, indexWuerfel);
        } else if (!spiel.getFeldCopilot(FeldName.RADIO_COPILOT_2).isBelegt()) {
            wuerfelAufFeldPlatzieren(FeldName.RADIO_COPILOT_2, wuerfel, indexWuerfel);
        } else {
            throw new IllegalArgumentException("Beide Radiofelder sind bereits belegt!");
        }
    }

    /**
     * Platziert einen Würfel auf dem Speed-Feld des Copiloten.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     */
    @Override
    public void speedWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren(FeldName.SPEED, wuerfel, indexWuerfel);
    }

    /**
     * Platziert einen Würfel auf dem Neigungs-Feld des Copiloten.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     */
    @Override
    public void neigenWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren(FeldName.NEIGEN, wuerfel, indexWuerfel);
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Klappen-Feld des Copiloten basierend auf dem Wert des Würfels.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     * @throws IllegalArgumentException Wenn der Würfelwert nicht zu einem Klappen-Feld passt.
     */
    public void klappenWaehlen(Wuerfel wuerfel, int indexWuerfel) {

        switch (wuerfel.getWert()) {
            case 1:
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_1_2, wuerfel, indexWuerfel);
                break;
            case 2:
                if (!spiel.getFeldCopilot(FeldName.KLAPPEN_1_2).isBelegt()) {
                    wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_1_2, wuerfel, indexWuerfel);
                    break;
                }
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_2_3, wuerfel, indexWuerfel);
                break;
            case 3:
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_2_3, wuerfel, indexWuerfel);
                break;
            case 4:
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_4_5, wuerfel, indexWuerfel);
                break;
            case 5:
                if (!spiel.getFeldCopilot(FeldName.KLAPPEN_4_5).isBelegt()) {
                    wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_4_5, wuerfel, indexWuerfel);
                    break;
                }
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_5_6, wuerfel, indexWuerfel);
                break;
            case 6:
                wuerfelAufFeldPlatzieren(FeldName.KLAPPEN_5_6, wuerfel, indexWuerfel);
                break;

            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!");
        }
    }
}
