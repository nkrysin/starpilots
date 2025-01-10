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
import com.teama.skyteam.drop.gui.buttons.ControlField;
import com.teama.skyteam.drop.gui.buttons.ControlGroup;
import com.teama.skyteam.drop.gui.utilities.PlayerActionCallback;
import com.teama.skyteam.drop.gui.utilities.SelectedWuerfelData;
import com.teama.skyteam.drop.gui.utilities.TextureCache;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spiel.SpielLogik;
import com.teama.skyteam.drop.spieler.Feld;
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
    private final int[][] pilotWuerfelsCoordinates = {{411, 553}, {491, 553}, {411, 473}, {491, 473}};
    private final int[][] copilotWuerfelsCoordinates = {{1367, 553}, {1448, 553}, {1367, 473}, {1448, 473}};
    private final GUIConstructionMethods guiConstructionMethods;
    private ArrayList<ImageButton> pilotWuerfelActors = new ArrayList<>();
    private ArrayList<ImageButton> copilotWuerfelActors = new ArrayList<>();
    private ArrayList<ControlGroup> controlGroups = new ArrayList<>();
    private ControlGroup pilotKoffeeControlGroup;
    private ControlGroup copilotKoffeeControlGroup;
    private ControlGroup pilotUseKoffeeControlGroup;
    private ControlGroup copilotUseKoffeeControlGroup;
    private SelectedWuerfelData selectedWuerfelData = null;
    private SpielLogik spielLogik;
    private Skin skin;
    private SchalterGUI schalterGUI;
    private Image ruder;
    private PlayerActionCallback callback;
    private SzenarioHoeheManager szenarioHoeheManager;
    private ControlField neuWurf;

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

    private void setRuder() {
        Texture ruderTexture = new Texture("images/spiel/main/axis.png");
        ruder = new Image(ruderTexture);
        ruder.setPosition(868, 630);
        ruder.setOrigin(ruder.getWidth() / 2, ruder.getHeight() / 2 - 5);
        this.stage.addActor(ruder);
    }

    public Image getRuder() {
        return ruder;
    }

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
                textureName = "images/spiel/dices/blue/blue_" + wert + ".png";
                x = pilotWuerfelsCoordinates[i][0];
                y = pilotWuerfelsCoordinates[i][1];
            } else {
                textureName = "images/spiel/dices/orange/orange_" + wert + ".png";
                x = copilotWuerfelsCoordinates[i][0];
                y = copilotWuerfelsCoordinates[i][1];
            }
            ImageButton wuerfelButton = guiConstructionMethods.createButton(textureName, x, y, 55, 55);
            wuerfelActors.add(wuerfelButton);
            stage.addActor(wuerfelButton);
        }

        if (spieler instanceof Pilot) {
            pilotWuerfelActors = wuerfelActors;
        } else {
            copilotWuerfelActors = wuerfelActors;
        }
    }

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
                normalTextureName = "images/spiel/dices/blue/blue_" + wert + ".png";
                selectedTextureName = "images/spiel/dices/blue/blue_" + wert + "_selected.png";
            } else {
                normalTextureName = "images/spiel/dices/orange/orange_" + wert + ".png";
                selectedTextureName = "images/spiel/dices/orange/orange_" + wert + "_selected.png";
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

    private void switchTexture(ImageButton button, String newTexturePath) {
        Texture newTexture = TextureCache.get(newTexturePath);
        TextureRegionDrawable newDrawable = new TextureRegionDrawable(new TextureRegion(newTexture));
        button.getStyle().imageUp = newDrawable;
    }

    private void createFieldActors() {
        controlGroups.add(guiConstructionMethods.createControlGroup("Pilot Controls", new String[]{
            "PRadio", "PNeigung", "PSpeed", "PFahrgestell12", "PFahrgestell34", "PFahrgestell56", "PBremsen2", "PBremsen4", "PBremsen6"
        }, new int[][]{
            {679, 791}, {773, 777}, {838, 475}, {679, 598}, {679, 464}, {679, 327}, {842, 293}, {933, 293}, {1025, 293}
        }, stage, 55, 55));

        controlGroups.add(guiConstructionMethods.createControlGroup("Copilot Controls", new String[]{
            "CNeigung", "CSpeed", "CRadio1", "CRadio2", "CKlappen12", "CKlappen23", "CKlappen45", "CKlappen56"
        }, new int[][]{
            {1091, 777}, {1027, 475}, {1185, 791}, {1185, 883}, {1185, 598}, {1185, 464}, {1185, 327}, {1185, 191}
        }, stage, 55, 55));

        pilotKoffeeControlGroup = guiConstructionMethods.createControlGroup(
            "Pilot Koffe Controls",
            new String[]{"PilotKaffee1", "PilotKaffee2", "PilotKaffee3"},
            new int[][]{
                {842, 142},
                {933, 142},
                {1025, 142}
            },
            stage,
            55,
            55
        );
        copilotKoffeeControlGroup = guiConstructionMethods.createControlGroup(
            "Copilot Koffe Controls",
            new String[]{"CopilotKaffee1", "CopilotKaffee2", "CopilotKaffee3"},
            new int[][]{
                {842, 142},
                {933, 142},
                {1025, 142}
            },
            stage,
            55,
            55
        );

        controlGroups.add(pilotKoffeeControlGroup);
        controlGroups.add(copilotKoffeeControlGroup);

        pilotUseKoffeeControlGroup = guiConstructionMethods.createUseKoffeeButtons(
            "Pilot UseKoffeeControlGroup",
            new int[][]{{711, 201}, {711, 143}, {772, 143}},
            stage
        );
        copilotUseKoffeeControlGroup = guiConstructionMethods.createUseKoffeeButtons(
            "Copilot UseKoffeeControlGroup",
            new int[][]{{711, 201}, {711, 143}, {772, 143}},
            stage
        );

        controlGroups.add(pilotUseKoffeeControlGroup);
        controlGroups.add(copilotUseKoffeeControlGroup);



        this.neuWurf = guiConstructionMethods.createNeuWurfButton(stage);


        initMirrors();
    }

    public void setKoffeeGroupsActive(Spieler current) {
        boolean isPilot = current instanceof Pilot;
        for (ControlField cf : pilotKoffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.enabled : Touchable.disabled);
        }
        for (ControlField cf : pilotUseKoffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.enabled : Touchable.disabled);
        }
        for (ControlField cf : copilotKoffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.disabled : Touchable.enabled);
        }
        for (ControlField cf : copilotUseKoffeeControlGroup.getAllFields()) {
            cf.getButton().setTouchable(isPilot ? Touchable.disabled : Touchable.enabled);
        }
    }

    public void highlightAvailableFields(ControlGroup group) {
        for (ControlField field : group.getAvailableFields()) {
            ImageButton button = field.getButton();
            switchTexture(button, "images/spiel/main/available.png");
        }
    }

    public void removeHighlight(ControlGroup group) {
        for (ControlField field : group.getAvailableFields()) {
            ImageButton button = field.getButton();
            switchTexture(button, "images/spiel/main/empty.png");
        }
    }

    public void disableFields(ControlGroup group, ControlGroup pflicht) {
        List<ControlField> pflichtFields = pflicht.getAvailableFields();
        for (ControlField field : group.getAvailableFields()) {
            if (!pflichtFields.contains(field)) {
                field.getButton().setTouchable(Touchable.disabled);
            }
        }
    }

    private ControlGroup getMainControlGroup(Spieler spieler) {
        return (spieler instanceof Pilot)
            ? getControlGroupByName("Pilot Controls")
            : getControlGroupByName("Copilot Controls");
    }

    private ControlGroup getPilotKoffeeControlGroup() {
        return pilotKoffeeControlGroup;
    }

    private ControlGroup getCopilotKoffeeControlGroup() {
        return copilotKoffeeControlGroup;
    }

    private ControlGroup getPilotUseKoffeeControlGroup() {
        return pilotUseKoffeeControlGroup;
    }

    private ControlGroup getCopilotUseKoffeeControlGroup() {
        return copilotUseKoffeeControlGroup;
    }

    private ControlGroup getCoffeeControlGroup(Spieler spieler) {
        return (spieler instanceof Pilot) ? getPilotKoffeeControlGroup() : getCopilotKoffeeControlGroup();
    }

    private ControlGroup getUseCoffeeControlGroup(Spieler spieler) {
        return (spieler instanceof Pilot) ? getPilotUseKoffeeControlGroup() : getCopilotUseKoffeeControlGroup();
    }

    private ControlGroup getControlGroupByName(String name) {
        for (ControlGroup cg : controlGroups) {
            if (cg.getName().equals(name)) {
                return cg;
            }
        }
        return null;
    }

    public void addFieldListenersForPlacement(Spieler spieler) {
        ControlGroup mainGroup = getMainControlGroup(spieler);
        addListenersToGroupFields(mainGroup, spieler);
        addCoffeeListeners(spieler);
    }

    private void addCoffeeListeners(Spieler spieler) {
        ControlGroup koffeeGroup = getCoffeeControlGroup(spieler);
        highlightAvailableFields(koffeeGroup);
        for (ControlField field : koffeeGroup.getAvailableFields()) {
            field.getButton().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    handleCoffeeFieldClick(field, spieler);
                }
            });
        }

    }

    private Map<ControlField, ControlField> mirroredFields = new HashMap<>();

    public void initMirrors() {
        ControlField pilotK1 = pilotKoffeeControlGroup.getFieldByName("PilotKaffee1");
        ControlField copilotK1 = copilotKoffeeControlGroup.getFieldByName("CopilotKaffee1");

        mirroredFields.put(pilotK1, copilotK1);
        mirroredFields.put(copilotK1, pilotK1);

        ControlField pilotK2 = pilotKoffeeControlGroup.getFieldByName("PilotKaffee2");
        ControlField copilotK2 = copilotKoffeeControlGroup.getFieldByName("CopilotKaffee2");

        mirroredFields.put(pilotK2, copilotK2);
        mirroredFields.put(copilotK2, pilotK2);

        ControlField pilotK3 = pilotKoffeeControlGroup.getFieldByName("PilotKaffee3");
        ControlField copilotK3 = copilotKoffeeControlGroup.getFieldByName("CopilotKaffee3");

        mirroredFields.put(pilotK3, copilotK3);
        mirroredFields.put(copilotK3, pilotK3);
    }



    private void handleCoffeeFieldClick(ControlField field, Spieler spieler) {
        if (selectedWuerfelData == null || field.isOccupied()) {
            return;
        }

        boolean isPilot = (spieler instanceof Pilot);
        Wuerfel w = selectedWuerfelData.getWuerfel();
        int index = selectedWuerfelData.getWuerfelIndex();

        boolean stepSuccessful = isPilot
            ? spielLogik.pilotSchritt(spieler, field.getName(), w, index)
            : spielLogik.copilotSchritt(spieler, field.getName(), w, index);

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
        if (callback != null){
            callback.onPlayerActionCompleted(spieler);
        }

    }



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

    private void blockFieldForBoth(ControlField fieldPilot, ControlField fieldCopilot, SelectedWuerfelData data) {
        String texture = data.getDiceTexturePath();

        switchTexture(fieldPilot.getButton(), texture);
        fieldPilot.setOccupied(true);
        fieldPilot.getButton().setTouchable(Touchable.disabled);

        switchTexture(fieldCopilot.getButton(), texture);
        fieldCopilot.setOccupied(true);
        fieldCopilot.getButton().setTouchable(Touchable.disabled);
    }

    private void handleMainFieldClick(ControlField field, Spieler spieler) {
        if (selectedWuerfelData == null || field.isOccupied()) {
            return;
        }
        boolean isPilot = spieler instanceof Pilot;
        Wuerfel w = selectedWuerfelData.getWuerfel();
        int index = selectedWuerfelData.getWuerfelIndex();
        boolean stepSuccessful = isPilot
            ? spielLogik.pilotSchritt(spieler, field.getName(), w, index)
            : spielLogik.copilotSchritt(spieler, field.getName(), w, index);
        if (!stepSuccessful) {
            return;
        }
        String texturePath = isPilot
            ? "images/spiel/dices/blue/blue_" + w.getWert() + ".png"
            : "images/spiel/dices/orange/orange_" + w.getWert() + ".png";
        removeWuerfelButtonFromHand(selectedWuerfelData);
        switchTexture(field.getButton(), texturePath);
        field.setOccupied(true);
        selectedWuerfelData = null;
        if (callback != null) {
            callback.onPlayerActionCompleted(spieler);
        }
    }

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

    public void checkFeldSituation(Spieler spieler) {
        System.out.println();
        Feld feldNeigung;
        Feld feldSpeed;
        int anzahlWuerfels;
        if (spieler instanceof Pilot) {
            feldNeigung = spiel.getFeldPilot("neigenFeld");
            feldSpeed = spiel.getFeldPilot("speedFeld");
            anzahlWuerfels = spiel.getPilot().getAnzahlWuerfels();
            spieler = spiel.getPilot();
        } else {
            feldNeigung = spiel.getFeldCopilot("neigenFeld");
            feldSpeed = spiel.getFeldCopilot("speedFeld");
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

    void verpflicteteAktion(Spieler spieler) {
        Feld feldNeigung;
        Feld feldSpeed;
        ControlField neigungField;
        ControlField speedField;
        ControlGroup verpflichteteGroup;
        ControlGroup allFields;
        ControlGroup coffeeFields;
        if (spieler instanceof Pilot) {
            feldNeigung = spiel.getFeldPilot("neigenFeld");
            feldSpeed = spiel.getFeldPilot("speedFeld");
            neigungField = getControlGroupByName("Pilot Controls").getFieldByName("PNeigung");
            speedField = getControlGroupByName("Pilot Controls").getFieldByName("PSpeed");
            allFields = getControlGroupByName("Pilot Controls");
            coffeeFields = pilotKoffeeControlGroup;
            spieler = spiel.getPilot();
        } else {
            feldNeigung = spiel.getFeldCopilot("neigenFeld");
            feldSpeed = spiel.getFeldCopilot("speedFeld");
            neigungField = getControlGroupByName("Copilot Controls").getFieldByName("CNeigung");
            speedField = getControlGroupByName("Copilot Controls").getFieldByName("CSpeed");
            allFields = getControlGroupByName("Copilot Controls");
            coffeeFields = copilotKoffeeControlGroup;
            spieler = spiel.getCopilot();
        }
        if (!feldSpeed.isBelegt() && !feldNeigung.isBelegt()) {
            spielLogik.createMessage("Achtung!", "Bitte setzen Sie die Würfel auf die Pflichtfelder");
            verpflichteteGroup = new ControlGroup("Verplichtete", List.of(neigungField, speedField));
            removeHighlight(allFields);
            removeHighlight(getPilotKoffeeControlGroup());
            removeHighlight(getCopilotKoffeeControlGroup());
            disableFields(allFields, verpflichteteGroup);
            disableFields(coffeeFields, verpflichteteGroup);
            addListenersToGroupFields(verpflichteteGroup, spieler);
        } else if (!feldSpeed.isBelegt()) {
            spielLogik.createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            verpflichteteGroup = new ControlGroup("Verplichtete", List.of(speedField));
            removeHighlight(allFields);
            removeHighlight(getPilotKoffeeControlGroup());
            removeHighlight(getCopilotKoffeeControlGroup());
            addListenersToGroupFields(verpflichteteGroup, spieler);
        } else {
            spielLogik.createMessage("Achtung!", "Bitte setzen Sie den Würfel auf das Pflichtfeld");
            verpflichteteGroup = new ControlGroup("Verplichtete", List.of(neigungField));
            removeHighlight(allFields);
            removeHighlight(getPilotKoffeeControlGroup());
            removeHighlight(getCopilotKoffeeControlGroup());
            addListenersToGroupFields(verpflichteteGroup, spieler);
        }
    }

    public SchalterGUI getSchalterGUI() {
        return schalterGUI;
    }

    public void setPlayerActionCallback(PlayerActionCallback callback) {
        this.callback = callback;
    }


    public void clearPlayerListeners(Spieler spieler) {
        ArrayList<ImageButton> wuerfelActors = (spieler instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        for (ImageButton button : wuerfelActors) {
            button.clearListeners();
        }

        ControlGroup group = (spieler instanceof Pilot)
            ? getControlGroupByName("Pilot Controls")
            : getControlGroupByName("Copilot Controls");
        for (ControlField field : group.getAvailableFields()) {
            field.getButton().clearListeners();
            switchTexture(field.getButton(), "images/spiel/main/empty.png");
        }


        if (spieler instanceof Pilot) {
            for (ControlField field : getPilotKoffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
            }
            for (ControlField field : getPilotUseKoffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
                switchTexture(field.getButton(), "images/spiel/main/empty.png");
            }
        } else {
            for (ControlField field : getCopilotKoffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
                //switchTexture(field.getButton(), "images/spiel/main/empty.png");
            }
            for (ControlField field : getCopilotUseKoffeeControlGroup().getAllFields()) {
                field.getButton().clearListeners();
                switchTexture(field.getButton(), "images/spiel/main/empty.png");
            }
        }


    }

    public void clearManyUseFields() {
        ControlGroup pilotGroup = getControlGroupByName("Pilot Controls");
        ControlGroup copilotGroup = getControlGroupByName("Copilot Controls");
        String[] pilotManyUseFields = {"PRadio", "PNeigung", "PSpeed"};
        for (String s : pilotManyUseFields) {
            ImageButton button = pilotGroup.getFieldByName(s).getButton();
            switchTexture(button, "images/spiel/main/empty.png");
            pilotGroup.getFieldByName(s).setOccupied(false);
        }
        String[] copilotManyUseFields = {"CNeigung", "CSpeed", "CRadio1", "CRadio2"};
        for (String s : copilotManyUseFields) {
            ImageButton button = copilotGroup.getFieldByName(s).getButton();
            switchTexture(button, "images/spiel/main/empty.png");
            copilotGroup.getFieldByName(s).setOccupied(false);
        }
        showNeuWurf(false);
        showCoffee(false, false);
    }

    public void showCoffee(Boolean pilotAvailable, Boolean copilotAvailable) {
        int coffeeCount = spiel.getKaffeTokenCount();
        ControlGroup pilotUseGroup = getPilotUseKoffeeControlGroup();
        if (!pilotAvailable) {
            for (ControlField field : pilotUseGroup.getAllFields()) {
                switchTexture(field.getButton(), "images/spiel/main/empty.png");
                field.getButton().setTouchable(Touchable.disabled);
            }
        } else {
            for (int i = 1; i <= 3; i++) {
                ControlField field = pilotUseGroup.getFieldByName("UseKoffe" + i);
                if (field != null) {
                    if (i <= coffeeCount) {
                        field.setOccupied(true);
                        field.getButton().setTouchable(Touchable.enabled);
                        switchTexture(field.getButton(), "images/spiel/tokens/kaffee.png");
                    } else {
                        field.setOccupied(false);
                        switchTexture(field.getButton(), "images/spiel/main/empty.png");
                        field.getButton().setTouchable(Touchable.disabled);
                    }
                }
            }
        }

        ControlGroup copilotUseGroup = getCopilotUseKoffeeControlGroup();
        if (!copilotAvailable) {
            for (ControlField field : copilotUseGroup.getAllFields()) {
                switchTexture(field.getButton(), "images/spiel/main/empty.png");
                field.getButton().setTouchable(Touchable.disabled);
            }
        } else {
            for (int i = 1; i <= 3; i++) {
                ControlField field = copilotUseGroup.getFieldByName("UseKoffe" + i);
                if (field != null) {
                    if (i <= coffeeCount) {
                        field.setOccupied(true);
                        field.getButton().setTouchable(Touchable.enabled);
                        switchTexture(field.getButton(), "images/spiel/tokens/kaffee.png");
                    } else {
                        field.setOccupied(false);
                        switchTexture(field.getButton(), "images/spiel/main/empty.png");
                        field.getButton().setTouchable(Touchable.disabled);
                    }
                }
            }
        }
    }


    public void useCoffee(Spieler s) {
        int coffeeCount = spiel.getKaffeTokenCount();
        ControlGroup useGroup = getUseCoffeeControlGroup(s);

        if (coffeeCount > 0) {
            ControlField field = useGroup.getFieldByName("UseKoffe" + coffeeCount);
            if (field != null && field.isOccupied()) {
                field.getButton().clearListeners();
                field.getButton().setTouchable(Touchable.enabled);
                switchTexture(field.getButton(), "images/spiel/tokens/kaffee_available.png");
                field.getButton().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        switchTexture(field.getButton(), "images/spiel/tokens/kaffee_selected.png");
                        spielLogik.createMessage("Kaffee-Token verwenden", "Bitte wählen Sie einen Würfel aus, dessen Wert Sie ändern möchten!");
                        waitForDiceSelection(s, field.getName());
                    }
                });
            }
        } else {
            for (ControlField f : useGroup.getAllFields()) {
                f.getButton().clearListeners();
                switchTexture(f.getButton(), "images/spiel/main/empty.png");
                f.setOccupied(false);
            }
        }
    }

    private void waitForDiceSelection(Spieler spieler, String name) {
        ArrayList<ImageButton> wuerfelActors = (spieler instanceof Pilot)
            ? pilotWuerfelActors
            : copilotWuerfelActors;
        ArrayList<Wuerfel> wuerfels = spieler.getWuerfels();
        String path = (spieler instanceof Pilot)
            ? "images/spiel/dices/blue/blue_"
            : "images/spiel/dices/orange/orange_";

        for (int i = 0; i < wuerfelActors.size(); i++) {
            final ImageButton wuerfelButton = wuerfelActors.get(i);
            final Wuerfel wuerfel = wuerfels.get(i);
            final int index = i;
            final int wert = wuerfel.getWert();

            wuerfelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switchTexture(wuerfelButton, path + wert + "_selected.png");
                    clearDiceListeners(spieler);
                    spielLogik.createKaffeeTokenWindow(wuerfel, spieler, index, (neuerWert) -> {
                        switchTexture(wuerfelButton, path + neuerWert + ".png");
                        removeCoffeeTokenByName(name, spieler);
                        koffeFieldClearLastUsed(spieler);
                        checkFeldSituation(spieler);
                        addWuerfelListenersForSpieler(spieler);

                        if (spiel.getKaffeTokenCount() > 0) {
                            useCoffee(spieler);
                        }
                    });
                }
            });
        }
    }



    private void koffeFieldClearLastUsed(Spieler s) {
        List<ControlField> pilotFields = pilotKoffeeControlGroup.getAllFields();
        List<ControlField> copilotFields = copilotKoffeeControlGroup.getAllFields();

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



    private void freeCoffeeFieldForBoth(ControlField field, Spieler s) {
        ControlField mirrored = mirroredFields.get(field);

        if (s instanceof Pilot) {
            field.setOccupied(false);
            switchTexture(field.getButton(), "images/spiel/main/available.png");
            mirrored.setOccupied(false);
            switchTexture(mirrored.getButton(), "images/spiel/main/available.png");
            mirrored.getButton().setTouchable(Touchable.enabled);
            addCoffeeListeners(s);

        }

        else {
            field.setOccupied(false);
            switchTexture(field.getButton(), "images/spiel/main/available.png");
            mirrored.setOccupied(false);
            switchTexture(mirrored.getButton(), "images/spiel/main/available.png");
            field.getButton().setTouchable(Touchable.enabled);
            addCoffeeListeners(s);
        }

    }


    private void clearDiceListeners(Spieler spieler) {
        ArrayList<ImageButton> wuerfelActors = (spieler instanceof Pilot) ? pilotWuerfelActors : copilotWuerfelActors;
        for (ImageButton ib : wuerfelActors) {
            ib.clearListeners();
        }
    }

    private void removeCoffeeTokenByName(String name, Spieler spieler) {
        ControlGroup useGroup = getUseCoffeeControlGroup(spieler);
        ControlField usedField = useGroup.getFieldByName(name);
        if (usedField != null) {
            usedField.setOccupied(false);
            switchTexture(usedField.getButton(), "images/spiel/main/empty.png");
            usedField.getButton().setTouchable(Touchable.disabled);
        }
    }

    public void showNeuWurf(Boolean available) {
        int anzahlTokens = spiel.getNeuWurfTokenCount();
        ImageButton button = neuWurf.getButton();
        if (anzahlTokens == 0) {
            switchTexture(button, "images/spiel/main/empty.png");
            button.setTouchable(Touchable.disabled);
        } else {
            if (available) {
                button.setTouchable(Touchable.enabled);
                switchTexture(button, "images/spiel/tokens/neuwurf_available.png");
            } else {
                switchTexture(button, "images/spiel/tokens/neuwurf.png");
            }
        }
    }

    public void useNeuWurf(Spieler spieler) {
        ImageButton button = neuWurf.getButton();
        button.clearListeners();
        if (spiel.getNeuWurfTokenCount() > 0) {
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    switchTexture(button, "images/spiel/tokens/neuwurf_selected.png");
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
                            switchTexture(button, "images/spiel/main/empty.png");
                            button.setTouchable(Touchable.disabled);
                        } else {
                            button.setTouchable(Touchable.enabled);
                            switchTexture(button, "images/spiel/tokens/neuwurf_available.png");
                        }
                    });
                }
            });
        }
    }


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

        if (pilotKoffeeControlGroup != null) {
            pilotKoffeeControlGroup.dispose();
            pilotKoffeeControlGroup = null;
        }
        if (copilotKoffeeControlGroup != null) {
            copilotKoffeeControlGroup.dispose();
            copilotKoffeeControlGroup = null;
        }
        if (pilotUseKoffeeControlGroup != null) {
            pilotUseKoffeeControlGroup.dispose();
            pilotUseKoffeeControlGroup = null;
        }
        if (copilotUseKoffeeControlGroup != null) {
            copilotUseKoffeeControlGroup.dispose();
            copilotUseKoffeeControlGroup = null;
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



    public void hideWuerfel(Spieler spieler) {
        float pilotX = 330;
        float pilotY = 390;

        float copilotX = 1290;
        float copilotY = 390;

        if (spieler instanceof Pilot) {
            renderHideImage(copilotX, copilotY);
            removeHideImage(pilotX, pilotY);
        }
        else {
            renderHideImage(pilotX, pilotY);
            removeHideImage(copilotX, copilotY);
        }
    }

    private void renderHideImage(float x, float y) {
        Image hideWuerfelImage = new Image(TextureCache.get("images/spiel/dices/dice_cover.png"));
        hideWuerfelImage.setPosition(x, y);
        stage.addActor(hideWuerfelImage);
    }

    private void removeHideImage(float x, float y) {
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Image && actor.getX() == x && actor.getY() == y) {
                actor.remove();
                break;
            }
        }
    }
    public void removeAllHideImages() {
        float pilotX = 330;
        float pilotY = 390;

        float copilotX = 1290;
        float copilotY = 390;

        removeHideImage(pilotX, pilotY);
        removeHideImage(copilotX, copilotY);
    }

}
