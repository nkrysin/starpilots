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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.teama.skyteam.drop.ui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.ui.utilities.SelectedDiceData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameGUI implements GameStateObserver {

    public final Stage stage;
    private final Game game;
    private final GameControlFactory gameControlFactory;
    private ArrayList<ImageButton> pilotDiceActors = new ArrayList<>();
    private ArrayList<ImageButton> copilotDiceActors = new ArrayList<>();
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
    private Map<ControlField, ControlField> mirroredFields = new HashMap<>();
    private GameStateManager gameStateManager;
    private FieldManager fieldManager;
    private DialogManager dialogManager;
    private DiceInputHandler diceInputHandler;


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
        this.gameStateManager = gameStateManager;
        this.fieldManager = fieldManager;
        this.dialogManager = new DialogManager(skin, stage, game);
        this.diceInputHandler = new DiceInputHandler(stage, gameControlFactory);
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

        controlGroups.add(pilotCoffeeControlGroup);
        controlGroups.add(copilotCoffeeControlGroup);

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

        controlGroups.add(pilotUseCoffeeControlGroup);
        controlGroups.add(copilotUseCoffeeControlGroup);


        this.reroll = gameControlFactory.createRerollButton(stage);


        initMirrors();
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

    private ControlGroup getPilotCoffeeControlGroup() {
        return pilotCoffeeControlGroup;
    }

    private ControlGroup getCopilotCoffeeControlGroup() {
        return copilotCoffeeControlGroup;
    }

    private ControlGroup getPilotUseCoffeeControlGroup() {
        return pilotUseCoffeeControlGroup;
    }

    private ControlGroup getCopilotUseCoffeeControlGroup() {
        return copilotUseCoffeeControlGroup;
    }

    /**
     * Gibt die Kaffee-Kontrollgruppe eines bestimmten Spielers zurück.
     *
     * @param player Der Player, dessen Kaffee-Kontrollgruppe abgerufen werden soll.
     * @return Die entsprechende Kaffee-Kontrollgruppe.
     */
    private ControlGroup getCoffeeControlGroup(Player player) {
        return (player instanceof Pilot) ? getPilotCoffeeControlGroup() : getCopilotCoffeeControlGroup();
    }

    /**
     * Gibt die Kontrollgruppe für die Verwendung von Kaffee eines bestimmten Spielers zurück.
     *
     * @param player Der Player, dessen Kaffee-Verwendungsgruppe abgerufen werden soll.
     * @return Die entsprechende Kontrollgruppe für die Verwendung von Kaffee.
     */
    private ControlGroup getUseCoffeeControlGroup(Player player) {
        return (player instanceof Pilot) ? getPilotUseCoffeeControlGroup() : getCopilotUseCoffeeControlGroup();
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
        addCoffeeListeners(player);
    }

    /**
     * Fügt Click-Listener zu den Kaffee-Kontrollfeldern eines Spielers hinzu.
     *
     * @param player Der Player, dessen Kaffee-Kontrollfelder aktiviert werden sollen.
     */
    private void addCoffeeListeners(Player player) {
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
     * Initialisiert die Spiegelung von Feldern zwischen Pilot und Copilot.
     *
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

    private boolean executeStep(ControlField field, Player player, Dice w){
        boolean isPilot = (player instanceof Pilot);
        ActionResult result = isPilot
            ? gameService.pilotStep(player, field.getAction(), w)
            : gameService.copilotStep(player, field.getAction(), w);
        return result.isSuccess();
    }

    /**
     * Behandelt die Aktion beim Klicken auf ein Kaffee-Kontrollfeld.
     *
     * @param field Das angeklickte Kontrollfeld.
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
        if (callback != null) {
            callback.onPlayerActionCompleted(player);
        }

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
     * Blockiert ein Field für beide Player (Pilot und Copilot) und markiert es als belegt.
     *
     * @param fieldPilot Das Kontrollfeld des Piloten.
     * @param fieldCopilot Das entsprechende Kontrollfeld des Copiloten.
     * @param data Die Daten des ausgewählten Würfels.
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
        ControlGroup coffeeFields;
        if (player instanceof Pilot) {
            fieldTilt = game.getFieldPilot(FieldName.TILT);
            fieldSpeed = game.getFieldPilot(FieldName.SPEED);
            tiltField = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS).getFieldByAction(ActionName.PILOT_TILT);
            speedField = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS).getFieldByAction(ActionName.PILOT_SPEED);
            allFields = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS);
            coffeeFields = pilotCoffeeControlGroup;
            player = game.getPilot();
        } else {
            fieldTilt = game.getFieldCopilot(FieldName.TILT);
            fieldSpeed = game.getFieldCopilot(FieldName.SPEED);
            tiltField = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS).getFieldByAction(ActionName.COPILOT_TILT);
            speedField = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS).getFieldByAction(ActionName.COPILOT_SPEED);
            allFields = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
            coffeeFields = copilotCoffeeControlGroup;
            player = game.getCopilot();
        }
        if (!fieldSpeed.isOccupied() && !fieldTilt.isOccupied()) {
            dialogManager.createMessage("Achtung!", "Bitte setzen Sie die Würfel auf die Pflichtfelder");
            requiredActionsGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(tiltField, speedField));
            removeHighlight(allFields);
            removeHighlight(getPilotCoffeeControlGroup());
            removeHighlight(getCopilotCoffeeControlGroup());
            disableFields(allFields, requiredActionsGroup);
            disableFields(coffeeFields, requiredActionsGroup);
            addListenersToGroupFields(requiredActionsGroup, player);
        } else if (!fieldSpeed.isOccupied()) {
            dialogManager.createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            requiredActionsGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(speedField));
            removeHighlight(allFields);
            removeHighlight(getPilotCoffeeControlGroup());
            removeHighlight(getCopilotCoffeeControlGroup());
            addListenersToGroupFields(requiredActionsGroup, player);
        } else {
            dialogManager.createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            requiredActionsGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(tiltField));
            removeHighlight(allFields);
            removeHighlight(getPilotCoffeeControlGroup());
            removeHighlight(getCopilotCoffeeControlGroup());
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
    }

    /**
     * Entfernt alle Listener von den Würfeln und Kontrollfeldern eines Spielers.
     *
     * @param player Der Player, dessen Listener entfernt werden sollen.
     */
    public void clearPlayerListeners(Player player) {
        ArrayList<ImageButton> diceActors = (player instanceof Pilot) ? pilotDiceActors : copilotDiceActors;
        for (ImageButton button : diceActors) {
            button.clearListeners();
        }

        ControlGroup group = (player instanceof Pilot)
            ? getControlGroupByName(ActionControlGroup.PILOT_CONTROLS)
            : getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
        for (ControlField field : group.getAvailableFields()) {
            field.getButton().clearListeners();
            TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        }


        if (player instanceof Pilot) {
            for (ControlField field : getPilotCoffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
            }
            for (ControlField field : getPilotUseCoffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
                TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            }
        } else {
            for (ControlField field : getCopilotCoffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
            }
            for (ControlField field : getCopilotUseCoffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
                TextureUtils.switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            }
        }
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
        showReroll(false);
        showCoffee(false, false);
    }


    /**
     * Zeigt die Kaffee-Kontrollgruppen an oder blendet sie aus.
     *
     * @param pilotAvailable Gibt an, ob die Kaffeegruppen des Piloten aktiv sein sollen.
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
     * Wartet auf die Auswahl eines Würfels durch den Player, um die Kaffee-Aktion auszuführen.
     *
     * @param player Der Player, der die Aktion durchführt.
     * @param name Der Name des Kaffee-Kontrollfeldes.
     */
    private void waitForDiceSelection(Player player, ActionName name) {
        ArrayList<ImageButton> diceActors = (player instanceof Pilot)
            ? pilotDiceActors
            : copilotDiceActors;
        ArrayList<Dice> dices = player.getAllDice();
        String path = (player instanceof Pilot)
            ? TexturePaths.DICE_PATH_PILOT
            : TexturePaths.DICE_PATH_COPILOT;

        for (int i = 0; i < diceActors.size(); i++) {
            final ImageButton wuerfelButton = diceActors.get(i);
            final Dice dice = dices.get(i);
            final int index = i;
            final int wert = dice.getValue();

            wuerfelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    TextureUtils.switchTexture(wuerfelButton, path + wert + TexturePaths.DICE_PATH_SELECTED);
                    diceInputHandler.clearDiceListeners(player);
                    dialogManager.creatCoffeeTokenWindow(dice, player, index, gameStateManager, fieldManager,
                        (neuerWert) -> {
                            TextureUtils.switchTexture(wuerfelButton, path + neuerWert + TexturePaths.IMG_FORMAT);
                        removeCoffeeTokenByName(name, player);
                        coffeeFieldClearLastUsed(player);
                        checkFieldSituation(player);
                            diceInputHandler.addDiceListenersForSpieler(player);

                        if (game.getCoffeeTokenCount() > 0) {
                            useCoffee(player);
                        }
                    });
                }
            });
        }
    }

    /**
     * Entfernt die zuletzt verwendeten Kaffee-Kontrollfelder.
     *
     * @param s Der Player, dessen Kontrollfelder zurückgesetzt werden sollen.
     */
    private void coffeeFieldClearLastUsed(Player s) {
        List<ControlField> pilotFields = pilotCoffeeControlGroup.getAllFields();
        List<ControlField> copilotFields = copilotCoffeeControlGroup.getAllFields();

        List<ControlField> allCoffeeFields = new ArrayList<>();
        allCoffeeFields.addAll(pilotFields);
        allCoffeeFields.addAll(copilotFields);

        for (int i = allCoffeeFields.size() - 1; i >= 0; i--) {
            ControlField field = allCoffeeFields.get(i);
            if (field.isOccupied()) {
                freeCoffeeFieldForBoth(field, s);
                break;
            }
        }
    }

    /**
     * Gibt ein Kaffee-Kontrollfeld und sein Spiegelbild für beide Player frei.
     *
     * @param field Das Kaffee-Kontrollfeld, das freigegeben werden soll.
     * @param s Der Player, für den das Kontrollfeld freigegeben wird.
     */
    private void freeCoffeeFieldForBoth(ControlField field, Player s) {
        ControlField mirrored = mirroredFields.get(field);

        if (s instanceof Pilot) {
            field.setOccupied(false);
            TextureUtils.switchTexture(field.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
            mirrored.setOccupied(false);
            TextureUtils.switchTexture(mirrored.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
            mirrored.getButton().setTouchable(Touchable.enabled);
            addCoffeeListeners(s);

        } else {
            field.setOccupied(false);
            TextureUtils.switchTexture(field.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
            mirrored.setOccupied(false);
            TextureUtils.switchTexture(mirrored.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
            field.getButton().setTouchable(Touchable.enabled);
            addCoffeeListeners(s);
        }
    }


    /**
     * Entfernt ein verwendetes Kaffee-Token aus der Kontrollgruppe.
     *
     * @param name Der Name des verwendeten Kaffee-Kontrollfeldes.
     * @param player Der Player, dessen Kaffee-Token entfernt werden soll.
     */
    private void removeCoffeeTokenByName(ActionName name, Player player) {
        ControlGroup useGroup = getUseCoffeeControlGroup(player);
        ControlField usedField = useGroup.getFieldByAction(name);
        if (usedField != null) {
            usedField.setOccupied(false);
            TextureUtils.switchTexture(usedField.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            usedField.getButton().setTouchable(Touchable.disabled);
        }
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

        if (reroll != null && reroll.getButton() != null) {
            ImageButton button = reroll.getButton();
            button.remove();
            reroll = null;
        }

        for (ImageButton button : pilotDiceActors) {
            button.remove();
        }
        pilotDiceActors.clear();

        for (ImageButton button : copilotDiceActors) {
            button.remove();
        }
        copilotDiceActors.clear();

        if (pilotCoffeeControlGroup != null) {
            pilotCoffeeControlGroup.dispose();
            pilotCoffeeControlGroup = null;
        }
        if (copilotCoffeeControlGroup != null) {
            copilotCoffeeControlGroup.dispose();
            copilotCoffeeControlGroup = null;
        }
        if (pilotUseCoffeeControlGroup != null) {
            pilotUseCoffeeControlGroup.dispose();
            pilotUseCoffeeControlGroup = null;
        }
        if (copilotUseCoffeeControlGroup != null) {
            copilotUseCoffeeControlGroup.dispose();
            copilotUseCoffeeControlGroup = null;
        }
        for (ControlGroup group : controlGroups) {
            group.dispose();
        }

        controlGroups.clear();

        mirroredFields.clear();
        diceInputHandler.clearSelectedDiceData();


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
