/**
 * Repräsentiert die Daten eines ausgewählten Würfels im Game.
 *
 * Die Klasse `SelectedWuerfelData` speichert Informationen über den Player, der den Würfel ausgewählt hat,
 * den Index des Würfels und die zugehörige Würfelinstanz. Außerdem wird der Pfad zur Textur des Würfels
 * basierend auf dem Würfelwert und dem Spielertyp (Pilot oder Copilot) generiert.
 */
package com.teama.skyteam.drop.gui.utilities;

import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class SelectedWuerfelData {

    private final Player player;
    private final int wuerfelIndex;
    private final Dice dice;
    private final String diceTexturePath;

    /**
     * Konstruktor zur Initialisierung der Daten des ausgewählten Würfels.
     *
     * @param player der Player, der den Würfel ausgewählt hat.
     * @param wuerfelIndex der Index des ausgewählten Würfels.
     * @param dice die Instanz des ausgewählten Würfels.
     */
    public SelectedWuerfelData(Player player, int wuerfelIndex, Dice dice) {
        this.player = player;
        this.wuerfelIndex = wuerfelIndex;
        this.dice = dice;

        int wert = dice.getValue();
        boolean isPilot = player instanceof Pilot;
        if (isPilot) {
            this.diceTexturePath = TexturePaths.WUERFEL_PATH_PILOT + wert + TexturePaths.IMG_FORMAT;
        } else {
            this.diceTexturePath = TexturePaths.WUERFEL_PATH_COPILOT + wert + TexturePaths.IMG_FORMAT;
        }
    }

    public Player getSpieler() {
        return player;
    }

    public int getWuerfelIndex() {
        return wuerfelIndex;
    }

    public Dice getWuerfel() {
        return dice;
    }


    public String getDiceTexturePath() {
        return diceTexturePath;
    }
}
