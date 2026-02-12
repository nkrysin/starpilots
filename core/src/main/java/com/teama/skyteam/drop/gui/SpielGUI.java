/**
 * Repräsentiert die grafische Benutzeroberfläche (GUI) des Spiels.
 *
 * Die Klasse `SpielGUI` verwaltet die Erstellung und Steuerung aller visuellen Elemente,
 * wie z. B. Würfel, Schalter und Steuergruppen, sowie die Interaktion zwischen Spielern
 * und der Spiel-Logik. Sie integriert verschiedene GUI-Komponenten wie Schalter, Kaffeegruppen
 * und Würfel und sorgt für die Synchronisation mit der Spielmechanik.
 */
package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.gui.buttons.ControlField;
import com.teama.skyteam.drop.gui.buttons.ControlGroup;
import com.teama.skyteam.drop.gui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.gui.utilities.SelectedWuerfelData;
import com.teama.skyteam.drop.gui.utilities.TextureCache;
import com.teama.skyteam.drop.spiel.action.type.ActionControlGroup;
import com.teama.skyteam.drop.spiel.action.type.ActionName;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.SpielLogik;
import com.teama.skyteam.drop.feld.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpielGUI {

    public final Stage stage;
    private final Spiel spiel;
    private final GUIConstructionMethods guiConstructionMethods;
    private ArrayList<ImageButton> pilotWuerfelActors = new ArrayList<>();
    private ArrayList<ImageButton> copilotWuerfelActors = new ArrayList<>();
    private ArrayList<ControlGroup> controlGroups = new ArrayList<>();
    private ControlGroup pilotKaffeeControlGroup;
    private ControlGroup copilotKaffeeControlGroup;
    private ControlGroup pilotUseKaffeeControlGroup;
    private ControlGroup copilotUseKaffeeControlGroup;
    private SelectedWuerfelData selectedWuerfelData = null;
    private SpielLogik spielLogik;
    private Skin skin;
    private SchalterGUI schalterGUI;
    private Image ruder;
    private PlayerActionCallback callback;
    private SzenarioHoeheManager szenarioHoeheManager;
    private ControlField neuWurf;
    private Map<ControlField, ControlField> mirroredFields = new HashMap<>();

    /**
     * Konstruktor zur Initialisierung der Spiel-GUI.
     *
     * @param spiel Die aktuelle Spiel-Logik.
     * @param stage Die Bühne, auf der die grafischen Elemente platziert werden.
     * @param skin Das Skin-Objekt für die Gestaltung der GUI-Elemente.
     * @param szenarioHoeheManager Der Manager zur Verwaltung der Szenariohöhe.
     */
    public SpielGUI(Spiel spiel, Stage stage, Skin skin, SzenarioHoeheManager szenarioHoeheManager) {
        this.spiel = spiel;
        this.stage = stage;
        this.skin = skin;
        setRuder();
        this.guiConstructionMethods = new GUIConstructionMethods();
        schalterGUI = new SchalterGUI(stage);
        schalterGUI.addToStage();
        this.szenarioHoeheManager = szenarioHoeheManager;
        spielLogik = new SpielLogik(spiel, stage, skin, schalterGUI, ruder, szenarioHoeheManager);
        createFieldActors();
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
     * Erstellt grafische Würfel für den angegebenen Spieler und fügt sie der Bühne hinzu.
     *
     * @param spieler Der Spieler, für den die Würfel erstellt werden.
     */
    public void createWuerfelForSpieler(Spieler spieler) {
        if (spieler instanceof Pilot) {
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

        ArrayList<Wuerfel> wuerfels = spieler.getWuerfels();
        ArrayList<ImageButton> wuerfelActors = new ArrayList<>();

        for (int i = 0; i < wuerfels.size(); i++) {
            Wuerfel w = wuerfels.get(i);
            int wert = w.getWert();
            String textureName;
            int x, y;
            if (spieler instanceof Pilot) {
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

        if (spieler instanceof Pilot) {
            pilotWuerfelActors = wuerfelActors;
        } else {
            copilotWuerfelActors = wuerfelActors;
        }
        hideWuerfel(spieler);
    }

    /**
     * Fügt den Würfeln eines Spielers Click-Listener hinzu.
     *
     * @param spieler Der Spieler, dessen Würfel interaktiv gemacht werden.
     */
    public void addWuerfelListenersForSpieler(Spieler spieler) {
        ArrayList<ImageButton> wuerfelActors = (spieler instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        ArrayList<Wuerfel> wuerfels = spieler.getWuerfels();
        final ImageButton[] prevSelectedButton = {null};
        final String[] prevNormalTextureName = {null};

        for (int i = 0; i < wuerfelActors.size(); i++) {
            final ImageButton wuerfelButton = wuerfelActors.get(i);
            final Wuerfel w = wuerfels.get(i);
            final int wert = w.getWert();
            final boolean isPilot = (spieler instanceof Pilot);
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
                    selectedWuerfelData = new SelectedWuerfelData(spieler, wuerfelIndex, w);
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
                ActionName.PILOT_NEIGUNG,
                ActionName.PILOT_SPEED,
                ActionName.PILOT_FAHRGESTELL_1_2,
                ActionName.PILOT_FAHRGESTELL_3_4,
                ActionName.PILOT_FAHRGESTELL_5_6,
                ActionName.PILOT_BREMSEN_2,
                ActionName.PILOT_BREMSEN_4,
                ActionName.PILOT_BREMSEN_6
            },
            UILayout.PILOT_ACTOR_COORDINATES, stage, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]));

        controlGroups.add(guiConstructionMethods.createControlGroup(
            ActionControlGroup.COPILOT_CONTROLS,
            new ActionName[]{
                ActionName.COPILOT_NEIGUNG,
                ActionName.COPILOT_SPEED,
                ActionName.COPILOT_RADIO_1,
                ActionName.COPILOT_RADIO_2,
                ActionName.COPILOT_KLAPPEN_1_2,
                ActionName.COPILOT_KLAPPEN_2_3,
                ActionName.COPILOT_KLAPPEN_4_5,
                ActionName.COPILOT_KLAPPEN_5_6
            },
            UILayout.COPILOT_ACTOR_COORDINATES, stage, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]));

        pilotKaffeeControlGroup = guiConstructionMethods.createControlGroup(
            ActionControlGroup.PILOT_KAFFEE_CONTROLS,
            new ActionName[]{
                ActionName.PILOT_KAFFEE_1,
                ActionName.PILOT_KAFFEE_2,
                ActionName.PILOT_KAFFEE_3
            },
            UILayout.KAFFEE_COORDINATES, stage, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]);
        copilotKaffeeControlGroup = guiConstructionMethods.createControlGroup(
            ActionControlGroup.COPILOT_KAFFEE_CONTROLS,
            new ActionName[]{
                ActionName.COPILOT_KAFFEE_1,
                ActionName.COPILOT_KAFFEE_2,
                ActionName.COPILOT_KAFFEE_3
            },
            UILayout.KAFFEE_COORDINATES, stage, UILayout.WUERFEL_SIZE[0], UILayout.WUERFEL_SIZE[1]);

        controlGroups.add(pilotKaffeeControlGroup);
        controlGroups.add(copilotKaffeeControlGroup);

        pilotUseKaffeeControlGroup = guiConstructionMethods.createUseKaffeeButtons(
            ActionControlGroup.PILOT_USE_KAFFEE_CONTROL_GROUP,
            UILayout.KAFFEE_CONTROL_COORDINATES,
            stage
        );
        copilotUseKaffeeControlGroup = guiConstructionMethods.createUseKaffeeButtons(
            ActionControlGroup.COPILOT_USE_KAFFEE_CONTROL_GROUP,
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
     * @param current Der aktuelle Spieler, dessen Kaffeeaktionen aktiviert werden.
     */
    public void setKaffeeGroupsActive(Spieler current) {
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
     * @param spieler Der Spieler, dessen Hauptkontrollgruppe abgerufen werden soll.
     * @return Die Hauptkontrollgruppe des Spielers.
     */
    private ControlGroup getMainControlGroup(Spieler spieler) {
        return (spieler instanceof Pilot)
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
     * @param spieler Der Spieler, dessen Kaffee-Kontrollgruppe abgerufen werden soll.
     * @return Die entsprechende Kaffee-Kontrollgruppe.
     */
    private ControlGroup getKaffeeControlGroup(Spieler spieler) {
        return (spieler instanceof Pilot) ? getPilotKaffeeControlGroup() : getCopilotKaffeeControlGroup();
    }

    /**
     * Gibt die Kontrollgruppe für die Verwendung von Kaffee eines bestimmten Spielers zurück.
     *
     * @param spieler Der Spieler, dessen Kaffee-Verwendungsgruppe abgerufen werden soll.
     * @return Die entsprechende Kontrollgruppe für die Verwendung von Kaffee.
     */
    private ControlGroup getUseKaffeeControlGroup(Spieler spieler) {
        return (spieler instanceof Pilot) ? getPilotUseKaffeeControlGroup() : getCopilotUseKaffeeControlGroup();
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
     * @param spieler Der Spieler, dessen Würfel platziert werden sollen.
     */
    public void addFieldListenersForPlacement(Spieler spieler) {
        ControlGroup mainGroup = getMainControlGroup(spieler);
        addListenersToGroupFields(mainGroup, spieler);
        addKaffeeListeners(spieler);
    }

    /**
     * Fügt Click-Listener zu den Kaffee-Kontrollfeldern eines Spielers hinzu.
     *
     * @param spieler Der Spieler, dessen Kaffee-Kontrollfelder aktiviert werden sollen.
     */
    private void addKaffeeListeners(Spieler spieler) {
        ControlGroup kaffeeGroup = getKaffeeControlGroup(spieler);
        highlightAvailableFields(kaffeeGroup);
        for (ControlField field : kaffeeGroup.getAvailableFields()) {
            field.getButton().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleKaffeeFieldClick(field, spieler);
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
        ControlField pilotK1 = pilotKaffeeControlGroup.getFieldByAction(ActionName.PILOT_KAFFEE_1);
        ControlField copilotK1 = copilotKaffeeControlGroup.getFieldByAction(ActionName.COPILOT_KAFFEE_1);

        mirroredFields.put(pilotK1, copilotK1);
        mirroredFields.put(copilotK1, pilotK1);

        ControlField pilotK2 = pilotKaffeeControlGroup.getFieldByAction(ActionName.PILOT_KAFFEE_2);
        ControlField copilotK2 = copilotKaffeeControlGroup.getFieldByAction(ActionName.COPILOT_KAFFEE_2);

        mirroredFields.put(pilotK2, copilotK2);
        mirroredFields.put(copilotK2, pilotK2);

        ControlField pilotK3 = pilotKaffeeControlGroup.getFieldByAction(ActionName.PILOT_KAFFEE_3);
        ControlField copilotK3 = copilotKaffeeControlGroup.getFieldByAction(ActionName.COPILOT_KAFFEE_3);

        mirroredFields.put(pilotK3, copilotK3);
        mirroredFields.put(copilotK3, pilotK3);
    }

    /**
     * Behandelt die Aktion beim Klicken auf ein Kaffee-Kontrollfeld.
     *
     * @param field Das angeklickte Kontrollfeld.
     * @param spieler Der Spieler, der die Aktion ausführt.
     */
    private void handleKaffeeFieldClick(ControlField field, Spieler spieler) {
        if (selectedWuerfelData == null || field.isOccupied()) {
            return;
        }

        boolean isPilot = (spieler instanceof Pilot);
        Wuerfel w = selectedWuerfelData.getWuerfel();
        int index = selectedWuerfelData.getWuerfelIndex();

        boolean stepSuccessful = isPilot
            ? spielLogik.pilotSchritt(spieler, field.getAction(), w)
            : spielLogik.copilotSchritt(spieler, field.getAction(), w);

        if (!stepSuccessful) {
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
            callback.onPlayerActionCompleted(spieler);
        }

    }

    /**
     * Fügt Click-Listener zu den Hauptkontrollfeldern eines Spielers hinzu.
     *
     * @param group Die Hauptkontrollgruppe des Spielers.
     * @param spieler Der Spieler, dessen Felder aktiviert werden.
     */
    private void addListenersToGroupFields(ControlGroup group, Spieler spieler) {
        highlightAvailableFields(group);
        for (ControlField field : group.getAvailableFields()) {
            field.getButton().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleMainFieldClick(field, spieler);
                }
            });
        }
    }

    /**
     * Blockiert ein Feld für beide Spieler (Pilot und Copilot) und markiert es als belegt.
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
     * @param spieler Der Spieler, der die Aktion ausführt.
     */
    private void handleMainFieldClick(ControlField field, Spieler spieler) {
        if (selectedWuerfelData == null || field.isOccupied()) {
            return;
        }
        boolean isPilot = spieler instanceof Pilot;
        Wuerfel w = selectedWuerfelData.getWuerfel();
        int index = selectedWuerfelData.getWuerfelIndex();
        boolean stepSuccessful = isPilot
            ? spielLogik.pilotSchritt(spieler, field.getAction(), w, index)
            : spielLogik.copilotSchritt(spieler, field.getAction(), w, index);
        if (!stepSuccessful) {
            return;
        }
        String texturePath = isPilot
            ? TexturePaths.WUERFEL_PATH_PILOT + w.getWert() + TexturePaths.IMG_FORMAT
            : TexturePaths.WUERFEL_PATH_COPILOT + w.getWert() + TexturePaths.IMG_FORMAT;
        removeWuerfelButtonFromHand(selectedWuerfelData);
        switchTexture(field.getButton(), texturePath);
        field.setOccupied(true);
        selectedWuerfelData = null;
        if (callback != null) {
            callback.onPlayerActionCompleted(spieler);
        }
    }

    /**
     * Entfernt den Button des Würfels aus der Hand eines Spielers.
     *
     * @param wuerfelData Die Daten des Würfels, der entfernt werden soll.
     */
    private void removeWuerfelButtonFromHand(SelectedWuerfelData wuerfelData) {
        Spieler owner = wuerfelData.getSpieler();
        int wuerfelIndex = wuerfelData.getWuerfelIndex();
        List<ImageButton> wuerfelActors = (owner instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        if (wuerfelIndex >= 0 && wuerfelIndex < wuerfelActors.size()) {
            ImageButton wuerfelButton = wuerfelActors.get(wuerfelIndex);
            wuerfelButton.remove();
            wuerfelActors.remove(wuerfelIndex);
        }
    }

    /**
     * Überprüft die aktuelle Situation der Felder für einen Spieler und führt ggf. Pflichtaktionen aus.
     *
     * @param spieler Der Spieler, dessen Feldsituation überprüft wird.
     */
    public void checkFeldSituation(Spieler spieler) {
        Feld feldNeigung;
        Feld feldSpeed;
        int anzahlWuerfels;
        if (spieler instanceof Pilot) {
            feldNeigung = spiel.getFeldPilot(FeldName.NEIGEN);
            feldSpeed = spiel.getFeldPilot(FeldName.SPEED);
            anzahlWuerfels = spiel.getPilot().getAnzahlWuerfels();
            spieler = spiel.getPilot();
        } else {
            feldNeigung = spiel.getFeldCopilot(FeldName.NEIGEN);
            feldSpeed = spiel.getFeldCopilot(FeldName.SPEED);
            anzahlWuerfels = spiel.getCopilot().getAnzahlWuerfels();
            spieler = spiel.getCopilot();
        }
        if (!feldNeigung.isBelegt() && !feldSpeed.isBelegt() && anzahlWuerfels == 2) {
            verpflicteteAktion(spieler);
        } else if (!feldNeigung.isBelegt() && anzahlWuerfels == 1) {
            verpflicteteAktion(spieler);
        } else if (!feldSpeed.isBelegt() && anzahlWuerfels == 1) {
            verpflicteteAktion(spieler);
        }
    }

    /**
     * Führt eine Pflichtaktion für einen Spieler aus, wenn diese erforderlich ist.
     *
     * @param spieler Der Spieler, der die Pflichtaktion ausführen muss.
     */
    void verpflicteteAktion(Spieler spieler) {
        Feld feldNeigung;
        Feld feldSpeed;
        ControlField neigungField;
        ControlField speedField;
        ControlGroup verpflichteteGroup;
        ControlGroup allFields;
        ControlGroup kaffeeFields;
        if (spieler instanceof Pilot) {
            feldNeigung = spiel.getFeldPilot(FeldName.NEIGEN);
            feldSpeed = spiel.getFeldPilot(FeldName.SPEED);
            neigungField = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS).getFieldByAction(ActionName.PILOT_NEIGUNG);
            speedField = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS).getFieldByAction(ActionName.PILOT_SPEED);
            allFields = getControlGroupByName(ActionControlGroup.PILOT_CONTROLS);
            kaffeeFields = pilotKaffeeControlGroup;
            spieler = spiel.getPilot();
        } else {
            feldNeigung = spiel.getFeldCopilot(FeldName.NEIGEN);
            feldSpeed = spiel.getFeldCopilot(FeldName.SPEED);
            neigungField = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS).getFieldByAction(ActionName.COPILOT_NEIGUNG);
            speedField = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS).getFieldByAction(ActionName.COPILOT_SPEED);
            allFields = getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
            kaffeeFields = copilotKaffeeControlGroup;
            spieler = spiel.getCopilot();
        }
        if (!feldSpeed.isBelegt() && !feldNeigung.isBelegt()) {
            spielLogik.createMessage("Achtung!", "Bitte setzen Sie die Würfel auf die Pflichtfelder");
            verpflichteteGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(neigungField, speedField));
            removeHighlight(allFields);
            removeHighlight(getPilotKaffeeControlGroup());
            removeHighlight(getCopilotKaffeeControlGroup());
            disableFields(allFields, verpflichteteGroup);
            disableFields(kaffeeFields, verpflichteteGroup);
            addListenersToGroupFields(verpflichteteGroup, spieler);
        } else if (!feldSpeed.isBelegt()) {
            spielLogik.createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            verpflichteteGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(speedField));
            removeHighlight(allFields);
            removeHighlight(getPilotKaffeeControlGroup());
            removeHighlight(getCopilotKaffeeControlGroup());
            addListenersToGroupFields(verpflichteteGroup, spieler);
        } else {
            spielLogik.createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            verpflichteteGroup = new ControlGroup(ActionControlGroup.REQUIRED_CONTROLS, List.of(neigungField));
            removeHighlight(allFields);
            removeHighlight(getPilotKaffeeControlGroup());
            removeHighlight(getCopilotKaffeeControlGroup());
            addListenersToGroupFields(verpflichteteGroup, spieler);
        }
    }

    public SchalterGUI getSchalterGUI() {
        return schalterGUI;
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
     * @param spieler Der Spieler, dessen Listener entfernt werden sollen.
     */
    public void clearPlayerListeners(Spieler spieler) {
        ArrayList<ImageButton> wuerfelActors = (spieler instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        for (ImageButton button : wuerfelActors) {
            button.clearListeners();
        }

        ControlGroup group = (spieler instanceof Pilot)
            ? getControlGroupByName(ActionControlGroup.PILOT_CONTROLS)
            : getControlGroupByName(ActionControlGroup.COPILOT_CONTROLS);
        for (ControlField field : group.getAvailableFields()) {
            field.getButton().clearListeners();
            switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        }


        if (spieler instanceof Pilot) {
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
        ActionName[] pilotManyUseFields = {ActionName.PILOT_RADIO, ActionName.PILOT_NEIGUNG, ActionName.PILOT_SPEED};
        for (ActionName s : pilotManyUseFields) {
            ImageButton button = pilotGroup.getFieldByAction(s).getButton();
            switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
            pilotGroup.getFieldByAction(s).setOccupied(false);
        }
        ActionName[] copilotManyUseFields = {ActionName.COPILOT_NEIGUNG, ActionName.COPILOT_SPEED, ActionName.COPILOT_RADIO_1,  ActionName.COPILOT_RADIO_2};
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
        int kaffeeCount = spiel.getKaffeeTokenCount();
        ControlGroup pilotUseGroup = getPilotUseKaffeeControlGroup();
        if (!pilotAvailable) {
            for (ControlField field : pilotUseGroup.getAllFields()) {
                switchTexture(field.getButton(), TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                field.getButton().setTouchable(Touchable.disabled);
            }
        } else {
            for (int i = 1; i <= 3; i++) {
                ControlField field = pilotUseGroup.getFieldByAction(
                    ActionName.useKaffee(i)
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
                    ActionName.useKaffee(i)
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
     * Aktiviert die Verwendung eines Kaffee-Tokens für einen Spieler.
     *
     * @param s Der Spieler, der ein Kaffee-Token verwenden möchte.
     */
    public void useKaffee(Spieler s) {
        int kaffeeCount = spiel.getKaffeeTokenCount();
        ControlGroup useGroup = getUseKaffeeControlGroup(s);

        if (kaffeeCount > 0) {
            ControlField field = useGroup.getFieldByAction(
                ActionName.useKaffee(kaffeeCount)
            );
            if (field != null && field.isOccupied()) {
                field.getButton().clearListeners();
                field.getButton().setTouchable(Touchable.enabled);
                switchTexture(field.getButton(), TexturePaths.KAFFEE_AVAILABLE_PATH);
                field.getButton().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        switchTexture(field.getButton(), TexturePaths.KAFFEE_SELECTED_PATH);
                        spielLogik.createMessage("Kaffee-Token verwenden", "Bitte wählen Sie einen Würfel aus, dessen Wert Sie ändern möchten!");
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
     * Wartet auf die Auswahl eines Würfels durch den Spieler, um die Kaffee-Aktion auszuführen.
     *
     * @param spieler Der Spieler, der die Aktion durchführt.
     * @param name Der Name des Kaffee-Kontrollfeldes.
     */
    private void waitForDiceSelection(Spieler spieler, ActionName name) {
        ArrayList<ImageButton> wuerfelActors = (spieler instanceof Pilot)
            ? pilotWuerfelActors
            : copilotWuerfelActors;
        ArrayList<Wuerfel> wuerfels = spieler.getWuerfels();
        String path = (spieler instanceof Pilot)
            ? TexturePaths.WUERFEL_PATH_PILOT
            : TexturePaths.WUERFEL_PATH_COPILOT;

        for (int i = 0; i < wuerfelActors.size(); i++) {
            final ImageButton wuerfelButton = wuerfelActors.get(i);
            final Wuerfel wuerfel = wuerfels.get(i);
            final int index = i;
            final int wert = wuerfel.getWert();

            wuerfelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switchTexture(wuerfelButton, path + wert + TexturePaths.WUERFEL_PATH_SELECTED);
                    clearDiceListeners(spieler);
                    spielLogik.createKaffeeTokenWindow(wuerfel, spieler, index, (neuerWert) -> {
                        switchTexture(wuerfelButton, path + neuerWert + TexturePaths.IMG_FORMAT);
                        removeKaffeeTokenByName(name, spieler);
                        kaffeeFieldClearLastUsed(spieler);
                        checkFeldSituation(spieler);
                        addWuerfelListenersForSpieler(spieler);

                        if (spiel.getKaffeeTokenCount() > 0) {
                            useKaffee(spieler);
                        }
                    });
                }
            });
        }
    }

    /**
     * Entfernt die zuletzt verwendeten Kaffee-Kontrollfelder.
     *
     * @param s Der Spieler, dessen Kontrollfelder zurückgesetzt werden sollen.
     */
    private void kaffeeFieldClearLastUsed(Spieler s) {
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
     * Gibt ein Kaffee-Kontrollfeld und sein Spiegelbild für beide Spieler frei.
     *
     * @param field Das Kaffee-Kontrollfeld, das freigegeben werden soll.
     * @param s Der Spieler, für den das Kontrollfeld freigegeben wird.
     */
    private void freeKaffeeFieldForBoth(ControlField field, Spieler s) {
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
     * @param spieler Der Spieler, dessen Würfel zurückgesetzt werden sollen.
     */
    private void clearDiceListeners(Spieler spieler) {
        ArrayList<ImageButton> wuerfelActors = (spieler instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        for (ImageButton ib : wuerfelActors) {
            ib.clearListeners();
        }
    }

    /**
     * Entfernt ein verwendetes Kaffee-Token aus der Kontrollgruppe.
     *
     * @param name Der Name des verwendeten Kaffee-Kontrollfeldes.
     * @param spieler Der Spieler, dessen Kaffee-Token entfernt werden soll.
     */
    private void removeKaffeeTokenByName(ActionName name, Spieler spieler) {
        ControlGroup useGroup = getUseKaffeeControlGroup(spieler);
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
        int anzahlTokens = spiel.getNeuWurfTokenCount();
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
     * Aktiviert die Verwendung eines "Neu Wurf"-Tokens für den Spieler.
     *
     * @param spieler Der Spieler, der ein "Neu Wurf"-Token verwenden möchte.
     */
    public void useNeuWurf(Spieler spieler) {
        ImageButton button = neuWurf.getButton();
        button.clearListeners();
        if (spiel.getNeuWurfTokenCount() > 0) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switchTexture(button, TexturePaths.NEUWURF_SELECTED_PATH);
                    spielLogik.createNeuWurfTokenWindow(spieler, (p1, p2) -> {
                        if (p1 != null) {
                            createWuerfelForSpieler(p1);
                            addWuerfelListenersForSpieler(p1);
                        }
                        if (p2 != null) {
                            createWuerfelForSpieler(p2);
                            addWuerfelListenersForSpieler(p2);
                        }
                        if (spiel.getNeuWurfTokenCount() == 0) {
                            switchTexture(button, TexturePaths.WITHOUT_HIGHLIGHT_PATH);
                            button.setTouchable(Touchable.disabled);
                        } else {
                            button.setTouchable(Touchable.enabled);
                            switchTexture(button, TexturePaths.NEUWURF_AVAILABLE_PATH);
                        }
                        removeAllHideImages();
                        if( (spieler instanceof Pilot) ){
                            hideWuerfel(spieler);
                        } else {
                            hideWuerfel(spiel.getCopilot());
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

        if (schalterGUI != null) {
            schalterGUI.dispose();
            schalterGUI = null;
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


        spielLogik = null;
        skin = null;
        callback = null;
        szenarioHoeheManager = null;
    }

    /**
     * Blendet die Würfel des anderen Spielers aus, um sie zu verbergen.
     *
     * @param spieler Der aktuelle Spieler, dessen Würfel sichtbar bleiben sollen.
     */
    public void hideWuerfel(Spieler spieler) {
        if (spieler instanceof Pilot) {
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
}
