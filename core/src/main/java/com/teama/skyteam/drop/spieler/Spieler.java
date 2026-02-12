package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.spiel.SpielConstants;
import com.teama.skyteam.drop.tokens.KaffeeToken;
import com.teama.skyteam.drop.tokens.KaffeeTokenAktion;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;
/**
 * Die abstrakte Klasse Spieler repräsentiert die grundlegenden Möglichkeiten, die für Co-/Pilot ähnlich sind.
 * Sie enthält die grundlegenden Mechaniken, die Spieler mit Würfeln und speziellen Token interagieren lassen.
 */

public abstract class Spieler {

    private ArrayList<Wuerfel> wuerfels = new ArrayList<Wuerfel>(SpielConstants.MAX_WUERFEL);

    public Spieler() {
    }

    public ArrayList<Wuerfel> getWuerfels() {
        return wuerfels;
    }

    public Wuerfel getWuerfel(Wuerfel wuerfel) {
        return wuerfels.stream()
            .filter(w -> w.equals(wuerfel))
            .findFirst()
            .orElse(null);
    }

    public boolean removeWuerfel(Wuerfel wuerfel) {
        return this.wuerfels.remove(wuerfel);
    }

    public void setWuerfels(ArrayList<Wuerfel> wuerfels) {
        this.wuerfels = wuerfels;
    }

    public int getAnzahlWuerfels() {
        return wuerfels.size();
    }

    /**
     * Führt ein erneutes Würfeln für einen Zielspieler mithilfe eines NeuWurfToken durch.
     *
     * @param token  Das Token, das das erneute Würfeln erlaubt.
     * @param target Der Zielspieler, dessen Würfel neu geworfen werden sollen.
     */
    public void neuWurfTokenBenutzen(NeuWurfToken token, Spieler target) {
        target.setWuerfels(token.erneutWerfen(target));
    }

    /**
     * Führt ein erneutes Würfeln für zwei Zielspieler mithilfe eines `NeuWurfToken` durch.
     *
     * @param token   Das Token, das das erneute Würfeln erlaubt.
     * @param target1 Der erste Zielspieler.
     * @param target2 Der zweite Zielspieler.
     */
    public void neuWurfTokenBenutzen(NeuWurfToken token, Spieler target1, Spieler target2) {
        target1.setWuerfels(token.erneutWerfen(target1));
        target2.setWuerfels(token.erneutWerfen(target2));
    }

    /**
     * Verwendet ein `KaffeeToken`, um den Wert eines Würfels zu ändern.
     *
     * @param token         Das `KaffeeToken`, das die Änderung ermöglicht.
     * @param aktion        Die Aktion, die auf den Würfel angewendet werden soll (z. B. PLUS oder MINUS).
     * @throws IllegalArgumentException Wenn der Würfelwert bereits maximal (6) oder minimal (1) ist.
     */
    public void kaffeeTokenBenutzen(KaffeeToken token, KaffeeTokenAktion aktion, Wuerfel wuerfel) {
        int index = wuerfels.indexOf(wuerfel);
        Wuerfel aktueller = wuerfels.get(index);
        if (aktion == KaffeeTokenAktion.PLUS && aktueller.getWert() == SpielConstants.MAX_WUERFEL_VALUE) {
            throw new IllegalArgumentException("Wuerfelwert ist schon maximal");
        } else if (aktion == KaffeeTokenAktion.MINUS && aktueller.getWert() == SpielConstants.MIN_WUERFEL_VALUE) {
            throw new IllegalArgumentException("Wuerfelwert ist schon minimal");
        }
        Wuerfel neuer = token.wertAendern(aktion, aktueller);
        wuerfels.set(index, neuer);
    }

    //Abstrakte Methoden, die weiter in den Co-/Pilot Klassen implementiert sind.

    protected abstract void kaffeeWaehlen(Wuerfel wuerfel, FeldName kaffeeFeld);

    protected abstract void radioWaehlen(Wuerfel wuerfel);

    protected abstract void speedWaehlen(Wuerfel wuerfel);

    protected abstract void neigenWaehlen(Wuerfel wuerfel);
}
