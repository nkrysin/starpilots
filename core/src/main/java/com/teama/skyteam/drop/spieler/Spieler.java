package com.teama.skyteam.drop.spieler;

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

    private ArrayList<Wuerfel> wuerfels = new ArrayList<Wuerfel>(4);

    public Spieler() {
    }

    public ArrayList<Wuerfel> getWuerfels() {
        return wuerfels;
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
     * @param indexWuerfel  Der Index des zu ändernden Würfels in der Liste.
     * @param aktion        Die Aktion, die auf den Würfel angewendet werden soll (z. B. PLUS oder MINUS).
     * @throws IllegalArgumentException Wenn der Würfelwert bereits maximal (6) oder minimal (1) ist.
     */
    public void kaffeeTokenBenutzen(KaffeeToken token, int indexWuerfel, KaffeeTokenAktion aktion) {
        Wuerfel wuerfel = wuerfels.get(indexWuerfel);
        if (aktion == KaffeeTokenAktion.PLUS && wuerfel.getWert() == 6) {
            throw new IllegalArgumentException("Wuerfelwert ist schon maximal");
        } else if (aktion == KaffeeTokenAktion.MINUS && wuerfel.getWert() == 1) {
            throw new IllegalArgumentException("Wuerfelwert ist schon minimal");
        } else {
            wuerfel = token.wertAendern(aktion, wuerfel);
            wuerfels.set(indexWuerfel, wuerfel);
        }
    }

    //Abstrakte Methoden, die weiter in den Co-/Pilot Klassen implementiert sind.

    protected abstract void kaffeeWaehlen(Wuerfel wuerfel, int indexWuerfel, String kaffeeFeld);

    protected abstract void radioWaehlen(Wuerfel wuerfel, int indexWuerfel);

    protected abstract void speedWaehlen(Wuerfel wuerfel, int indexWuerfel);

    protected abstract void neigenWaehlen(Wuerfel wuerfel, int indexWuerfel);
}
