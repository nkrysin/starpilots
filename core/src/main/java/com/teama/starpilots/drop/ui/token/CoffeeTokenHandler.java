package com.teama.starpilots.drop.ui.token;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teama.starpilots.drop.application.gameservice.GameService;
import com.teama.starpilots.drop.application.gamestate.GameStateManager;
import com.teama.starpilots.drop.application.logic.fields.FieldManager;
import com.teama.starpilots.drop.domain.action.command.ActionResult;
import com.teama.starpilots.drop.domain.action.type.ActionName;
import com.teama.starpilots.drop.domain.model.game.Game;
import com.teama.starpilots.drop.domain.model.player.Pilot;
import com.teama.starpilots.drop.domain.model.player.Player;
import com.teama.starpilots.drop.domain.model.tokens.Dice;
import com.teama.starpilots.drop.ui.buttons.ControlField;
import com.teama.starpilots.drop.ui.buttons.ControlGroup;
import com.teama.starpilots.drop.ui.constants.TexturePaths;
import com.teama.starpilots.drop.ui.dialog.DialogManager;
import com.teama.starpilots.drop.ui.input.DiceInputHandler;
import com.teama.starpilots.drop.ui.utilities.PlayerActionCallback;
import com.teama.starpilots.drop.ui.utilities.SelectedDiceData;
import com.teama.starpilots.drop.ui.utilities.TextureUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CoffeeTokenHandler {
    private final DiceInputHandler diceInputHandler;
    private final DialogManager dialogManager;
    private final Game game;
    private final GameService gameService;
    private final GameStateManager gameStateManager;
    private final FieldManager fieldManager;

    private final ControlGroup pilotCoffeeControlGroup;
    private final ControlGroup copilotCoffeeControlGroup;
    private final ControlGroup pilotUseCoffeeControlGroup;
    private final ControlGroup copilotUseCoffeeControlGroup;
    private final Map<ControlField, ControlField> mirroredFields = new HashMap<>();

    private Consumer<Player> onFieldSituationCheck;
    private PlayerActionCallback playerActionCallback;

    public CoffeeTokenHandler(
        ControlGroup pilotCoffeeControlGroup,
        ControlGroup copilotCoffeeControlGroup,
        ControlGroup pilotUseCoffeeControlGroup,
        ControlGroup copilotUseCoffeeControlGroup,
        DiceInputHandler diceInputHandler,
        DialogManager dialogManager,
        Game game,
        GameService gameService,
        GameStateManager gameStateManager,
        FieldManager fieldManager
    ) {
        this.pilotCoffeeControlGroup = pilotCoffeeControlGroup;
        this.copilotCoffeeControlGroup = copilotCoffeeControlGroup;
        this.pilotUseCoffeeControlGroup = pilotUseCoffeeControlGroup;
        this.copilotUseCoffeeControlGroup = copilotUseCoffeeControlGroup;
        this.diceInputHandler = diceInputHandler;
        this.dialogManager = dialogManager;
        this.game = game;
        this.gameService = gameService;
        this.gameStateManager = gameStateManager;
        this.fieldManager = fieldManager;
        initMirrors();
    }

    public void setOnFieldSituationCheck(Consumer<Player> callback) {
        this.onFieldSituationCheck = callback;
    }

    public void setPlayerActionCallback(PlayerActionCallback callback) {
        this.playerActionCallback = callback;
    }

    /**
     * Aktiviert die Kontrollgruppen für die Kaffeeaktionen eines Spielers.
     *
     * @param current Der aktuelle Player, dessen Kaffeeaktionen aktiviert werden.
     */
    public void setCoffeeGroupsActive(Player current) {
        boolean isPilot = current instanceof Pilot;
        for (ControlField cf : pilotCoffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.enabled : Touchable.disabled);
        }
        for (ControlField cf : pilotUseCoffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.enabled : Touchable.disabled);
        }
        for (ControlField cf : copilotCoffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.disabled : Touchable.enabled);
        }
        for (ControlField cf : copilotUseCoffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.disabled : Touchable.enabled);
        }
    }

    /**
     * Zeigt die Kaffee-Kontrollgruppen an oder blendet sie aus.
     *
     * @param pilotAvailable   Gibt an, ob die Kaffeegruppen des Piloten aktiv sein sollen.
     * @param copilotAvailable Gibt an, ob die Kaffeegruppen des Copiloten aktiv sein sollen.
     */
    public void showCoffee(Boolean pilotAvailable, Boolean copilotAvailable) {
        int kaffeeCount = game.getCoffeeTokenCount();
        ControlGroup pilotUseGroup = getPilotUseCoffeeControlGroup();
        if (!pilotAvailable) {
            for (ControlField field : pilotUseGroup.getAllFields()) {
                TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                field.getButton().setTouchable(Touchable.disabled);
            }
        } else {
            for (int i = 1; i <= 3; i++) {
                ControlField field = pilotUseGroup.getFieldByAction(
                    ActionName.useCoffee(i)
                );

                if (field != null) {
                    if (i <= kaffeeCount) {
                        field.setOccupied(true);
                        field.getButton().setTouchable(Touchable.enabled);
                        TextureUtils.switchTexture(field.getButton(), TexturePaths.COFFEE_PATH);
                    } else {
                        field.setOccupied(false);
                        TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                        field.getButton().setTouchable(Touchable.disabled);
                    }
                }
            }
        }

        ControlGroup copilotUseGroup = getCopilotUseCoffeeControlGroup();
        if (!copilotAvailable) {
            for (ControlField field : copilotUseGroup.getAllFields()) {
                TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                field.getButton().setTouchable(Touchable.disabled);
            }
        } else {
            for (int i = 1; i <= 3; i++) {
                ControlField field = copilotUseGroup.getFieldByAction(
                    ActionName.useCoffee(i)
                );

                if (field != null) {
                    if (i <= kaffeeCount) {
                        field.setOccupied(true);
                        field.getButton().setTouchable(Touchable.enabled);
                        TextureUtils.switchTexture(field.getButton(), TexturePaths.COFFEE_PATH);
                    } else {
                        field.setOccupied(false);
                        TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                        field.getButton().setTouchable(Touchable.disabled);
                    }
                }
            }
        }
    }


    /**
     * Aktiviert die Verwendung eines Kaffee-Tokens für einen Player.
     *
     * @param s Der Player, der ein Kaffee-Token verwenden möchte.
     */
    public void useCoffee(Player s) {
        int coffeeTokenCount = game.getCoffeeTokenCount();
        ControlGroup useGroup = getUseCoffeeControlGroup(s);

        if (coffeeTokenCount > 0) {
            ControlField field = useGroup.getFieldByAction(
                ActionName.useCoffee(coffeeTokenCount)
            );
            if (field != null && field.isOccupied()) {
                field.getButton().clearListeners();
                field.getButton().setTouchable(Touchable.enabled);
                TextureUtils.switchTexture(field.getButton(), TexturePaths.COFFEE_AVAILABLE_PATH);
                field.getButton().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        TextureUtils.switchTexture(field.getButton(), TexturePaths.COFFEE_SELECTED_PATH);
                        dialogManager.createMessage("Kaffee-Token verwenden", "Bitte wählen Sie einen Würfel aus, dessen Wert Sie ändern möchten!");
                        waitForDiceSelection(s, field.getAction());
                    }
                });
            }
        } else {
            for (ControlField f : useGroup.getAllFields()) {
                f.getButton().clearListeners();
                TextureUtils.switchTexture(f.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                f.setOccupied(false);
            }
        }
    }

    /**
     * Fügt Click-Listener zu den Kaffee-Kontrollfeldern eines Spielers hinzu.
     *
     * @param player Der Player, dessen Kaffee-Kontrollfelder aktiviert werden sollen.
     */
    public void addCoffeeListeners(Player player) {
        ControlGroup kaffeeGroup = getCoffeeControlGroup(player);
        highlightAvailableFields(kaffeeGroup);
        for (ControlField field : kaffeeGroup.getAvailableFields()) {
            field.getButton().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleCoffeeFieldClick(field, player);
                }
            });
        }

    }

    /**
     * Behandelt die Aktion beim Klicken auf ein Kaffee-Kontrollfeld.
     *
     * @param field  Das angeklickte Kontrollfeld.
     * @param player Der Player, der die Aktion ausführt.
     */
    private void handleCoffeeFieldClick(ControlField field, Player player) {
        if (diceInputHandler.getSelectedDiceData() == null || field.isOccupied()) {
            return;
        }

        Dice d = diceInputHandler.getSelectedDiceData().getDice();
        boolean result = executeStep(field, player, d);

        if (!result) {
            return;
        }

        SelectedDiceData data = diceInputHandler.getSelectedDiceData();

        ControlField mirroredField = mirroredFields.get(field);
        blockFieldForBoth(field, mirroredField, data);
        diceInputHandler.removeDiceButtonFromHand(data);

        field.setOccupied(true);
        if (mirroredField != null) {
            mirroredField.setOccupied(true);
        }
        diceInputHandler.clearSelectedDiceData();
        if (playerActionCallback != null) {
            playerActionCallback.onPlayerActionCompleted(player);
        }

    }

    /**
     * Blockiert ein Field für beide Player (Pilot und Copilot) und markiert es als belegt.
     *
     * @param fieldPilot   Das Kontrollfeld des Piloten.
     * @param fieldCopilot Das entsprechende Kontrollfeld des Copiloten.
     * @param data         Die Daten des ausgewählten Würfels.
     */
    private void blockFieldForBoth(ControlField fieldPilot, ControlField fieldCopilot, SelectedDiceData data) {
        String texture = data.getDiceTexturePath();

        TextureUtils.switchTexture(fieldPilot.getButton(), texture);
        fieldPilot.setOccupied(true);
        fieldPilot.getButton().setTouchable(Touchable.disabled);

        TextureUtils.switchTexture(fieldCopilot.getButton(), texture);
        fieldCopilot.setOccupied(true);
        fieldCopilot.getButton().setTouchable(Touchable.disabled);
    }

    private void waitForDiceSelection(Player player, ActionName name) {
        String path = (player instanceof Pilot) ? TexturePaths.DICE_PATH_PILOT : TexturePaths.DICE_PATH_COPILOT;
        diceInputHandler.addDiceSelectionListeners(player, (button, dice, index) -> {
            TextureUtils.switchTexture(button, path + dice.getValue() + TexturePaths.DICE_PATH_SELECTED);
            diceInputHandler.clearDiceListeners(player);
            dialogManager.creatCoffeeTokenWindow(dice, player, index, gameStateManager, fieldManager,
                (neuerWert) -> {
                    TextureUtils.switchTexture(button, path + neuerWert + TexturePaths.IMG_FORMAT);
                    removeCoffeeTokenByName(name, player);
                    coffeeFieldClearLastUsed(player);
                    onFieldSituationCheck.accept(player);  // ← callback вместо прямого вызова!
                    diceInputHandler.addDiceListenersForSpieler(player);
                    if (game.getCoffeeTokenCount() > 0) {
                        useCoffee(player);
                    }
                });
        });
    }


    /**
     * Initialisiert die Spiegelung von Feldern zwischen Pilot und Copilot.
     * <p>
     * Erstellt Zuordnungen zwischen den entsprechenden Feldern der Kaffee-Kontrollgruppen
     * von Pilot und Copilot, sodass deren Zustände synchronisiert werden können.
     */
    public void initMirrors() {
        ControlField pilotK1 = pilotCoffeeControlGroup.getFieldByAction(ActionName.PILOT_COFFEE_1);
        ControlField copilotK1 = copilotCoffeeControlGroup.getFieldByAction(ActionName.COPILOT_COFFEE_1);

        mirroredFields.put(pilotK1, copilotK1);
        mirroredFields.put(copilotK1, pilotK1);

        ControlField pilotK2 = pilotCoffeeControlGroup.getFieldByAction(ActionName.PILOT_COFFEE_2);
        ControlField copilotK2 = copilotCoffeeControlGroup.getFieldByAction(ActionName.COPILOT_COFFEE_2);

        mirroredFields.put(pilotK2, copilotK2);
        mirroredFields.put(copilotK2, pilotK2);

        ControlField pilotK3 = pilotCoffeeControlGroup.getFieldByAction(ActionName.PILOT_COFFEE_3);
        ControlField copilotK3 = copilotCoffeeControlGroup.getFieldByAction(ActionName.COPILOT_COFFEE_3);

        mirroredFields.put(pilotK3, copilotK3);
        mirroredFields.put(copilotK3, pilotK3);
    }

    private boolean executeStep(ControlField field, Player player, Dice w) {
        boolean isPilot = (player instanceof Pilot);
        ActionResult result = isPilot
            ? gameService.pilotStep(player, field.getAction(), w)
            : gameService.copilotStep(player, field.getAction(), w);
        return result.isSuccess();
    }

    // --- Helper methods ---

    private ControlGroup getCoffeeControlGroup(Player player) {
        return (player instanceof Pilot) ? pilotCoffeeControlGroup : copilotCoffeeControlGroup;
    }

    private ControlGroup getUseCoffeeControlGroup(Player player) {
        return (player instanceof Pilot) ? pilotUseCoffeeControlGroup : copilotUseCoffeeControlGroup;
    }

    private ControlGroup getPilotUseCoffeeControlGroup() {
        return pilotUseCoffeeControlGroup;
    }

    private ControlGroup getCopilotUseCoffeeControlGroup() {
        return copilotUseCoffeeControlGroup;
    }

    private void highlightAvailableFields(ControlGroup group) {
        for (ControlField field : group.getAvailableFields()) {
            TextureUtils.switchTexture(field.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
        }
    }

    private void coffeeFieldClearLastUsed(Player s) {
        List<ControlField> allCoffeeFields = new ArrayList<>();
        allCoffeeFields.addAll(pilotCoffeeControlGroup.getAllFields());
        allCoffeeFields.addAll(copilotCoffeeControlGroup.getAllFields());

        for (int i = allCoffeeFields.size() - 1; i >= 0; i--) {
            ControlField field = allCoffeeFields.get(i);
            if (field.isOccupied()) {
                freeCoffeeFieldForBoth(field, s);
                break;
            }
        }
    }

    private void freeCoffeeFieldForBoth(ControlField field, Player s) {
        ControlField mirrored = mirroredFields.get(field);

        field.setOccupied(false);
        TextureUtils.switchTexture(field.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
        mirrored.setOccupied(false);
        TextureUtils.switchTexture(mirrored.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);

        if (s instanceof Pilot) {
            mirrored.getButton().setTouchable(Touchable.enabled);
        } else {
            field.getButton().setTouchable(Touchable.enabled);
        }
        addCoffeeListeners(s);
    }

    private void removeCoffeeTokenByName(ActionName name, Player player) {
        ControlGroup useGroup = getUseCoffeeControlGroup(player);
        ControlField usedField = useGroup.getFieldByAction(name);
        if (usedField != null) {
            usedField.setOccupied(false);
            TextureUtils.switchTexture(usedField.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            usedField.getButton().setTouchable(Touchable.disabled);
        }
    }

    public void disableCoffeeFields(Player player) {
        ControlGroup coffeeGroup = getCoffeeControlGroup(player);
        for (ControlField field : coffeeGroup.getAvailableFields()) {
            field.getButton().setTouchable(Touchable.disabled);
        }
    }

    public void removeAllCoffeeHighlights() {
        for (ControlField field : pilotCoffeeControlGroup.getAvailableFields()) {
            TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        }
        for (ControlField field : copilotCoffeeControlGroup.getAvailableFields()) {
            TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        }
    }

    public void clearPlayerCoffeeListeners(Player player) {
        if (player instanceof Pilot) {
            for (ControlField field : pilotCoffeeControlGroup.getAllFields()) {
                field.getButton().clearListeners();
            }
            for (ControlField field : pilotUseCoffeeControlGroup.getAllFields()) {
                field.getButton().clearListeners();
                TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            }
        } else {
            for (ControlField field : copilotCoffeeControlGroup.getAllFields()) {
                field.getButton().clearListeners();
            }
            for (ControlField field : copilotUseCoffeeControlGroup.getAllFields()) {
                field.getButton().clearListeners();
                TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            }
        }
    }

    public void dispose() {
        if (pilotCoffeeControlGroup != null) pilotCoffeeControlGroup.dispose();
        if (copilotCoffeeControlGroup != null) copilotCoffeeControlGroup.dispose();
        if (pilotUseCoffeeControlGroup != null) pilotUseCoffeeControlGroup.dispose();
        if (copilotUseCoffeeControlGroup != null) copilotUseCoffeeControlGroup.dispose();
        mirroredFields.clear();
    }
}
