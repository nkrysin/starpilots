package com.teama.starpilots.drop.domain.model.tokens;

import com.teama.starpilots.drop.domain.model.player.Player;

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

    public ArrayList<Dice> reroll(Player player) {
        int anzahlWuerfel = player.getAllDiceCount();
        ArrayList<Dice> neuWurfe = new ArrayList<Dice>();
        for (int i = 0; i < anzahlWuerfel; i++) {
            neuWurfe.add(new Dice());
        }
        return neuWurfe;
    }


}
