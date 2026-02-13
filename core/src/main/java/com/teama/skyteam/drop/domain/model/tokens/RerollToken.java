package com.teama.skyteam.drop.domain.model.tokens;

import com.teama.skyteam.drop.domain.model.player.Player;

import java.util.ArrayList;

public class RerollToken {

    /**
     * Diese Methode erzeugt neue Würfel für den Player basierend auf der Anzahl seiner Würfel.
     *
     * @param player Der Player, dessen Würfel neu geworfen werden sollen.
     * @return Eine ArrayList von neuen Dice-Objekten, wobei jeder Würfel ein neues Ergebnis darstellt.
     *
     * Funktionsweise:
     * Für jeden Würfel des Spielers wird ein neues Dice-Objekt erstellt und zur Liste hinzugefügt.
     */

    public ArrayList<Dice> erneutWerfen(Player player) {
        int anzahlWuerfel = player.getAnzahlWuerfels();
        ArrayList<Dice> neuWurfe = new ArrayList<Dice>();
        for (int i = 0; i < anzahlWuerfel; i++) {
            neuWurfe.add(new Dice());
        }
        return neuWurfe;
    }


}
