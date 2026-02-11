package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.feld.Feld;
import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.tokens.KaffeeToken;
import com.teama.skyteam.drop.tokens.Wuerfel;


/**
 * Die Klasse Pilot erweitert die abstrakte Klasse Spieler.
 * Sie implementiert spezifische Logiken, die einem Piloten im Spiel zugeordnet sind,
 * wie das Platzieren von Würfeln auf verschiedenen Spielfeldern.
 */

public class Pilot extends Spieler{

    private Spiel spiel;

    public Pilot(Spiel spiel) {
        super();
        this.spiel = spiel;
    }

    /**
     * Platziert einen Würfel auf einem Spielfeld des Piloten.
     * Falls das Feld Kaffee ist, wird es gleichzeitig für den Copiloten blockiert.
     *
     * @param feldName Der Name des Zielfeldes.
     * @param wuerfel  Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     * @throws IllegalArgumentException Wenn das Feld bereits belegt ist.
     */
    private void wuerfelAufFeldPlatzieren(FeldName feldName, Wuerfel wuerfel, int indexWuerfel) {
        Feld feld = spiel.getFeldPilot(feldName);

        if (feld.wuerfelPlatzieren(wuerfel)) {
            getWuerfels().remove(indexWuerfel);
            switch (feldName) {
                case KAFFEE_1, KAFFEE_2, KAFFEE_3 ->
                    spiel.getFeldCopilot(feldName).setBelegt();
            }

        } else {
            throw new IllegalArgumentException("Feld '" + feldName + "' ist schon belegt!");
        }
    }

    /**
     * Überprüft, ob das Kaffee-Feld für beide Spieler frei ist,
     * und platziert einen Würfel darauf, falls es verfügbar ist.
     * Zusätzlich wird die Anzahl der Kaffee-Tokens erhöht.
     *
     * @param feldName Der Name des Zielfeldes.
     * @param wuerfel  Der zu platzierende Würfel.
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
        if (spiel.getKaffeeTokenCount() >= 3) {
            throw new IllegalArgumentException("Sie können nicht mehr als 3 KaffeeTokens haben!");
        }

        kaffeeAufFeldPruefenUndPlatzieren(kaffeeFeld, wuerfel, indexWuerfel);

    }

    /**
     * Platziert einen Würfel auf dem Radio-Feld.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     */
    @Override
    public void radioWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren(FeldName.RADIO_PILOT, wuerfel, indexWuerfel);
    }

    /**
     * Platziert einen Würfel auf dem Speed-Feld.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     */
    @Override
    public void speedWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren(FeldName.SPEED, wuerfel, indexWuerfel);
    }

    /**
     * Platziert einen Würfel auf dem Neigungs-Feld.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     */
    @Override
    public void neigenWaehlen(Wuerfel wuerfel, int indexWuerfel) {
        wuerfelAufFeldPlatzieren(FeldName.NEIGEN, wuerfel, indexWuerfel);
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Bremsen-Feld basierend auf dem Wert des Würfels.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     * @throws IllegalArgumentException Wenn der Würfelwert nicht zu einem Bremsen-Feld passt.
     */
    public void bremsenWaehlen(Wuerfel wuerfel, int indexWuerfel) {

        switch (wuerfel.getWert()) {
            case 2:
                wuerfelAufFeldPlatzieren(FeldName.BREMSEN_2, wuerfel, indexWuerfel);
                break;
            case 4:
                wuerfelAufFeldPlatzieren(FeldName.BREMSEN_4, wuerfel, indexWuerfel);
                break;
            case 6:
                wuerfelAufFeldPlatzieren(FeldName.BREMSEN_6, wuerfel, indexWuerfel);
                break;
            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!");
        }
    }

    /**
     * Platziert einen Würfel auf einem spezifischen Fahrgestell-Feld basierend auf dem Wert des Würfels.
     * @param wuerfel Der zu platzierende Würfel.
     * @param indexWuerfel Der Index des Würfels in der Liste der Würfel des Spielers.
     * @throws IllegalArgumentException Wenn der Würfelwert nicht zu einem Fahrgestell-Feld passt.
     */
    public void fahrgestellWaehlen(Wuerfel wuerfel, int indexWuerfel) {

        switch (wuerfel.getWert()) {
            case 1,2:
                wuerfelAufFeldPlatzieren(FeldName.FAHRGESTELL_1_2, wuerfel, indexWuerfel);
                break;
            case 3,4:
                wuerfelAufFeldPlatzieren(FeldName.FAHRGESTELL_3_4, wuerfel, indexWuerfel);
                break;
            case 5,6:
                wuerfelAufFeldPlatzieren(FeldName.FAHRGESTELL_5_6, wuerfel, indexWuerfel);
                break;
            default:
                throw new IllegalArgumentException("Ein Würfel mit diesem Wert kann nicht auf diesem Feld platziert werden!");
        }
    }
}
