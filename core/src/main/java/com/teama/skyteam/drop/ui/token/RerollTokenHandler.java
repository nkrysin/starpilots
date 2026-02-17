package com.teama.skyteam.drop.ui.token;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.ui.buttons.ControlField;
import com.teama.skyteam.drop.ui.constants.TexturePaths;
import com.teama.skyteam.drop.ui.dialog.DialogManager;
import com.teama.skyteam.drop.ui.input.DiceInputHandler;
import com.teama.skyteam.drop.ui.utilities.TextureUtils;

public class RerollTokenHandler {
    private ControlField reroll;
    private Game game;
    private DialogManager dialogManager;
    private DiceInputHandler diceInputHandler;
    private GameStateManager gameStateManager;

    public RerollTokenHandler(ControlField reroll, Game game, DialogManager dialogManager, DiceInputHandler diceInputHandler, GameStateManager gameStateManager) {
        this.reroll = reroll;
        this.game = game;
        this.dialogManager = dialogManager;
        this.diceInputHandler = diceInputHandler;
        this.gameStateManager = gameStateManager;
    }

    /**
     * Zeigt oder versteckt den Button für den "Neu Wurf"-Token.
     *
     * @param available Gibt an, ob der Button aktiv oder deaktiviert sein soll.
     */
    public void showReroll(Boolean available) {
        int rerollTokenCount = game.getRerollTokenCount();
        ImageButton button = reroll.getButton();
        if (rerollTokenCount == 0) {
            TextureUtils.switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            button.setTouchable(Touchable.disabled);
        } else {
            if (available) {
                button.setTouchable(Touchable.enabled);
                TextureUtils.switchTexture(button, TexturePaths.REROLL_AVAILABLE_PATH);
            } else {
                TextureUtils.switchTexture(button, TexturePaths.REROLL_PATH);
            }
        }
    }

    /**
     * Aktiviert die Verwendung eines "Neu Wurf"-Tokens für den Player.
     *
     * @param player Der Player, der ein "Neu Wurf"-Token verwenden möchte.
     */
    public void useReroll(Player player) {
        ImageButton button = reroll.getButton();
        button.clearListeners();
        if (game.getRerollTokenCount() > 0) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    TextureUtils.switchTexture(button, TexturePaths.REROLL_SELECTED_PATH);
                    dialogManager.createRerollTokenWindow(player, gameStateManager, (p1, p2) -> {
                        if (p1 != null) {
                            diceInputHandler.createDiceForPlayer(p1);
                            diceInputHandler.addDiceListenersForSpieler(p1);
                        }
                        if (p2 != null) {
                            diceInputHandler.createDiceForPlayer(p2);
                            diceInputHandler.addDiceListenersForSpieler(p2);
                        }
                        if (game.getRerollTokenCount() == 0) {
                            TextureUtils.switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                            button.setTouchable(Touchable.disabled);
                        } else {
                            button.setTouchable(Touchable.enabled);
                            TextureUtils.switchTexture(button, TexturePaths.REROLL_AVAILABLE_PATH);
                        }
                        diceInputHandler.removeAllHideImages();
                        if( (player instanceof Pilot) ){
                            diceInputHandler.hideDice(player);
                        } else {
                            diceInputHandler.hideDice(game.getCopilot());
                        }
                    });
                }
            });
        }
    }

    /**
     * Gibt alle verwendeten Ressourcen frei und entfernt alle GUI-Elemente.
     */
    public void dispose() {
        if (reroll != null && reroll.getButton() != null) {
            ImageButton button = reroll.getButton();
            button.remove();
            reroll = null;
        }
    }
}
