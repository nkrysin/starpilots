package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.teama.skyteam.drop.gui.buttons.ControlGroup;
import com.teama.skyteam.drop.spiel.Spiel;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;


public class SpielGUI {

    private final Spiel spiel;
    public final Stage stage;
    private final int[][] pilotDicesCoordinates = {{411, 553}, {491, 553}, {411, 473}, {491, 473}};
    private final int[][] copilotDicesCoordinates = {{1367, 553}, {1448, 553}, {1367, 473}, {1448, 473}};
    private final GUIConstructionMethods guiConstructionMethods;
    private ArrayList<ImageButton> pilotDiceActors = new ArrayList<>();
    private ArrayList<ImageButton> copilotDiceActors = new ArrayList<>();
    private ArrayList<ControlGroup> controlGroups = new ArrayList<ControlGroup>();
    //пилот: 411, 553 (55х55); 491, 553 (55х55); 411, 473 (55х55); 491,473 (55х55);
    //сопилот:  1367, 553 (55х55); 1448, 553 (55х55); 1367, 473 (55х55); 1367, 473 (55х55);


    public SpielGUI(Spiel spiel, Stage stage) {
        //конструктор должен вызываться один раз, потом при обнолвении кубиков/действий должны вызываться методы отсюда
        this.spiel = spiel;
        this.stage = stage; //это типа сцена, где размещаются актеры, которых мы позже добавим
        this.guiConstructionMethods = new GUIConstructionMethods();

        createFieldActors();


        createFieldActors();
        //TODO нужно написать лисенеры
    }

    public void createDiceForSpieler(Spieler spieler) {
        ArrayList<Wuerfel> wuerfels = spieler.getWuerfels();
        ArrayList<ImageButton> diceActors = new ArrayList<>();

        for (int i = 0; i < wuerfels.size(); i++) {
            Wuerfel w = wuerfels.get(i);
            int wert = w.getWert();

            String textureName;
            int x, y;

            if (spieler instanceof Pilot) {
                textureName = "images/spiel/dices/blue/blue_" + wert + ".png";
                x = pilotDicesCoordinates[i][0];
                y = pilotDicesCoordinates[i][1];
            } else {
                textureName = "images/spiel/dices/orange/orange_" + wert + ".png";
                x = copilotDicesCoordinates[i][0];
                y = copilotDicesCoordinates[i][1];
            }

            ImageButton diceButton = guiConstructionMethods.createDiceButton(textureName, x, y);
            diceActors.add(diceButton);
            stage.addActor(diceButton);
        }

        if (spieler instanceof Pilot) {
            pilotDiceActors = diceActors;
        } else {
            copilotDiceActors = diceActors;
        }
    }

    private void createFieldActors() {
        controlGroups.add(guiConstructionMethods.createControlGroup("Pilot Controls", new String[]{
            "Radio", "Speed", "Neigung", "Fahrgestell12", "Fahrgestell34", "Fahrgestell56", "Bremsen2", "Bremsen4", "Bremsen6"
        }, new int[][]{
            {679, 791}, {773, 777}, {838, 475}, {679, 558}, {679, 464}, {679, 327}, {842, 293}, {933, 293}, {1025, 293}
        }, stage));

        controlGroups.add(guiConstructionMethods.createControlGroup("Copilot Controls", new String[]{
            "Speed", "Neigung", "Radio1", "Radio2", "Klappen12", "Klappen23", "Klappen45", "Klappen56"
        }, new int[][]{
            {1091, 777}, {1027, 475}, {1185, 791}, {1185, 883}, {1185, 558}, {1185, 464}, {1185, 327}, {1185, 191}
        }, stage));

        controlGroups.add(guiConstructionMethods.createControlGroup("Koffe Controls", new String[]{
            "Koffe1", "Koffe2", "Koffe3"
        }, new int[][]{
            {842, 142}, {933, 142}, {1025, 142}
        }, stage));

        guiConstructionMethods.createSingleFieldButton("NeuWurf", 681, 873, stage);
        guiConstructionMethods.createSingleFieldButton("UseKoffe1", 656, 54, stage);
        guiConstructionMethods.createSingleFieldButton("UseKoffe2", 717, 54, stage);
        guiConstructionMethods.createSingleFieldButton("UseKoffe3", 779, 54, stage);
    }




}
