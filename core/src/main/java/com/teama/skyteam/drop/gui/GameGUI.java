/**
 * Repräsentiert die grafische Benutzeroberfläche (GUI) des Spiels.
 *
 * Die Klasse `GameGUI` verwaltet die Erstellung und Steuerung aller visuellen Elemente,
 * wie z. B. Würfel, Schalter und Steuergruppen, sowie die Interaktion zwischen Spielern
 * und der Game-Logik. Sie integriert verschiedene GUI-Komponenten wie Schalter, Kaffeegruppen
 * und Würfel und sorgt für die Synchronisation mit der Spielmechanik.
 */
package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Align;
import com.teama.skyteam.drop.application.gameservice.GameService;
import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.gui.buttons.ControlField;
import com.teama.skyteam.drop.gui.buttons.ControlGroup;
import com.teama.skyteam.drop.gui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.gui.utilities.SelectedWuerfelData;
import com.teama.skyteam.drop.gui.utilities.TextureCache;
import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.action.type.ActionControlGroup;
import com.teama.skyteam.drop.domain.action.type.ActionName;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.application.gamestate.GameStateManager;
import com.teama.skyteam.drop.application.logic.fields.FieldManager;
import com.teama.skyteam.drop.application.logic.observer.GameStateObserver;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeTokenAction;
import com.teama.skyteam.drop.domain.model.tokens.RerollToken;
import com.teama.skyteam.drop.domain.model.tokens.Dice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GameGUI implements GameStateObserver {

    public final Stage stage;
    private final Game game;
    private final GUIConstructionMethods guiConstructionMethods;
    private ArrayList<ImageButton> pilotWuerfelActors = new ArrayList<>();
    private ArrayList<ImageButton> copilotWuerfelActors = new ArrayList<>();
    private ArrayList<ControlGroup>  controlGroups = new ArrayList<>();
    private ControlGroup pilotKaffeeControlGroup;
    private ControlGroup copilotKaffeeControlGroup;
    private ControlGroup pilotUseKaffeeControlGroup;
    private ControlGroup copilotUseKaffeeControlGroup;
    private SelectedWuerfelData selectedWuerfelData = null;
    private GameService gameService;
    private Skin skin;
    private SwitchGUI switchGUI;
    private Image ruder;
    private PlayerActionCallback callback;
    private CampaignAltitudeManager campaignAltitudeManager;
    private ControlField neuWurf;
    private Map<ControlField, ControlField> mirroredFields = new HashMap<>();
    private GameStateManager gameStateManager;
    private FieldManager fieldManager;


    /**
     * Konstruktor zur Initialisierung der Game-GUI.
     *
     * @param game Die aktuelle Game-Logik.
     * @param stage Die Bühne, auf der die grafischen Elemente platziert werden.
     * @param skin Das Skin-Objekt für die Gestaltung der GUI-Elemente.
     * @param campaignAltitudeManager Der Manager zur Verwaltung der Szenariohöhe.
     */
    public GameGUI(Game game, Stage stage, Skin skin, CampaignAltitudeManager campaignAltitudeManager, GameService gameService, GameStateManager gameStateManager, FieldManager fieldManager) {
        this.game = game;
        this.stage = stage;
        this.skin = skin;
        setRuder();
        this.guiConstructionMethods = new GUIConstructionMethods();
        switchGUI = new SwitchGUI(stage);
        switchGUI.addToStage();
        this.campaignAltitudeManager = campaignAltitudeManager;
        this.gameService = gameService;
        gameService.addObserver(this);
        createFieldActors();
        this.gameStateManager = gameStateManager;
        this.fieldManager = fieldManager;
    }

    /**
     * Initialisiert und fügt das Ruder zur Bühne hinzu.
     */
    private void setRuder() {
        Texture ruderTexture = new Texture(TexturePaths.RUDER_PATH);
        ruder = new Image(ruderTexture);
        ruder.setPosition(UILayout.RUDER_COORDINATES[0], UILayout.RUDER_COORDINATES[1]);
        ruder.setOrigin(ruder.getWidth() / 2, ruder.getHeight() / 2 - 5);
        this.stage.addActor(ruder);
    }

    public Image getRuder() {
        return ruder;
    }

    /**
     * Erstellt grafische Würfel für den angegebenen Player und fügt sie der Bühne hinzu.
     *
     * @param player Der Player, für den die Würfel erstellt werden.
     */
    public void createWuerfelForSpieler(Player player) {
        if (player instanceof Pilot) {
            for (ImageButton oldButton : pilotWuerfelActors) {
                oldButton.remove();
            }
            pilotWuerfelActors.clear();
        } else {
            for (ImageButton oldButton : copilotWuerfelActors) {
                oldButton.remove();
            }
            copilotWuerfelActors.clear();
        }

        ArrayList<Dice> dice = player.getAllDice();
        ArrayList<ImageButton> wuerfelActors = new ArrayList<>();

        for (int i = 0; i < dice.size(); i++) {
            Dice w = dice.get(i);
            int wert = w.getValue();
            String textureName;
            int x, y;
            if (player instanceof Pilot) {
                textureName = TexturePaths.WUERFEL_PATH_PILOT + wert + TexturePaths.IMG_FORMAT;
                x = UILayout.PILOT_WUERFEL_COORDINATES[i][0];
                y = UILayout.PILOT_WUERFEL_COORDINATES[i][1];
            } else {
                textureName = TexturePaths.WUERFEL_PATH_COPILOT + wert + TexturePaths.IMG_FORMAT;
                x = UILayout.COPILOT_WUERFEL_COORDINATES[i][0];
                y = UILayout.COPILOT_WUERFEL_COORDINATES[i][1];
            }
            ImageButton wuerfelButton = guiConstructionMethods.createButton(textureName, x, y, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]);
            wuerfelActors.add(wuerfelButton);
            stage.addActor(wuerfelButton);
        }

        if (player instanceof Pilot) {
            pilotWuerfelActors = wuerfelActors;
        } else {
            copilotWuerfelActors = wuerfelActors;
        }
        hideWuerfel(player);
    }

    /**
     * Fügt den Würfeln eines Spielers Click-Listener hinzu.
     *
     * @param player Der Player, dessen Würfel interaktiv gemacht werden.
     */
    public void addWuerfelListenersForSpieler(Player player) {
        ArrayList<ImageButton> wuerfelActors = (player instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        ArrayList<Dice> dice = player.getAllDice();
        final ImageButton[] prevSelectedButton = {null};
        final String[] prevNormalTextureName = {null};

        for (int i = 0; i < wuerfelActors.size(); i++) {
            final ImageButton wuerfelButton = wuerfelActors.get(i);
            final Dice w = dice.get(i);
            final int wert = w.getValue();
            final boolean isPilot = (player instanceof Pilot);
            final String normalTextureName;
            final String selectedTextureName;
            if (isPilot) {
                normalTextureName = TexturePaths.WUERFEL_PATH_PILOT + wert + TexturePaths.IMG_FORMAT;
                selectedTextureName = TexturePaths.WUERFEL_PATH_PILOT + wert +  TexturePaths.WUERFEL_PATH_SELECTED;
            } else {
                normalTextureName = TexturePaths.WUERFEL_PATH_COPILOT + wert + TexturePaths.IMG_FORMAT;
                selectedTextureName = TexturePaths.WUERFEL_PATH_COPILOT + wert +  TexturePaths.WUERFEL_PATH_SELECTED;
            }
            final int wuerfelIndex = i;
            wuerfelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (prevSelectedButton[0] == wuerfelButton) {
                        return;
                    }
                    if (prevSelectedButton[0] != null && prevNormalTextureName[0] != null) {
                        switchTexture(prevSelectedButton[0], prevNormalTextureName[0]);
                    }
                    switchTexture(wuerfelButton, selectedTextureName);
                    prevSelectedButton[0] = wuerfelButton;
                    prevNormalTextureName[0] = normalTextureName;
                    selectedWuerfelData = new SelectedWuerfelData(player, wuerfelIndex, w);
                }
            });
        }
    }

    /**
     * Ändert die Textur eines ImageButtons.
     *
     * @param button Der Button, dessen Textur geändert werden soll.
     * @param newTexturePath Der Pfad zur neuen Textur.
     */
    private void switchTexture(ImageButton button, String newTexturePath) {
        Texture newTexture = TextureCache.get(newTexturePath);
        TextureRegionDrawable newDrawable = new TextureRegionDrawable(new TextureRegion(newTexture));
        button.getStyle().imageUp = newDrawable;
    }

    /**
     * Erstellt die grafischen Felder (ControlFields) für die Steuerung der Spieleraktionen.
     */
    private void createFieldActors() {
        controlGroups.add(guiConstructionMethods.createControlGroup(
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
            UILayout.PILOT_ACTOR_COORDINATES, stage, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]));

        controlGroups.add(guiConstructionMethods.createControlGroup(
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
            UILayout.COPILOT_ACTOR_COORDINATES, stage, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]));

        pilotKaffeeControlGroup = guiConstructionMethods.createControlGroup(
            ActionControlGroup.PILOT_COFFEE_CONTROLS,
            new ActionName[]{
                ActionName.PILOT_COFFEE_1,
                ActionName.PILOT_COFFEE_2,
                ActionName.PILOT_COFFEE_3
            },
            UILayout.KAFFEE_COORDINATES, stage, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]);
        copilotKaffeeControlGroup = guiConstructionMethods.createControlGroup(
            ActionControlGroup.COPILOT_COFFEE_CONTROLS,
            new ActionName[]{
                ActionName.COPILOT_COFFEE_1,
                ActionName.COPILOT_COFFEE_2,
                ActionName.COPILOT_COFFEE_3
            },
            UILayout.KAFFEE_COORDINATES, stage, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]);

        controlGroups.add(pilotKaffeeControlGroup);
        controlGroups.add(copilotKaffeeControlGroup);

        pilotUseKaffeeControlGroup = guiConstructionMethods.createUseKaffeeButtons(
            ActionControlGroup.PILOT_USE_COFFEE_CONTROL_GROUP,
            UILayout.KAFFEE_CONTROL_COORDINATES,
            stage
        );
        copilotUseKaffeeControlGroup = guiConstructionMethods.createUseKaffeeButtons(
            ActionControlGroup.COPILOT_USE_COFFEE_CONTROL_GROUP,
            UILayout.KAFFEE_CONTROL_COORDINATES,
            stage
        );

        controlGroups.add(pilotUseKaffeeControlGroup);
        controlGroups.add(copilotUseKaffeeControlGroup);


        this.neuWurf = guiConstructionMethods.createNeuWurfButton(stage);


        initMirrors();
    }

    /**
     * Aktiviert die Kontrollgruppen für die Kaffeeaktionen eines Spielers.
     *
     * @param current Der aktuelle Player, dessen Kaffeeaktionen aktiviert werden.
     */
    public void setKaffeeGroupsActive(Player current) {
        boolean isPilot = current instanceof Pilot;
        for (ControlField cf : pilotKaffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.enabled : Touchable.disabled);
        }
        for (ControlField cf : pilotUseKaffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.enabled : Touchable.disabled);
        }
        for (ControlField cf : copilotKaffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.disabled : Touchable.enabled);
        }
        for (ControlField cf : copilotUseKaffeeControlGroup.getAllFields()) {
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
            switchTexture(button, TexturePaths.AVAILABLE_FIELD_PATH);
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
            switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
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

    private ControlGroup getPilotKaffeeControlGroup() {
        return pilotKaffeeControlGroup;
    }

    private ControlGroup getCopilotKaffeeControlGroup() {
        return copilotKaffeeControlGroup;
    }

    private ControlGroup getPilotUseKaffeeControlGroup() {
        return pilotUseKaffeeControlGroup;
    }

    private ControlGroup getCopilotUseKaffeeControlGroup() {
        return copilotUseKaffeeControlGroup;
    }

    /**
     * Gibt die Kaffee-Kontrollgruppe eines bestimmten Spielers zurück.
     *
     * @param player Der Player, dessen Kaffee-Kontrollgruppe abgerufen werden soll.
     * @return Die entsprechende Kaffee-Kontrollgruppe.
     */
    private ControlGroup getKaffeeControlGroup(Player player) {
        return (player instanceof Pilot) ? getPilotKaffeeControlGroup() : getCopilotKaffeeControlGroup();
    }

    /**
     * Gibt die Kontrollgruppe für die Verwendung von Kaffee eines bestimmten Spielers zurück.
     *
     * @param player Der Player, dessen Kaffee-Verwendungsgruppe abgerufen werden soll.
     * @return Die entsprechende Kontrollgruppe für die Verwendung von Kaffee.
     */
    private ControlGroup getUseKaffeeControlGroup(Player player) {
        return (player instanceof Pilot) ? getPilotUseKaffeeControlGroup() : getCopilotUseKaffeeControlGroup();
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
        addKaffeeListeners(player);
    }

    /**
     * Fügt Click-Listener zu den Kaffee-Kontrollfeldern eines Spielers hinzu.
     *
     * @param player Der Player, dessen Kaffee-Kontrollfelder aktiviert werden sollen.
     */
    private void addKaffeeListeners(Player player) {
        ControlGroup kaffeeGroup = getKaffeeControlGroup(player);
        highlightAvailableFields(kaffeeGroup);
        for (ControlField field : kaffeeGroup.getAvailableFields()) {
            field.getButton().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleKaffeeFieldClick(field, player);
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
        ControlField pilotK1 = pilotKaffeeControlGroup.getFieldByAction(ActionName.PILOT_COFFEE_1);
        ControlField copilotK1 = copilotKaffeeControlGroup.getFieldByAction(ActionName.COPILOT_COFFEE_1);

        mirroredFields.put(pilotK1, copilotK1);
        mirroredFields.put(copilotK1, pilotK1);

        ControlField pilotK2 = pilotKaffeeControlGroup.getFieldByAction(ActionName.PILOT_COFFEE_2);
        ControlField copilotK2 = copilotKaffeeControlGroup.getFieldByAction(ActionName.COPILOT_COFFEE_2);

        mirroredFields.put(pilotK2, copilotK2);
        mirroredFields.put(copilotK2, pilotK2);

        ControlField pilotK3 = pilotKaffeeControlGroup.getFieldByAction(ActionName.PILOT_COFFEE_3);
        ControlField copilotK3 = copilotKaffeeControlGroup.getFieldByAction(ActionName.COPILOT_COFFEE_3);

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
    private void handleKaffeeFieldClick(ControlField field, Player player) {
        if (selectedWuerfelData == null || field.isOccupied()) {
            return;
        }

        Dice w = selectedWuerfelData.getWuerfel();
        boolean result = executeStep(field, player, w);

        if (!result) {
            return;
        }

        SelectedWuerfelData data = selectedWuerfelData;

        ControlField mirroredField = mirroredFields.get(field);
        blockFieldForBoth(field, mirroredField, data);
        removeWuerfelButtonFromHand(data);

        field.setOccupied(true);
        if (mirroredField != null) {
            mirroredField.setOccupied(true);
        }
        selectedWuerfelData = null;
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
    private void blockFieldForBoth(ControlField fieldPilot, ControlField fieldCopilot, SelectedWuerfelData data) {
        String texture = data.getDiceTexturePath();

        switchTexture(fieldPilot.getButton(), texture);
        fieldPilot.setOccupied(true);
        fieldPilot.getButton().setTouchable(Touchable.disabled);

        switchTexture(fieldCopilot.getButton(), texture);
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
        if (selectedWuerfelData == null || field.isOccupied()) {
            return;
        }
        boolean isPilot = player instanceof Pilot;
        Dice w = selectedWuerfelData.getWuerfel();

        boolean result = executeStep(field, player, w);
        if (!result) {
            return;
        }

        String texturePath = isPilot
            ? TexturePaths.WUERFEL_PATH_PILOT + w.getValue() + TexturePaths.IMG_FORMAT
            : TexturePaths.WUERFEL_PATH_COPILOT + w.getValue() + TexturePaths.IMG_FORMAT;
        removeWuerfelButtonFromHand(selectedWuerfelData);
        switchTexture(field.getButton(), texturePath);
        field.setOccupied(true);
        selectedWuerfelData = null;
        if (callback != null) {
            callback.onPlayerActionCompleted(player);
        }
    }

    /**
     * Entfernt den Button des Würfels aus der Hand eines Spielers.
     *
     * @param wuerfelData Die Daten des Würfels, der entfernt werden soll.
     */
    private void removeWuerfelButtonFromHand(SelectedWuerfelData wuerfelData) {
        Player owner = wuerfelData.getSpieler();
        int wuerfelIndex = wuerfelData.getWuerfelIndex();
        List<ImageButton> wuerfelActors = (owner instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        if (wuerfelIndex >= 0 && wuerfelIndex < wuerfelActors.size()) {
            ImageButton wuerfelButton = wuerfelActors.get(wuerfelIndex);
            wuerfelButton.remove();
            wuerfelActors.remove(wuerfelIndex);
        }
    }

    /**
     * Überprüft die aktuelle Situation der Felder für einen Player und führt ggf. Pflichtaktionen aus.
     *
     * @param player Der Player, dessen Feldsituation überprüft wird.
     */
    public void checkFeldSituation(Player player) {
        Field fieldNeigung;
        Field fieldSpeed;
        int anzahlWuerfels;
        if (player instanceof Pilot) {
            fieldNeigung = game.getFieldPilot(FieldName.TILT);
            fieldSpeed = game.getFieldPilot(FieldName.SPEED);
            anzahlWuerfels = game.getPilot().getAllDiceCount();
            player = game.getPilot();
        } else {
            fieldNeigung = game.getFieldCopilot(FieldName.TILT);
            fieldSpeed = game.getFieldCopilot(FieldName.SPEED);
            anzahlWuerfels = game.getCopilot().getAllDiceCount();
            player = game.getCopilot();
        }
        if (!fieldNeigung.isOccupied() && !fieldSpeed.isOccupied() && anzahlWuerfels == 2) {
            verpflicteteAktion(player);
        } else if (!fieldNeigung.isOccupied() && anzahlWuerfels == 1) {
            verpflicteteAktion(player);
        } else if (!fieldSpeed.isOccupied() && anzahlWuerfels == 1) {
            verpflicteteAktion(player);
        }
    }

    /**
     * Führt eine Pflichtaktion für einen Player aus, wenn diese erforderlich ist.
     *
     * @param player Der Player, der die Pflichtaktion ausführen muss.
     */
    void verpflicteteAktion(Player player) {
        Field fieldNeigung;
        Field fieldSpeed;
        ControlField neigungField;
        ControlField speedField;
        ControlGroup verpflichteteGroup;
        ControlGroup allFields;
        ControlGroup kaffeeFields;
        if (player instanceof Pilot) {
            fieldNeigung = game.getFieldPilot(FieldName.TILT);
            fieldSpeed = game.getFieldPilot(FieldName.SPEED);
            neigungField = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS).getFieldByAction(ActionName.PILOT_TILT);
            speedField = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS).getFieldByAction(ActionName.PILOT_SPEED);
            allFields = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS);
            kaffeeFields = pilotKaffeeControlGroup;
            player = game.getPilot();
        } else {
            fieldNeigung = game.getFieldCopilot(FieldName.TILT);
            fieldSpeed = game.getFieldCopilot(FieldName.SPEED);
            neigungField = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS).getFieldByAction(ActionName.COPILOT_TILT);
            speedField = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS).getFieldByAction(ActionName.COPILOT_SPEED);
            allFields = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
            kaffeeFields = copilotKaffeeControlGroup;
            player = game.getCopilot();
        }
        if (!fieldSpeed.isOccupied() && !fieldNeigung.isOccupied()) {
            createMessage("Achtung!", "Bitte setzen Sie die Würfel auf die Pflichtfelder");
            verpflichteteGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(neigungField, speedField));
            removeHighlight(allFields);
            removeHighlight(getPilotKaffeeControlGroup());
            removeHighlight(getCopilotKaffeeControlGroup());
            disableFields(allFields, verpflichteteGroup);
            disableFields(kaffeeFields, verpflichteteGroup);
            addListenersToGroupFields(verpflichteteGroup, player);
        } else if (!fieldSpeed.isOccupied()) {
            createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            verpflichteteGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(speedField));
            removeHighlight(allFields);
            removeHighlight(getPilotKaffeeControlGroup());
            removeHighlight(getCopilotKaffeeControlGroup());
            addListenersToGroupFields(verpflichteteGroup, player);
        } else {
            createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            verpflichteteGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(neigungField));
            removeHighlight(allFields);
            removeHighlight(getPilotKaffeeControlGroup());
            removeHighlight(getCopilotKaffeeControlGroup());
            addListenersToGroupFields(verpflichteteGroup, player);
        }
    }

    public SwitchGUI getSchalterGUI() {
        return switchGUI;
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
        ArrayList<ImageButton> wuerfelActors = (player instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        for (ImageButton button : wuerfelActors) {
            button.clearListeners();
        }

        ControlGroup group = (player instanceof Pilot)
            ? getControlGroupByName(ActionControlGroup.PILOT_CONTROLS)
            : getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
        for (ControlField field : group.getAvailableFields()) {
            field.getButton().clearListeners();
            switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        }


        if (player instanceof Pilot) {
            for (ControlField field : getPilotKaffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
            }
            for (ControlField field : getPilotUseKaffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
                switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            }
        } else {
            for (ControlField field : getCopilotKaffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
            }
            for (ControlField field : getCopilotUseKaffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
                switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
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
            switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            pilotGroup.getFieldByAction(s).setOccupied(false);
        }
        ActionName[] copilotManyUseFields = {ActionName.COPILOT_TILT, ActionName.COPILOT_SPEED, ActionName.COPILOT_RADIO_1,  ActionName.COPILOT_RADIO_2};
        for (ActionName s : copilotManyUseFields) {
            ImageButton button = copilotGroup.getFieldByAction(s).getButton();
            switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            copilotGroup.getFieldByAction(s).setOccupied(false);
        }
        showNeuWurf(false);
        showKaffee(false, false);
    }


    /**
     * Zeigt die Kaffee-Kontrollgruppen an oder blendet sie aus.
     *
     * @param pilotAvailable Gibt an, ob die Kaffeegruppen des Piloten aktiv sein sollen.
     * @param copilotAvailable Gibt an, ob die Kaffeegruppen des Copiloten aktiv sein sollen.
     */
    public void showKaffee(Boolean pilotAvailable, Boolean copilotAvailable) {
        int kaffeeCount = game.getCoffeeTokenCount();
        ControlGroup pilotUseGroup = getPilotUseKaffeeControlGroup();
        if (!pilotAvailable) {
            for (ControlField field : pilotUseGroup.getAllFields()) {
                switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
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
                        switchTexture(field.getButton(), TexturePaths.KAFFEE_PATH);
                    } else {
                        field.setOccupied(false);
                        switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                        field.getButton().setTouchable(Touchable.disabled);
                    }
                }
            }
        }

        ControlGroup copilotUseGroup = getCopilotUseKaffeeControlGroup();
        if (!copilotAvailable) {
            for (ControlField field : copilotUseGroup.getAllFields()) {
                switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
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
                        switchTexture(field.getButton(), TexturePaths.KAFFEE_PATH);
                    } else {
                        field.setOccupied(false);
                        switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
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
    public void useKaffee(Player s) {
        int kaffeeCount = game.getCoffeeTokenCount();
        ControlGroup useGroup = getUseKaffeeControlGroup(s);

        if (kaffeeCount > 0) {
            ControlField field = useGroup.getFieldByAction(
                ActionName.useCoffee(kaffeeCount)
            );
            if (field != null && field.isOccupied()) {
                field.getButton().clearListeners();
                field.getButton().setTouchable(Touchable.enabled);
                switchTexture(field.getButton(), TexturePaths.KAFFEE_AVAILABLE_PATH);
                field.getButton().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        switchTexture(field.getButton(), TexturePaths.KAFFEE_SELECTED_PATH);
                        createMessage("Kaffee-Token verwenden", "Bitte wählen Sie einen Würfel aus, dessen Wert Sie ändern möchten!");
                        waitForDiceSelection(s, field.getAction());
                    }
                });
            }
        } else {
            for (ControlField f : useGroup.getAllFields()) {
                f.getButton().clearListeners();
                switchTexture(f.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
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
        ArrayList<ImageButton> wuerfelActors = (player instanceof Pilot)
            ? pilotWuerfelActors
            : copilotWuerfelActors;
        ArrayList<Dice> dices = player.getAllDice();
        String path = (player instanceof Pilot)
            ? TexturePaths.WUERFEL_PATH_PILOT
            : TexturePaths.WUERFEL_PATH_COPILOT;

        for (int i = 0; i < wuerfelActors.size(); i++) {
            final ImageButton wuerfelButton = wuerfelActors.get(i);
            final Dice dice = dices.get(i);
            final int index = i;
            final int wert = dice.getValue();

            wuerfelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switchTexture(wuerfelButton, path + wert + TexturePaths.WUERFEL_PATH_SELECTED);
                    clearDiceListeners(player);
                    createKaffeeTokenWindow(dice, player, index, (neuerWert) -> {
                        switchTexture(wuerfelButton, path + neuerWert + TexturePaths.IMG_FORMAT);
                        removeKaffeeTokenByName(name, player);
                        kaffeeFieldClearLastUsed(player);
                        checkFeldSituation(player);
                        addWuerfelListenersForSpieler(player);

                        if (game.getCoffeeTokenCount() > 0) {
                            useKaffee(player);
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
    private void kaffeeFieldClearLastUsed(Player s) {
        List<ControlField> pilotFields = pilotKaffeeControlGroup.getAllFields();
        List<ControlField> copilotFields = copilotKaffeeControlGroup.getAllFields();

        List<ControlField> allKaffeeFields = new ArrayList<>();
        allKaffeeFields.addAll(pilotFields);
        allKaffeeFields.addAll(copilotFields);

        for (int i = allKaffeeFields.size() - 1; i >= 0; i--) {
            ControlField field = allKaffeeFields.get(i);
            if (field.isOccupied()) {
                freeKaffeeFieldForBoth(field, s);
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
    private void freeKaffeeFieldForBoth(ControlField field, Player s) {
        ControlField mirrored = mirroredFields.get(field);

        if (s instanceof Pilot) {
            field.setOccupied(false);
            switchTexture(field.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
            mirrored.setOccupied(false);
            switchTexture(mirrored.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
            mirrored.getButton().setTouchable(Touchable.enabled);
            addKaffeeListeners(s);

        } else {
            field.setOccupied(false);
            switchTexture(field.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
            mirrored.setOccupied(false);
            switchTexture(mirrored.getButton(), TexturePaths.AVAILABLE_FIELD_PATH);
            field.getButton().setTouchable(Touchable.enabled);
            addKaffeeListeners(s);
        }
    }

    /**
     * Entfernt alle Listener von den Würfeln eines Spielers.
     *
     * @param player Der Player, dessen Würfel zurückgesetzt werden sollen.
     */
    private void clearDiceListeners(Player player) {
        ArrayList<ImageButton> wuerfelActors = (player instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        for (ImageButton ib : wuerfelActors) {
            ib.clearListeners();
        }
    }

    /**
     * Entfernt ein verwendetes Kaffee-Token aus der Kontrollgruppe.
     *
     * @param name Der Name des verwendeten Kaffee-Kontrollfeldes.
     * @param player Der Player, dessen Kaffee-Token entfernt werden soll.
     */
    private void removeKaffeeTokenByName(ActionName name, Player player) {
        ControlGroup useGroup = getUseKaffeeControlGroup(player);
        ControlField usedField = useGroup.getFieldByAction(name);
        if (usedField != null) {
            usedField.setOccupied(false);
            switchTexture(usedField.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            usedField.getButton().setTouchable(Touchable.disabled);
        }
    }

    /**
     * Zeigt oder versteckt den Button für den "Neu Wurf"-Token.
     *
     * @param available Gibt an, ob der Button aktiv oder deaktiviert sein soll.
     */
    public void showNeuWurf(Boolean available) {
        int anzahlTokens = game.getRerollTokenCount();
        ImageButton button = neuWurf.getButton();
        if (anzahlTokens == 0) {
            switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            button.setTouchable(Touchable.disabled);
        } else {
            if (available) {
                button.setTouchable(Touchable.enabled);
                switchTexture(button, TexturePaths.NEUWURF_AVAILABLE_PATH);
            } else {
                switchTexture(button, TexturePaths.NEUWURF_PATH);
            }
        }
    }

    /**
     * Aktiviert die Verwendung eines "Neu Wurf"-Tokens für den Player.
     *
     * @param player Der Player, der ein "Neu Wurf"-Token verwenden möchte.
     */
    public void useNeuWurf(Player player) {
        ImageButton button = neuWurf.getButton();
        button.clearListeners();
        if (game.getRerollTokenCount() > 0) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switchTexture(button, TexturePaths.NEUWURF_SELECTED_PATH);
                    createNeuWurfTokenWindow(player, (p1, p2) -> {
                        if (p1 != null) {
                            createWuerfelForSpieler(p1);
                            addWuerfelListenersForSpieler(p1);
                        }
                        if (p2 != null) {
                            createWuerfelForSpieler(p2);
                            addWuerfelListenersForSpieler(p2);
                        }
                        if (game.getRerollTokenCount() == 0) {
                            switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                            button.setTouchable(Touchable.disabled);
                        } else {
                            button.setTouchable(Touchable.enabled);
                            switchTexture(button, TexturePaths.NEUWURF_AVAILABLE_PATH);
                        }
                        removeAllHideImages();
                        if( (player instanceof Pilot) ){
                            hideWuerfel(player);
                        } else {
                            hideWuerfel(game.getCopilot());
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
        if (ruder != null && ruder.getDrawable() instanceof TextureRegionDrawable) {
            ruder.remove();
            ruder = null;
        }

        if (guiConstructionMethods != null) {
            guiConstructionMethods.dispose();

        }

        if (switchGUI != null) {
            switchGUI.dispose();
            switchGUI = null;
        }

        if (neuWurf != null && neuWurf.getButton() != null) {
            ImageButton button = neuWurf.getButton();
            button.remove();
            neuWurf = null;
        }

        for (ImageButton button : pilotWuerfelActors) {
            button.remove();
        }
        pilotWuerfelActors.clear();

        for (ImageButton button : copilotWuerfelActors) {
            button.remove();
        }
        copilotWuerfelActors.clear();

        if (pilotKaffeeControlGroup != null) {
            pilotKaffeeControlGroup.dispose();
            pilotKaffeeControlGroup = null;
        }
        if (copilotKaffeeControlGroup != null) {
            copilotKaffeeControlGroup.dispose();
            copilotKaffeeControlGroup = null;
        }
        if (pilotUseKaffeeControlGroup != null) {
            pilotUseKaffeeControlGroup.dispose();
            pilotUseKaffeeControlGroup = null;
        }
        if (copilotUseKaffeeControlGroup != null) {
            copilotUseKaffeeControlGroup.dispose();
            copilotUseKaffeeControlGroup = null;
        }
        for (ControlGroup group : controlGroups) {
            group.dispose();
        }

        controlGroups.clear();

        mirroredFields.clear();
        selectedWuerfelData = null;


        gameService = null;
        skin = null;
        callback = null;
        campaignAltitudeManager = null;
    }

    /**
     * Blendet die Würfel des anderen Spielers aus, um sie zu verbergen.
     *
     * @param player Der aktuelle Player, dessen Würfel sichtbar bleiben sollen.
     */
    public void hideWuerfel(Player player) {
        if (player instanceof Pilot) {
            renderHideImage(UILayout.COPILOT_HIDING_SCREEN_COORDINATES[0], UILayout.COPILOT_HIDING_SCREEN_COORDINATES[1]);
            removeHideImage(UILayout.PILOT_HIDING_SCREEN_COORDINATES[0], UILayout.PILOT_HIDING_SCREEN_COORDINATES[1]);
        } else {
            renderHideImage(UILayout.PILOT_HIDING_SCREEN_COORDINATES[0], UILayout.PILOT_HIDING_SCREEN_COORDINATES[1]);
            removeHideImage(UILayout.COPILOT_HIDING_SCREEN_COORDINATES[0], UILayout.COPILOT_HIDING_SCREEN_COORDINATES[1]);
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

















    private Window window;

    /**
     * Zeigt ein modales Fenster mit einer Nachricht an.
     *
     * @param title Der Titel des Fensters.
     * @param text  Der Inhalt der Nachricht.
     */
    public void createMessage(String title, String text) {
        if (window == null) {
            window = new Window(title, skin);
            window.setModal(true);
            window.setMovable(false);
            window.add(text).pad(20).row();
            TextButton closeButton = new TextButton("OK", skin);
            closeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    window.remove();
                    window = null;
                }
            });
            window.add(closeButton).pad(10).row();
            window.pack();
            window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2f, (Gdx.graphics.getHeight() - window.getHeight()) / 2f);
            stage.addActor(window);
        }
    }

    /**
     * Erstellt ein modales Fenster, um den Wert eines Würfels mit einem Kaffee-Token zu ändern.
     * Der Player kann den Wert des Würfels entweder erhöhen oder verringern, solange die Grenzen eingehalten werden.
     *
     * @param dice  Der Würfel, dessen Wert geändert werden soll.
     * @param player  Der Player, der das Kaffee-Token verwendet.
     * @param index    Der Index des Würfels in der Würfelliste des Spielers.
     * @param callback Eine Callback-Funktion, die den neuen Wert des Würfels verarbeitet.
     */
    public void createKaffeeTokenWindow(Dice dice, Player player, int index, Consumer<Integer> callback) {
        if (window == null) {
            window = new Window("Wert aendern", skin);
            window.setModal(true);
            window.getTitleLabel().setAlignment(Align.center);

            window.add("Wie moechten Sie den Wert des Wuerfels aendern?").pad(20).center().row();

            Table buttonTable = new Table();
            buttonTable.center();

            TextButton plusButton = new TextButton("Plus", skin);
            plusButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (dice.getValue() < 6) {
                        player.useCoffeToken(gameStateManager.takeCoffeeToken(), CoffeeTokenAction.PLUS, dice);
                        int neuerWert = player.getDice(dice).getValue();
                        fieldManager.clearCoffeeFields(game, game.getCoffeeTokenCount());
                        window.remove();
                        window = null;
                        callback.accept(neuerWert);
                    } else {
                        window.remove();
                        window = null;
                        createMessage("Fehler", "Das Würfel-Ergebnis kann nicht höher sein!");
                    }
                }
            });
            TextButton minusButton = new TextButton("Minus", skin);
            minusButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (dice.getValue() > 1) {
                        player.useCoffeToken(gameStateManager.takeCoffeeToken(), CoffeeTokenAction.MINUS, dice);
                        int neuerWert = player.getAllDice().get(index).getValue();
                        fieldManager.clearCoffeeFields(game, game.getCoffeeTokenCount());
                        window.remove();
                        window = null;
                        callback.accept(neuerWert);
                    } else {
                        window.remove();
                        window = null;
                        createMessage("Fehler", "Das Würfel-Ergebnis kann nicht niedriger sein!");
                    }
                }
            });
            buttonTable.add(plusButton).padRight(20);
            buttonTable.add(minusButton).padLeft(20);
            window.add(buttonTable).center().pad(20).row();
            window.pack();
            window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2f, (Gdx.graphics.getHeight() - window.getHeight()) / 2f);
            stage.addActor(window);
        }
    }

    /**
     * Erstellt ein modales Fenster, um ein NeuWurf-Token zu verwenden.
     * Der Player kann entscheiden, ob er neue Würfel einem Player oder beiden Spielern gleichzeitig gibt.
     *
     * @param player  Der Player, der das NeuWurf-Token verwendet.
     * @param callback Eine Callback-Funktion, die die Player aktualisiert, die neue Würfel erhalten haben.
     */
    public void createNeuWurfTokenWindow(Player player, BiConsumer<Player, Player> callback) {
        if (window == null) {
            window = new Window("Neu createRandomDice", skin);
            window.setModal(true);
            window.setMovable(false);
            Label label = new Label("Wem möchten Sie neue Würfel geben?", skin);
            window.add(label).pad(20).row();
            Table buttonTable = new Table();
            RerollToken token = gameStateManager.takeRerollToken();
            Player pilot = game.getPilot();
            Player copilot = game.getCopilot();
            TextButton pilotButton = new TextButton("Pilot", skin);
            pilotButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.useRerollToken(token, pilot);
                    window.remove();
                    window = null;
                    callback.accept(pilot, null);
                }
            });
            TextButton copilotButton = new TextButton("Copilot", skin);
            copilotButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.useRerollToken(token, copilot);
                    window.remove();
                    window = null;
                    callback.accept(null, copilot);
                }
            });
            TextButton beideButton = new TextButton("Beide", skin);
            beideButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.useRerollToken(token, pilot, copilot);
                    window.remove();
                    window = null;
                    callback.accept(pilot, copilot);
                }
            });
            buttonTable.add(pilotButton).pad(5);
            buttonTable.add(copilotButton).pad(5);
            buttonTable.add(beideButton).pad(5);
            window.add(buttonTable).center().pad(10).row();
            window.pack();
            window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2f, (Gdx.graphics.getHeight() - window.getHeight()) / 2f);
            stage.addActor(window);
        }
    }

    /**
     * Erstellt ein Fenster, das anzeigt, dass das Game gewonnen oder verloren wurde.
     *
     * @param win true, wenn das Game gewonnen wurde; false, wenn es verloren wurde,
     * das Bild im Fenster ist davon abhängig
     */
    public void createFinalWindow(boolean win) {
        if (window != null) {
            window.remove();
            window = null;
        }
        String path;
        String title;
        float w;
        float h;
        String buttonText;
        if (win) {
            path = TexturePaths.WIN_PATH;
            w = 800;
            h = 550;
            buttonText = "Hurra!";
            title = "Sieg!";
        } else {
            path = TexturePaths.LOOSE_PATH;
            w = 800;
            h = 450;
            buttonText = "Schade!";
            title = "Verloren!";
        }
        window = new Window(title, skin);
        window.setModal(true);
        Texture texture = new Texture(path);
        Image image = new Image(texture);
        window.add(image).pad(10).row();
        TextButton closeButton = new TextButton(buttonText, skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
                window = null;
                Gdx.app.exit();
            }
        });
        window.add(closeButton).pad(10).row();
        window.setSize(w, h);
        window.setPosition((Gdx.graphics.getWidth() - window.getWidth()) / 2f, (Gdx.graphics.getHeight() - window.getHeight()) / 2f);
        stage.addActor(window);
    }


    /*
    * GameStateObserver
    * */

    @Override
    public void onBrakesActivated(int switchIndex, int indicatorValue) {
        switchGUI.activateBremsenSchalter(switchIndex);
        switchGUI.moveBremsenIndicator(indicatorValue);
    }

    @Override
    public void onLandingGearActivated(int switchIndex, int count) {
        switchGUI.activateFahrgestellSchalter(switchIndex);
        switchGUI.moveFahrgestellIndicator(count);
    }

    @Override
    public void onFlapsActivated(int switchIndex, int state) {
        switchGUI.activateKlappenSchalter(switchIndex);
        switchGUI.moveKlappenIndicator(state);
    }

    @Override
    public void onTiltChanged(float targetRotation) {
        ruder.addAction(Actions.rotateTo(targetRotation, 1f));
    }

    @Override
    public void onSpeedChanged(int steps) {
        campaignAltitudeManager.updateScenario(steps);
    }





}
