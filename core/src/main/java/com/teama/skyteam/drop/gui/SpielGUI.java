package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teama.skyteam.drop.gui.buttons.ControlField;
import com.teama.skyteam.drop.gui.buttons.ControlGroup;
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
    private ArrayList<ControlGroup> useKoffeeControlGroup = new ArrayList<ControlGroup>();
    //пилот: 411, 553 (55х55); 491, 553 (55х55); 411, 473 (55х55); 491,473 (55х55);
    //сопилот:  1367, 553 (55х55); 1448, 553 (55х55); 1367, 473 (55х55); 1367, 473 (55х55);
    private SelectedWuerfelData selectedWuerfelData = null;
    private  SpielLogik spielLogik;
    private Skin skin;
    private SchalterGUI schalterGUI;

    public SpielGUI(Spiel spiel, Stage stage, Skin skin) {
        //конструктор должен вызываться один раз, потом при обнолвении кубиков/действий должны вызываться методы отсюда
        this.spiel = spiel;
        this.stage = stage; //это типа сцена, где размещаются актеры, которых мы позже добавим
        this.skin = skin;
        this.guiConstructionMethods = new GUIConstructionMethods();
        schalterGUI = new SchalterGUI(stage);
        schalterGUI.addToStage();
        spielLogik = new SpielLogik(spiel, stage, skin, schalterGUI);
        createFieldActors();
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

            ImageButton wuerfelButton = guiConstructionMethods.createButton(textureName, x, y);
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
        }, stage));

        controlGroups.add(guiConstructionMethods.createControlGroup("Copilot Controls", new String[]{
            "CNeigung", "CSpeed", "CRadio1", "CRadio2", "CKlappen12", "CKlappen23", "CKlappen45", "CKlappen56"
        }, new int[][]{
            {1091, 777}, {1027, 475}, {1185, 791}, {1185, 883}, {1185, 598}, {1185, 464}, {1185, 327}, {1185, 191}
        }, stage));

        controlGroups.add(guiConstructionMethods.createControlGroup("Koffe Controls", new String[]{
            "Kaffee1", "Kaffee2", "Kaffee3"
        }, new int[][]{
            {842, 142}, {933, 142}, {1025, 142}
        }, stage));

        useKoffeeControlGroup.add(guiConstructionMethods.createKoffeeButtons("useKoffeeControlGroup", stage));
    }


    public void highlightAvailableFields(ControlGroup group) {
        for (ControlField field : group.getAvailableFields()) {
            ImageButton button = field.getButton();
            switchTexture(button, "images/spiel/main/available.png");
        }
    }

    public void removeHighlight(ControlGroup group){
        for (ControlField field : group.getAvailableFields()) {
            ImageButton button = field.getButton();
            switchTexture(button, "images/spiel/main/empty.png");
        }
    }

    public void disableFields(ControlGroup group, ControlGroup pflicht){
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
                    Wuerfel w  = selectedWuerfelData.getWuerfel();
                    int index = selectedWuerfelData.getWuerfelIndex();

                    if (isPilot){
                        if (spielLogik.pilotSchritt(spieler, field.getName(), w,index)){
                            removeWuerfelButtonFromHand(selectedWuerfelData);
                            textureName = "images/spiel/dices/blue/blue_" + selectedWuerfelData.getWuerfel().getWert() + ".png";
                            switchTexture(field.getButton(), textureName);

                            field.setOccupied(true);
                            selectedWuerfelData = null;
                        }
                       else{
                           return;
                       }
                    }

                    else{
                        if(spielLogik.copilotSchritt(spieler, field.getName(), w,index)){
                            removeWuerfelButtonFromHand(selectedWuerfelData);
                            textureName = "images/spiel/dices/orange/orange_" + selectedWuerfelData.getWuerfel().getWert() + ".png";
                            switchTexture(field.getButton(), textureName);

                            field.setOccupied(true);
                            selectedWuerfelData = null;
                        }
                        else {
                            return;
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

    public void checkFeldSituation(Spieler spieler){
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
        if(!feldNeigung.isBelegt() && !feldSpeed.isBelegt() && anzahlWuerfels == 2){
            verpflicteteAktion(spieler);
        }
        else if(!feldNeigung.isBelegt() && anzahlWuerfels == 1) {
            verpflicteteAktion(spieler);
        }
        else if(!feldSpeed.isBelegt() && anzahlWuerfels == 1) {
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

        } else if (!(feldSpeed.isBelegt())){
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

    public SchalterGUI getSchalterGUI(){
        return schalterGUI;
    }
}
