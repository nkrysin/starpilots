package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import java.util.List;


public class SpielGUI {

    public final Stage stage;
    private final Spiel spiel;
    private final int[][] pilotWuerfelsCoordinates = {{411, 553}, {491, 553}, {411, 473}, {491, 473}};
    private final int[][] copilotWuerfelsCoordinates = {{1367, 553}, {1448, 553}, {1367, 473}, {1448, 473}};
    private final GUIConstructionMethods guiConstructionMethods;
    private ArrayList<ImageButton> pilotWuerfelActors = new ArrayList<>();
    private ArrayList<ImageButton> copilotWuerfelActors = new ArrayList<>();
    private ArrayList<ControlGroup> controlGroups = new ArrayList<ControlGroup>();
    private ControlGroup useKoffeeControlGroup;
    //пилот: 411, 553 (55х55); 491, 553 (55х55); 411, 473 (55х55); 491,473 (55х55);
    //сопилот:  1367, 553 (55х55); 1448, 553 (55х55); 1367, 473 (55х55); 1367, 473 (55х55);
    private SelectedWuerfelData selectedWuerfelData = null;
    private SpielLogik spielLogik;
    private Skin skin;
    private SchalterGUI schalterGUI;
    private Image ruder = new Image(new Texture("images/spiel/main/axis 2.png"));
    private PlayerActionCallback callback;
    private GameUIManager gameUIManager;
    private ControlField neuWurf;

    public SpielGUI(Spiel spiel, Stage stage, Skin skin, GameUIManager gameUIManager) {
        //конструктор должен вызываться один раз, потом при обнолвении кубиков/действий должны вызываться методы отсюда
        this.spiel = spiel;
        this.stage = stage; //это типа сцена, где размещаются актеры, которых мы позже добавим
        this.skin = skin;
        setRuder();
        this.guiConstructionMethods = new GUIConstructionMethods();
        schalterGUI = new SchalterGUI(stage);
        schalterGUI.addToStage();
        this.gameUIManager = gameUIManager;
        spielLogik = new SpielLogik(spiel, stage, skin, schalterGUI, ruder, gameUIManager);
        createFieldActors();
    }

    private void setRuder() {
        ruder.setPosition(868, 630);
        ruder.setOrigin(ruder.getWidth() / 2, ruder.getHeight() / 2 - 5);
        this.stage.addActor(ruder);
    }

    public Image getRuder() {
        return ruder;
    }

    public void createWuerfelForSpieler(Spieler spieler) {
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
        ArrayList<ImageButton> wuerfelActors;
        if (spieler instanceof Pilot) {
            wuerfelActors = pilotWuerfelActors;
        } else {
            wuerfelActors = copilotWuerfelActors;
        }
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

        controlGroups.add(guiConstructionMethods.createControlGroup("Koffe Controls", new String[]{
            "Kaffee1", "Kaffee2", "Kaffee3"
        }, new int[][]{
            {842, 142}, {933, 142}, {1025, 142}
        }, stage, 55, 55));

        this.neuWurf = guiConstructionMethods.createNeuWurfButton(stage);
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


    public void addFieldListenersForPlacement(Spieler spieler) {
        final boolean isPilot = (spieler instanceof Pilot);
        ControlGroup group;
        if (isPilot) {
            group = getControlGroupByName("Pilot Controls");
        } else {
            group = getControlGroupByName("Copilot Controls");
        }
        addListenersToGroupFields(getControlGroupByName("Koffe Controls"), spieler);
        //TODO NeuWurf
        addListenersToGroupFields(group, spieler);
    }


    private ControlGroup getControlGroupByName(String name) {
        for (ControlGroup group : controlGroups) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    private void addListenersToGroupFields(ControlGroup group, Spieler spieler) {
        highlightAvailableFields(group);
        for (ControlField field : group.getAvailableFields()) {
            field.getButton().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (selectedWuerfelData == null) {
                        return;
                    }
                    if (field.isOccupied()) {
                        return;
                    }


                    final String textureName;
                    final boolean isPilot = (spieler instanceof Pilot);
                    Wuerfel w = selectedWuerfelData.getWuerfel();
                    int index = selectedWuerfelData.getWuerfelIndex();

                    boolean stepSuccessful = false;
                    if (isPilot) {
                        stepSuccessful = spielLogik.pilotSchritt(spieler, field.getName(), w, index);
                        if (stepSuccessful) {
                            textureName = "images/spiel/dices/blue/blue_" + w.getWert() + ".png";
                        } else {
                            return;
                        }
                    } else {
                        stepSuccessful = spielLogik.copilotSchritt(spieler, field.getName(), w, index);
                        if (stepSuccessful) {
                            textureName = "images/spiel/dices/orange/orange_" + w.getWert() + ".png";
                        } else {
                            return;
                        }
                    }

                    if (stepSuccessful) {
                        removeWuerfelButtonFromHand(selectedWuerfelData);
                        switchTexture(field.getButton(), textureName);
                        field.setOccupied(true);
                        selectedWuerfelData = null;

                        // уведомляем контроллер о завершении действия
                        if (callback != null) {
                            callback.onPlayerActionCompleted(spieler);
                        }
                    }

                }
            });
        }
    }

    private void removeWuerfelButtonFromHand(SelectedWuerfelData wuerfelData) {
        Spieler owner = wuerfelData.getSpieler();
        int wuerfelIndex = wuerfelData.getWuerfelIndex();

        ArrayList<ImageButton> wuerfelActors;
        if (owner instanceof Pilot) {
            wuerfelActors = pilotWuerfelActors;
        } else {
            wuerfelActors = copilotWuerfelActors;
        }

        ImageButton wuerfelButton = wuerfelActors.get(wuerfelIndex);
        wuerfelButton.remove();
        wuerfelActors.remove(wuerfelIndex);
    }

    public void checkFeldSituation(Spieler spieler) {
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
        ControlGroup verplichteteGroup;
        ControlGroup allFields;
        if (spieler instanceof Pilot) {
            feldNeigung = spiel.getFeldPilot("neigenFeld");
            feldSpeed = spiel.getFeldPilot("speedFeld");
            neigungField = getControlGroupByName("Pilot Controls").getFieldByName("PNeigung");
            speedField = getControlGroupByName("Pilot Controls").getFieldByName("PSpeed");
            allFields = getControlGroupByName("Pilot Controls");
            spieler = spiel.getPilot();
        } else {
            feldNeigung = spiel.getFeldCopilot("neigenFeld");
            feldSpeed = spiel.getFeldCopilot("speedFeld");
            neigungField = getControlGroupByName("Copilot Controls").getFieldByName("CNeigung");
            speedField = getControlGroupByName("Copilot Controls").getFieldByName("CSpeed");
            allFields = getControlGroupByName("Copilot Controls");
            spieler = spiel.getCopilot();
        }
        if (!(feldSpeed.isBelegt()) && !(feldNeigung.isBelegt())) {
            //spielLogik.createMessage("Achtung!","Bitte setzen Sie die Würfel auf die Pflichtfelder" );
            verplichteteGroup = new ControlGroup("Verplichtete", List.of(neigungField, speedField));
            removeHighlight(getControlGroupByName("Pilot Controls"));
            disableFields(allFields, verplichteteGroup);
            addListenersToGroupFields(verplichteteGroup, spieler);

        } else if (!(feldSpeed.isBelegt())) {
            //spielLogik.createMessage("Achtung!","Bitte setzen Sie die Würfel auf die Pflichtfelde\n Sie können nur Geschwindigkeit wählen ");
            verplichteteGroup = new ControlGroup("Verplichtete", List.of(speedField));
            removeHighlight(getControlGroupByName("Pilot Controls"));
            addListenersToGroupFields(verplichteteGroup, spieler);
        } else {
            //spielLogik.createMessage("Achtung!","Bitte setzen Sie die Würfel auf die Pflichtfelde\n Sie können nur Neigen wählen ");
            verplichteteGroup = new ControlGroup("Verplichtete", List.of(neigungField));
            removeHighlight(getControlGroupByName("Pilot Controls"));
            addListenersToGroupFields(verplichteteGroup, spieler);
        }
    }

    public SchalterGUI getSchalterGUI() {
        return schalterGUI;
    }

    public void setPlayerActionCallback(PlayerActionCallback callback) {
        this.callback = callback;
    }

    public void clearPlayerListeners(Spieler spieler) {
        ArrayList<ImageButton> wuerfelActors;
        if (spieler instanceof Pilot) {
            wuerfelActors = pilotWuerfelActors;
        } else {
            wuerfelActors = copilotWuerfelActors;
        }

        for (ImageButton button : wuerfelActors) {
            button.clearListeners();
        }

        ControlGroup group;
        if (spieler instanceof Pilot) {
            group = getControlGroupByName("Pilot Controls");
        } else {
            group = getControlGroupByName("Copilot Controls");
        }

        for (ControlField field : group.getAvailableFields()) {
            field.getButton().clearListeners();
            switchTexture(field.getButton(), "images/spiel/main/empty.png");
        }

        List<ControlField> coffeeGroup = getControlGroupByName("Koffe Controls").getAvailableFields();
        if (!coffeeGroup.isEmpty()) {
            for (ControlField field : coffeeGroup) {
                field.getButton().clearListeners();
                switchTexture(field.getButton(), "images/spiel/main/empty.png");
            }
        }
        //TODO NeuWurf

        //TODO сюда же надо вписать кнопки потом использования всяких токенов и прочего, после хода игрока, они должны чиститься
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
        showCoffee(false);
    }

    public void showCoffee(Boolean available) {
        List<ControlField> coffeeGroup = getControlGroupByName("Koffe Controls").getAvailableFields();

        int coffeesToShow = 3 - coffeeGroup.size();

        useKoffeeControlGroup = guiConstructionMethods.createUseKoffeeButtons("useKoffeeControlGroup", stage, coffeesToShow);

        for (int i = 1; i <= coffeesToShow; i++) {
            ControlField field = useKoffeeControlGroup.getFieldByName("UseKoffe" + i);
            if (field != null) {
                if(available){
                    switchTexture(field.getButton(), "images/spiel/tokens/kaffee_available.png");
                } else {
                    switchTexture(field.getButton(), "images/spiel/tokens/kaffee.png");
                }

            }
        }
    }



    public void useCoffee(){

    }


    public void showNeuWurf(Boolean available){
        int anzahlTokens = spiel.getNeuWurfTokenCount();
        ImageButton button = neuWurf.getButton();
        if (anzahlTokens == 0){
            switchTexture(button, "images/spiel/main/empty.png");
        } else {
            if(available){
                switchTexture(button, "images/spiel/tokens/neuwurf_available.png");
            }
            else{
                switchTexture(button, "images/spiel/tokens/neuwurf.png");
            }

        }
    }



}


