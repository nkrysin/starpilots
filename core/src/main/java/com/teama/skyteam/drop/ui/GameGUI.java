/**
 * Repräsentiert die grafische Benutzeroberfläche (GUI) des Spiels.
 *
 * Die Klasse `GameGUI` verwaltet die Erstellung und Steuerung aller visuellen Elemente,
 * wie z. B. Würfel, Schalter und Steuergruppen, sowie die Interaktion zwischen Spielern
 * und der Game-Logik. Sie integriert verschiedene GUI-Komponenten wie Schalter, Kaffeegruppen
 * und Würfel und sorgt für die Synchronisation mit der Spielmechanik.
 */
package com.teama.skyteam.drop.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teama.skyteam.drop.application.gameservice.GameService;
import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.ui.buttons.ControlField;
import com.teama.skyteam.drop.ui.buttons.ControlGroup;
import com.teama.skyteam.drop.ui.constants.TexturePaths;
import com.teama.skyteam.drop.ui.constants.UILayout;
import com.teama.skyteam.drop.ui.dialog.DialogManager;
import com.teama.skyteam.drop.ui.factory.GameControlFactory;
import com.teama.skyteam.drop.ui.input.DiceInputHandler;
import com.teama.skyteam.drop.ui.token.CoffeeTokenHandler;
import com.teama.skyteam.drop.ui.token.RerollTokenHandler;
import com.teama.skyteam.drop.ui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.type.ActionControlGroup;
import com.teama.skyteam.drop.domain.action.type.ActionName;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.application.logic.fields.FieldManager;
import com.teama.skyteam.drop.application.logic.observer.GameStateObserver;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.Dice;
import com.teama.skyteam.drop.ui.utilities.TextureUtils;
import com.teama.skyteam.drop.ui.view.CampaignAltitudeView;
import com.teama.skyteam.drop.ui.view.SwitchPanelView;
import java.util.ArrayList;
import java.util.List;

public class GameGUI implements GameStateObserver {

    public final Stage stage;
    private final Game game;
    private final GameControlFactory gameControlFactory;
    private ArrayList<ControlGroup>  controlGroups = new ArrayList<>();
    private ControlGroup pilotCoffeeControlGroup;
    private ControlGroup copilotCoffeeControlGroup;
    private ControlGroup pilotUseCoffeeControlGroup;
    private ControlGroup copilotUseCoffeeControlGroup;
    private GameService gameService;
    private Skin skin;
    private SwitchPanelView switchPanelView;
    private Image rudder;
    private PlayerActionCallback callback;
    private CampaignAltitudeView campaignAltitudeView;
    private ControlField reroll;
    private DialogManager dialogManager;
    private DiceInputHandler diceInputHandler;
    private CoffeeTokenHandler coffeeTokenHandler;
    private RerollTokenHandler rerollTokenHandler;

    /**
     * Konstruktor zur Initialisierung der Game-GUI.
     *
     * @param game Die aktuelle Game-Logik.
     * @param stage Die Bühne, auf der die grafischen Elemente platziert werden.
     * @param skin Das Skin-Objekt für die Gestaltung der GUI-Elemente.
     * @param campaignAltitudeView Der Manager zur Verwaltung der Szenariohöhe.
     */
    public GameGUI(Game game, Stage stage, Skin skin, CampaignAltitudeView campaignAltitudeView, GameService gameService, GameStateManager gameStateManager, FieldManager fieldManager) {
        this.game = game;
        this.stage = stage;
        this.skin = skin;
        setRudder();
        this.gameControlFactory = new GameControlFactory();
        switchPanelView = new SwitchPanelView(stage);
        switchPanelView.addToStage();
        this.campaignAltitudeView = campaignAltitudeView;
        this.gameService = gameService;
        gameService.addObserver(this);
        createFieldActors();
        this.dialogManager = new DialogManager(skin, stage, game);
        this.diceInputHandler = new DiceInputHandler(stage, gameControlFactory);
        this.coffeeTokenHandler = new CoffeeTokenHandler(
            pilotCoffeeControlGroup, copilotCoffeeControlGroup,
            pilotUseCoffeeControlGroup, copilotUseCoffeeControlGroup,
            diceInputHandler, dialogManager, game, gameService, gameStateManager, fieldManager);
        this.coffeeTokenHandler.setOnFieldSituationCheck(this::checkFieldSituation);
        this.rerollTokenHandler = new RerollTokenHandler(reroll, game, dialogManager, diceInputHandler, gameStateManager);
    }

    public DialogManager getDialogManager() {
        return dialogManager;
    }

    public DiceInputHandler getDiceInputHandler() {
        return diceInputHandler;
    }

    /**
     * Initialisiert und fügt das Rudder zur Bühne hinzu.
     */
    private void setRudder() {
        Texture rudderTexture = new Texture(TexturePaths.RUDDER_PATH);
        rudder = new Image(rudderTexture);
        rudder.setPosition(UILayout.RUDDER_COORDINATES[0], UILayout.RUDDER_COORDINATES[1]);
        rudder.setOrigin(rudder.getWidth() / 2, rudder.getHeight() / 2 - 5);
        this.stage.addActor(rudder);
    }

    public Image getRudder() {
        return rudder;
    }

    /**
     * Erstellt die grafischen Felder (ControlFields) für die Steuerung der Spieleraktionen.
     */
    private void createFieldActors() {
        controlGroups.add(gameControlFactory.createControlGroup(
            ActionControlGroup.PILOT_CONTROLS,
            new ActionName[]{
                ActionName.PILOT_RADIO,
                ActionName.PILOT_TILT,
                ActionName.PILOT_SPEED,
                ActionName.PILOT_LANDING_GEAR_1_2,
                ActionName.PILOT_LANDING_GEAR_3_4,
                ActionName.PILOT_LANDING_GEAR_5_6,
                ActionName.PILOT_BRAKES_2,
                ActionName.PILOT_BRAKES_4,
                ActionName.PILOT_BRAKES_6
            },
            UILayout.PILOT_ACTOR_COORDINATES, stage, UILayout.DICE_SIZE[0], UILayout.DICE_SIZE[1]));

        controlGroups.add(gameControlFactory.createControlGroup(
            ActionControlGroup.COPILOT_CONTROLS,
            new ActionName[]{
                ActionName.COPILOT_TILT,
                ActionName.COPILOT_SPEED,
                ActionName.COPILOT_RADIO_1,
                ActionName.COPILOT_RADIO_2,
                ActionName.COPILOT_FLAPS_1_2,
                ActionName.COPILOT_FLAPS_2_3,
                ActionName.COPILOT_FLAPS_4_5,
                ActionName.COPILOT_FLAPS_5_6
            },
            UILayout.COPILOT_ACTOR_COORDINATES, stage, UILayout.DICE_SIZE[0], UILayout.DICE_SIZE[1]));

        pilotCoffeeControlGroup = gameControlFactory.createControlGroup(
            ActionControlGroup.PILOT_COFFEE_CONTROLS,
            new ActionName[]{
                ActionName.PILOT_COFFEE_1,
                ActionName.PILOT_COFFEE_2,
                ActionName.PILOT_COFFEE_3
            },
            UILayout.COFFEE_COORDINATES, stage, UILayout.DICE_SIZE[0], UILayout.DICE_SIZE[1]);
        copilotCoffeeControlGroup = gameControlFactory.createControlGroup(
            ActionControlGroup.COPILOT_COFFEE_CONTROLS,
            new ActionName[]{
                ActionName.COPILOT_COFFEE_1,
                ActionName.COPILOT_COFFEE_2,
                ActionName.COPILOT_COFFEE_3
            },
            UILayout.COFFEE_COORDINATES, stage, UILayout.DICE_SIZE[0], UILayout.DICE_SIZE[1]);

        pilotUseCoffeeControlGroup = gameControlFactory.createUseCoffeeButtons(
            ActionControlGroup.PILOT_USE_COFFEE_CONTROL_GROUP,
            UILayout.COFFEE_CONTROL_COORDINATES,
            stage
        );
        copilotUseCoffeeControlGroup = gameControlFactory.createUseCoffeeButtons(
            ActionControlGroup.COPILOT_USE_COFFEE_CONTROL_GROUP,
            UILayout.COFFEE_CONTROL_COORDINATES,
            stage
        );

        this.reroll = gameControlFactory.createRerollButton(stage);
    }

    /**
     * Hebt die verfügbaren Felder einer Kontrollgruppe hervor.
     *
     * @param group Die Kontrollgruppe, deren Felder hervorgehoben werden sollen.
     */
    public void highlightAvailableFields(ControlGroup group) {
        for (ControlField field : group.getAvailableFields()) {
            ImageButton button = field.getButton();
            TextureUtils.switchTexture(button, TexturePaths.AVAILABLE_FIELD_PATH);
        }
    }

    /**
     * Entfernt die Hervorhebung von verfügbaren Feldern einer Kontrollgruppe.
     *
     * @param group Die Kontrollgruppe, deren Hervorhebung entfernt werden soll.
     */
    public void removeHighlight(ControlGroup group) {
        for (ControlField field : group.getAvailableFields()) {
            ImageButton button = field.getButton();
            TextureUtils.switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        }
    }

    /**
     * Deaktiviert Felder in einer Kontrollgruppe, die nicht in der Pflichtgruppe enthalten sind.
     *
     * @param group Die Kontrollgruppe, deren Felder deaktiviert werden sollen.
     * @param pflicht Die Pflichtgruppe, deren Felder aktiv bleiben sollen.
     */
    public void disableFields(ControlGroup group, ControlGroup pflicht) {
        List<ControlField> pflichtFields = pflicht.getAvailableFields();
        for (ControlField field : group.getAvailableFields()) {
            if (!pflichtFields.contains(field)) {
                field.getButton().setTouchable(Touchable.disabled);
            }
        }
    }

    /**
     * Gibt die Hauptkontrollgruppe des Spielers zurück.
     *
     * @param player Der Player, dessen Hauptkontrollgruppe abgerufen werden soll.
     * @return Die Hauptkontrollgruppe des Spielers.
     */
    private ControlGroup getMainControlGroup(Player player) {
        return (player instanceof Pilot)
            ? getControlGroupByName(ActionControlGroup.PILOT_CONTROLS)
            : getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
    }

    /**
     * Gibt eine Kontrollgruppe anhand ihres Namens zurück.
     *
     * @param group Der Name der Kontrollgruppe.
     * @return Die Kontrollgruppe mit dem angegebenen Namen oder null, wenn sie nicht existiert.
     */
    private ControlGroup getControlGroupByName(ActionControlGroup group) {
        for (ControlGroup cg : controlGroups) {
            if (cg.getControlGroup() == group) {
                return cg;
            }
        }
        return null;
    }

    /**
     * Fügt Click-Listener zu den Feldern einer Kontrollgruppe hinzu, um Würfel zu platzieren.
     *
     * @param player Der Player, dessen Würfel platziert werden sollen.
     */
    public void addFieldListenersForPlacement(Player player) {
        ControlGroup mainGroup = getMainControlGroup(player);
        addListenersToGroupFields(mainGroup, player);
        coffeeTokenHandler.addCoffeeListeners(player);
    }

    /**
     * Fügt Click-Listener zu den Hauptkontrollfeldern eines Spielers hinzu.
     *
     * @param group Die Hauptkontrollgruppe des Spielers.
     * @param player Der Player, dessen Felder aktiviert werden.
     */
    private void addListenersToGroupFields(ControlGroup group, Player player) {
        highlightAvailableFields(group);
        for (ControlField field : group.getAvailableFields()) {
            field.getButton().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleMainFieldClick(field, player);
                }
            });
        }
    }


    /**
     * Behandelt die Aktion beim Klicken auf ein Hauptkontrollfeld.
     *
     * @param field Das angeklickte Kontrollfeld.
     * @param player Der Player, der die Aktion ausführt.
     */
    private void handleMainFieldClick(ControlField field, Player player) {
        if (diceInputHandler.getSelectedDiceData() == null || field.isOccupied()) {
            return;
        }
        boolean isPilot = player instanceof Pilot;
        Dice w = diceInputHandler.getSelectedDiceData().getDice();

        boolean result = executeStep(field, player, w);
        if (!result) {
            return;
        }

        String texturePath = isPilot
            ? TexturePaths.DICE_PATH_PILOT + w.getValue() + TexturePaths.IMG_FORMAT
            : TexturePaths.DICE_PATH_COPILOT + w.getValue() + TexturePaths.IMG_FORMAT;
        diceInputHandler.removeDiceButtonFromHand(diceInputHandler.getSelectedDiceData());
        TextureUtils.switchTexture(field.getButton(), texturePath);
        field.setOccupied(true);
        diceInputHandler.clearSelectedDiceData();
        if (callback != null) {
            callback.onPlayerActionCompleted(player);
        }
    }

    private boolean executeStep(ControlField field, Player player, Dice w) {
        boolean isPilot = (player instanceof Pilot);
        ActionResult result = isPilot
            ? gameService.pilotStep(player, field.getAction(), w)
            : gameService.copilotStep(player, field.getAction(), w);
        return result.isSuccess();
    }

    /**
     * Überprüft die aktuelle Situation der Felder für einen Player und führt ggf. Pflichtaktionen aus.
     *
     * @param player Der Player, dessen Feldsituation überprüft wird.
     */
    public void checkFieldSituation(Player player) {
        Field fieldTilt;
        Field fieldSpeed;
        int diceAmount;
        if (player instanceof Pilot) {
            fieldTilt = game.getFieldPilot(FieldName.TILT);
            fieldSpeed = game.getFieldPilot(FieldName.SPEED);
            diceAmount = game.getPilot().getAllDiceCount();
            player = game.getPilot();
        } else {
            fieldTilt = game.getFieldCopilot(FieldName.TILT);
            fieldSpeed = game.getFieldCopilot(FieldName.SPEED);
            diceAmount = game.getCopilot().getAllDiceCount();
            player = game.getCopilot();
        }
        if (!fieldTilt.isOccupied() && !fieldSpeed.isOccupied() && diceAmount == 2) {
            requiredAction(player);
        } else if (!fieldTilt.isOccupied() && diceAmount == 1) {
            requiredAction(player);
        } else if (!fieldSpeed.isOccupied() && diceAmount == 1) {
            requiredAction(player);
        }
    }

    /**
     * Führt eine Pflichtaktion für einen Player aus, wenn diese erforderlich ist.
     *
     * @param player Der Player, der die Pflichtaktion ausführen muss.
     */
    void requiredAction(Player player) {
        Field fieldTilt;
        Field fieldSpeed;
        ControlField tiltField;
        ControlField speedField;
        ControlGroup requiredActionsGroup;
        ControlGroup allFields;
        if (player instanceof Pilot) {
            fieldTilt = game.getFieldPilot(FieldName.TILT);
            fieldSpeed = game.getFieldPilot(FieldName.SPEED);
            tiltField = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS).getFieldByAction(ActionName.PILOT_TILT);
            speedField = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS).getFieldByAction(ActionName.PILOT_SPEED);
            allFields = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS);
            player = game.getPilot();
        } else {
            fieldTilt = game.getFieldCopilot(FieldName.TILT);
            fieldSpeed = game.getFieldCopilot(FieldName.SPEED);
            tiltField = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS).getFieldByAction(ActionName.COPILOT_TILT);
            speedField = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS).getFieldByAction(ActionName.COPILOT_SPEED);
            allFields = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
            player = game.getCopilot();
        }
        if (!fieldSpeed.isOccupied() && !fieldTilt.isOccupied()) {
            dialogManager.createMessage("Achtung!", "Bitte setzen Sie die Würfel auf die Pflichtfelder");
            requiredActionsGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(tiltField, speedField));
            removeHighlight(allFields);
            coffeeTokenHandler.removeAllCoffeeHighlights();
            disableFields(allFields, requiredActionsGroup);
            coffeeTokenHandler.disableCoffeeFields(player);
            addListenersToGroupFields(requiredActionsGroup, player);
        } else if (!fieldSpeed.isOccupied()) {
            dialogManager.createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            requiredActionsGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(speedField));
            removeHighlight(allFields);
            coffeeTokenHandler.removeAllCoffeeHighlights();
            addListenersToGroupFields(requiredActionsGroup, player);
        } else {
            dialogManager.createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            requiredActionsGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(tiltField));
            removeHighlight(allFields);
            coffeeTokenHandler.removeAllCoffeeHighlights();
            addListenersToGroupFields(requiredActionsGroup, player);
        }
    }

    public SwitchPanelView getSwitchGUI() {
        return switchPanelView;
    }

    /**
     * Setzt den Callback für Spieleraktionen.
     *
     * @param callback Die Callback-Instanz, die bei Abschluss einer Spieleraktion aufgerufen wird.
     */
    public void setPlayerActionCallback(PlayerActionCallback callback) {
        this.callback = callback;
        coffeeTokenHandler.setPlayerActionCallback(callback);
    }

    public void showCoffee(Boolean pilotAvailable, Boolean copilotAvailable) {
        coffeeTokenHandler.showCoffee(pilotAvailable, copilotAvailable);
    }

    public void useCoffee(Player player) {
        coffeeTokenHandler.useCoffee(player);
    }

    public void setCoffeeGroupsActive(Player player) {
        coffeeTokenHandler.setCoffeeGroupsActive(player);
    }

    /**
     * Entfernt alle Listener von den Würfeln und Kontrollfeldern eines Spielers.
     *
     * @param player Der Player, dessen Listener entfernt werden sollen.
     */
    public void clearPlayerListeners(Player player) {
        diceInputHandler.clearDiceListeners(player);

        ControlGroup group = (player instanceof Pilot)
            ? getControlGroupByName(ActionControlGroup.PILOT_CONTROLS)
            : getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
        for (ControlField field : group.getAvailableFields()) {
            field.getButton().clearListeners();
            TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        }

        coffeeTokenHandler.clearPlayerCoffeeListeners(player);
    }

    /**
     * Entfernt die Hervorhebung von Mehrfachnutzungsfeldern und setzt sie zurück.
     */
    public void clearManyUseFields() {
        ControlGroup pilotGroup = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS);
        ControlGroup copilotGroup = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
        ActionName[] pilotManyUseFields = {ActionName.PILOT_RADIO, ActionName.PILOT_TILT, ActionName.PILOT_SPEED};
        for (ActionName s : pilotManyUseFields) {
            ImageButton button = pilotGroup.getFieldByAction(s).getButton();
            TextureUtils.switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            pilotGroup.getFieldByAction(s).setOccupied(false);
        }
        ActionName[] copilotManyUseFields = {ActionName.COPILOT_TILT, ActionName.COPILOT_SPEED, ActionName.COPILOT_RADIO_1,  ActionName.COPILOT_RADIO_2};
        for (ActionName s : copilotManyUseFields) {
            ImageButton button = copilotGroup.getFieldByAction(s).getButton();
            TextureUtils.switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            copilotGroup.getFieldByAction(s).setOccupied(false);
        }
        rerollTokenHandler.showReroll(false);
        coffeeTokenHandler.showCoffee(false, false);
    }

    public void showReroll(Boolean available) {
        rerollTokenHandler.showReroll(available);
    }

    public void useReroll(Player player) {
        rerollTokenHandler.useReroll(player);
    }


    /**
     * Gibt alle verwendeten Ressourcen frei und entfernt alle GUI-Elemente.
     */
    public void dispose() {
        if (rudder != null && rudder.getDrawable() instanceof TextureRegionDrawable) {
            rudder.remove();
            rudder = null;
        }

        if (gameControlFactory != null) {
            gameControlFactory.dispose();

        }

        if (switchPanelView != null) {
            switchPanelView.dispose();
            switchPanelView = null;
        }

        rerollTokenHandler.dispose();
        diceInputHandler.dispose();
        coffeeTokenHandler.dispose();

        for (ControlGroup group : controlGroups) {
            group.dispose();
        }
        controlGroups.clear();

        gameService = null;
        skin = null;
        callback = null;
        campaignAltitudeView = null;
    }

    /*
    * GameStateObserver
    * */

    @Override
    public void onBrakesActivated(int switchIndex, int indicatorValue) {
        switchPanelView.activateBrakesSwitch(switchIndex);
        switchPanelView.moveBrakesIndicator(indicatorValue);
    }

    @Override
    public void onLandingGearActivated(int switchIndex, int count) {
        switchPanelView.activateLandingGearSwitch(switchIndex);
        switchPanelView.moveLandingGearIndicator(count);
    }

    @Override
    public void onFlapsActivated(int switchIndex, int state) {
        switchPanelView.aktivateFlapsSwitch(switchIndex);
        switchPanelView.moveFlapsIndicator(state);
    }

    @Override
    public void onTiltChanged(float targetRotation) {
        rudder.addAction(Actions.rotateTo(targetRotation, 1f));
    }

    @Override
    public void onSpeedChanged(int steps) {
        campaignAltitudeView.updateCampaign(steps);
    }


}
