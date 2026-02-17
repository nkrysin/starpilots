package com.teama.skyteam.drop.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.domain.model.tokens.Dice;
import com.teama.skyteam.drop.ui.constants.TexturePaths;
import com.teama.skyteam.drop.ui.constants.UILayout;
import com.teama.skyteam.drop.ui.factory.GameControlFactory;
import com.teama.skyteam.drop.ui.utilities.SelectedDiceData;
import com.teama.skyteam.drop.ui.utilities.TextureCache;
import com.teama.skyteam.drop.ui.utilities.TextureUtils;

import java.util.ArrayList;
import java.util.List;

public class DiceInputHandler {
    private final Stage stage;
    private final GameControlFactory gameControlFactory;

    private ArrayList<ImageButton> pilotDiceActors = new ArrayList<>();
    private ArrayList<ImageButton> copilotDiceActors = new ArrayList<>();
    private SelectedDiceData selectedDiceData = null;

    public DiceInputHandler(Stage stage, GameControlFactory gameControlFactory) {
        this.stage = stage;
        this.gameControlFactory = gameControlFactory;
    }

    public SelectedDiceData getSelectedDiceData() {
        return selectedDiceData;
    }

    public void clearSelectedDiceData() {
        selectedDiceData = null;
    }



    /**
     * Erstellt grafische Würfel für den angegebenen Player und fügt sie der Bühne hinzu.
     *
     * @param player Der Player, für den die Würfel erstellt werden.
     */
    public void createDiceForPlayer(Player player) {
        if (player instanceof Pilot) {
            for (ImageButton oldButton : pilotDiceActors) {
                oldButton.remove();
            }
            pilotDiceActors.clear();
        } else {
            for (ImageButton oldButton : copilotDiceActors) {
                oldButton.remove();
            }
            copilotDiceActors.clear();
        }

        ArrayList<Dice> dice = player.getAllDice();
        ArrayList<ImageButton> diceActors = new ArrayList<>();

        for (int i = 0; i < dice.size(); i++) {
            Dice d = dice.get(i);
            int value = d.getValue();
            String textureName;
            int x, y;
            if (player instanceof Pilot) {
                textureName = TexturePaths.DICE_PATH_PILOT + value + TexturePaths.IMG_FORMAT;
                x = UILayout.PILOT_DICE_COORDINATES[i][0];
                y = UILayout.PILOT_DICE_COORDINATES[i][1];
            } else {
                textureName = TexturePaths.DICE_PATH_COPILOT + value + TexturePaths.IMG_FORMAT;
                x = UILayout.COPILOT_DICE_COORDINATES[i][0];
                y = UILayout.COPILOT_DICE_COORDINATES[i][1];
            }
            ImageButton wuerfelButton = gameControlFactory.createButton(textureName, x, y, UILayout.DICE_SIZE[0], UILayout.DICE_SIZE[1]);
            diceActors.add(wuerfelButton);
            stage.addActor(wuerfelButton);
        }

        if (player instanceof Pilot) {
            pilotDiceActors = diceActors;
        } else {
            copilotDiceActors =diceActors;
        }
        hideDice(player);
    }

    /**
     * Fügt den Würfeln eines Spielers Click-Listener hinzu.
     *
     * @param player Der Player, dessen Würfel interaktiv gemacht werden.
     */
    public void addDiceListenersForSpieler(Player player) {
        ArrayList<ImageButton> wuerfelActors = (player instanceof Pilot) ? pilotDiceActors : copilotDiceActors;
        ArrayList<Dice> dice = player.getAllDice();
        final ImageButton[] prevSelectedButton = {null};
        final String[] prevNormalTextureName = {null};

        for (int i = 0; i < wuerfelActors.size(); i++) {
            final ImageButton wuerfelButton = wuerfelActors.get(i);
            final Dice d = dice.get(i);
            final int value = d.getValue();
            final boolean isPilot = (player instanceof Pilot);
            final String normalTextureName;
            final String selectedTextureName;
            if (isPilot) {
                normalTextureName = TexturePaths.DICE_PATH_PILOT + value + TexturePaths.IMG_FORMAT;
                selectedTextureName = TexturePaths.DICE_PATH_PILOT + value +  TexturePaths.DICE_PATH_SELECTED;
            } else {
                normalTextureName = TexturePaths.DICE_PATH_COPILOT + value + TexturePaths.IMG_FORMAT;
                selectedTextureName = TexturePaths.DICE_PATH_COPILOT + value +  TexturePaths.DICE_PATH_SELECTED;
            }
            final int diceIndex = i;
            wuerfelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (prevSelectedButton[0] == wuerfelButton) {
                        return;
                    }
                    if (prevSelectedButton[0] != null && prevNormalTextureName[0] != null) {
                        TextureUtils.switchTexture(prevSelectedButton[0], prevNormalTextureName[0]);
                    }
                    TextureUtils.switchTexture(wuerfelButton, selectedTextureName);
                    prevSelectedButton[0] = wuerfelButton;
                    prevNormalTextureName[0] = normalTextureName;
                    selectedDiceData = new SelectedDiceData(player, diceIndex, d);
                }
            });
        }
    }

    /**
     * Entfernt den Button des Würfels aus der Hand eines Spielers.
     *
     * @param diceData Die Daten des Würfels, der entfernt werden soll.
     */
    public void removeDiceButtonFromHand(SelectedDiceData diceData) {
        Player owner = diceData.getPlayer();
        int diceIndex = diceData.getDiceIndex();
        List<ImageButton> diceActors = (owner instanceof Pilot) ? pilotDiceActors : copilotDiceActors;
        if (diceIndex >= 0 && diceIndex < diceActors.size()) {
            ImageButton diceButton = diceActors.get(diceIndex);
            diceButton.remove();
            diceActors.remove(diceIndex);
        }
    }

    /**
     * Entfernt alle Listener von den Würfeln eines Spielers.
     *
     * @param player Der Player, dessen Würfel zurückgesetzt werden sollen.
     */
    public void clearDiceListeners(Player player) {
        ArrayList<ImageButton> wuerfelActors = (player instanceof Pilot) ? pilotDiceActors : copilotDiceActors;
        for (ImageButton ib : wuerfelActors) {
            ib.clearListeners();
        }
    }


    /**
     * Fügt ein Bild hinzu, das die Würfel an der angegebenen Position abdeckt.
     *
     * @param x Die X-Koordinate der Position.
     * @param y Die Y-Koordinate der Position.
     */
    private void renderHideImage(float x, float y) {
        Image hideWuerfelImage = new Image(TextureCache.get(TexturePaths.DICE_COVER_PATH));
        hideWuerfelImage.setPosition(x, y);
        stage.addActor(hideWuerfelImage);
    }

    /**
     * Entfernt das Bild, das die Würfel an der angegebenen Position abdeckt.
     *
     * @param x Die X-Koordinate der Position.
     * @param y Die Y-Koordinate der Position.
     */
    private void removeHideImage(float x, float y) {
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Image && actor.getX() == x && actor.getY() == y) {
                actor.remove();
                break;
            }
        }
    }

    /**
     * Entfernt alle Bilder, die die Würfel verbergen.
     */
    public void removeAllHideImages() {
        removeHideImage(UILayout.PILOT_HIDING_SCREEN_COORDINATES[0], UILayout.PILOT_HIDING_SCREEN_COORDINATES[1]);
        removeHideImage(UILayout.COPILOT_HIDING_SCREEN_COORDINATES[0], UILayout.COPILOT_HIDING_SCREEN_COORDINATES[1]);
    }

    /**
     * Blendet die Würfel des anderen Spielers aus, um sie zu verbergen.
     *
     * @param player Der aktuelle Player, dessen Würfel sichtbar bleiben sollen.
     */
    public void hideDice(Player player) {
        if (player instanceof Pilot) {
            renderHideImage(UILayout.COPILOT_HIDING_SCREEN_COORDINATES[0], UILayout.COPILOT_HIDING_SCREEN_COORDINATES[1]);
            removeHideImage(UILayout.PILOT_HIDING_SCREEN_COORDINATES[0], UILayout.PILOT_HIDING_SCREEN_COORDINATES[1]);
        } else {
            renderHideImage(UILayout.PILOT_HIDING_SCREEN_COORDINATES[0], UILayout.PILOT_HIDING_SCREEN_COORDINATES[1]);
            removeHideImage(UILayout.COPILOT_HIDING_SCREEN_COORDINATES[0], UILayout.COPILOT_HIDING_SCREEN_COORDINATES[1]);
        }
    }

    @FunctionalInterface
    public interface DiceSelectionListener {
        void onDiceSelected(ImageButton button, Dice dice, int index);
    }

    public void addDiceSelectionListeners(Player player, DiceSelectionListener listener) {
        ArrayList<ImageButton> diceActors = (player instanceof Pilot) ? pilotDiceActors : copilotDiceActors;
        ArrayList<Dice> dices = player.getAllDice();
        for (int i = 0; i < diceActors.size(); i++) {
            final ImageButton button = diceActors.get(i);
            final Dice dice = dices.get(i);
            final int index = i;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    listener.onDiceSelected(button, dice, index);
                }
            });
        }
    }

    public void dispose() {
        for (ImageButton button : pilotDiceActors) {
            button.remove();
        }
        pilotDiceActors.clear();

        for (ImageButton button : copilotDiceActors) {
            button.remove();
        }
        copilotDiceActors.clear();

        removeAllHideImages();
        selectedDiceData = null;
    }

}
