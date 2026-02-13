package com.teama.skyteam.drop.domain.model.tokens;

import com.teama.skyteam.drop.domain.model.spieler.Spieler;

import java.util.ArrayList;

public class NeuWurfToken {

    /**
     * Diese Methode erzeugt neue Würfel für den Spieler basierend auf der Anzahl seiner Würfel.
     *
     * @param spieler Der Spieler, dessen Würfel neu geworfen werden sollen.
     * @return Eine ArrayList von neuen Wuerfel-Objekten, wobei jeder Würfel ein neues Ergebnis darstellt.
     *
     * Funktionsweise:
     * Für jeden Würfel des Spielers wird ein neues Wuerfel-Objekt erstellt und zur Liste hinzugefügt.
     */

    public ArrayList<Wuerfel> erneutWerfen(Spieler spieler) {
        int anzahlWuerfel = spieler.getAnzahlWuerfels();
        ArrayList<Wuerfel> neuWurfe = new ArrayList<Wuerfel>();
        for (int i = 0; i < anzahlWuerfel; i++) {
            neuWurfe.add(new Wuerfel());
        }
        return neuWurfe;
    }


}
