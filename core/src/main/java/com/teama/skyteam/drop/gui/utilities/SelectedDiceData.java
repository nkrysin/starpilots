/**
 * Repräsentiert die Daten eines ausgewählten Würfels im Game.
 *
 * Die Klasse `SelectedDiceData` speichert Informationen über den Player, der den Würfel ausgewählt hat,
 * den Index des Würfels und die zugehörige Würfelinstanz. Außerdem wird der Pfad zur Textur des Würfels
 * basierend auf dem Würfelwert und dem Spielertyp (Pilot oder Copilot) generiert.
 */
package com.teama.skyteam.drop.gui.utilities;

import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class SelectedDiceData {

    private final Player player;
    private final int diceIndex;
    private final Dice dice;
    private final String diceTexturePath;

    /**
     * Konstruktor zur Initialisierung der Daten des ausgewählten Würfels.
     *
     * @param player der Player, der den Würfel ausgewählt hat.
     * @param diceIndex der Index des ausgewählten Würfels.
     * @param dice die Instanz des ausgewählten Würfels.
     */
    public SelectedDiceData(Player player, int diceIndex, Dice dice) {
        this.player = player;
        this.diceIndex = diceIndex;
        this.dice = dice;

        int wert = dice.getValue();
        boolean isPilot = player instanceof Pilot;
        if (isPilot) {
            this.diceTexturePath = TexturePaths.DICE_PATH_PILOT + wert + TexturePaths.IMG_FORMAT;
        } else {
            this.diceTexturePath = TexturePaths.DICE_PATH_COPILOT + wert + TexturePaths.IMG_FORMAT;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getDiceIndex() {
        return diceIndex;
    }

    public Dice getDice() {
        return dice;
    }


    public String getDiceTexturePath() {
        return diceTexturePath;
    }
}
