package com.teama.skyteam.drop.spiel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Spieler;

import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;

public class SpielGUI {

    private final Spiel spiel;
    private final SpielLogik spielLogik;
    private final Stage stage;


    private ArrayList<ImageButton> pilotDiceActors = new ArrayList<>();
    private ArrayList<ImageButton> copilotDiceActors = new ArrayList<>();

    private Button pilotSpeedField;
    private Button pilotNeigungField;
    private Button pilotRadioField;
    private Button pilotFahrgestellField12;
    private Button pilotFahrgestellField34;
    private Button pilotFahrgestellField56;
    private Button pilotBremsenField2;
    private Button pilotBremsenField4;
    private Button pilotBremsenField6;

    private Button copilotSpeedField;
    private Button copilotNeigungField;
    private Button copilotRadioField1;
    private Button copilotRadioField2;
    private Button pilotKlappenField12;
    private Button pilotKlappenField23;
    private Button pilotKlappenField45;
    private Button pilotKlappenField56;

    private Button koffe1;
    private Button koffe2;
    private Button koffe3;

    private Button useNeuWurf;
    private Button useKoffe1;
    private Button useKoffe2;
    private Button useKoffe3;


    private final int[][] pilotDicesCoordinates = {{411, 553}, {491, 553}, {411, 473}, {491, 473}};
    private final int[][] copilotDicesCoordinates = {{1367, 553}, {1448, 553}, {1367, 473}, {1448, 473}};
    //пилот: 411, 553 (55х55); 491, 553 (55х55); 411, 473 (55х55); 491,473 (55х55);
    //сопилот:  1367, 553 (55х55); 1448, 553 (55х55); 1367, 473 (55х55); 1367, 473 (55х55);


    public SpielGUI(Spiel spiel, SpielLogik spielLogik, Stage stage, Skin skin) {
        this.spiel = spiel;
        this.spielLogik = spielLogik;
        this.stage = stage; //это типа сцена, где размещаются актеры, которых мы позже добавим


        createDiceForSpieler(spiel.getPilot());
        createDiceForSpieler(spiel.getCopilot());

        createFieldActors();
        //TODO нужно написать лисенеры
    }

    private void createDiceForSpieler(Spieler spieler) {
        ArrayList<Wuerfel> wuerfels = spieler.getWuerfels();
        ArrayList<ImageButton> diceActors = new ArrayList<>();

        for (int i = 0; i < wuerfels.size(); i++) {
            Wuerfel w = wuerfels.get(i);
            int wert = w.getWert();

            String textureName;
            int x, y;

            if (spieler instanceof Pilot) {
                textureName = "spiel/dices/blue/blue_" + wert + ".png";
                x = pilotDicesCoordinates[i][0];
                y = pilotDicesCoordinates[i][1];
            } else {
                textureName = "spiel/dices/orange/orange_" + wert + ".png";
                x = copilotDicesCoordinates[i][0];
                y = copilotDicesCoordinates[i][1];
            }

            ImageButton diceButton = createDiceButton(textureName, x, y);
            diceActors.add(diceButton);
            stage.addActor(diceButton);
        }

        if (spieler instanceof Pilot) {
            pilotDiceActors = diceActors;
        } else {
            copilotDiceActors = diceActors;
        }
    }

    private ImageButton createDiceButton(String textureName, int x, int y) {
        Texture tex = new Texture(textureName);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new com.badlogic.gdx.graphics.g2d.TextureRegion(tex));
        ImageButton diceButton = new ImageButton(style);
        diceButton.setBounds(x, y, 55, 55);
        return diceButton;
    }

    private Button createFieldButton(int x, int y) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = null;
        style.down = null;
        style.over = null;

        Button button = new Button(style);

        button.setPosition(x, y);
        button.setSize(60, 60);

        return button;
    }

    private void createFieldActors() {
        //создает кнопки
        pilotRadioField = createFieldButton(679, 791);
        pilotSpeedField = createFieldButton(773,777);
        pilotNeigungField = createFieldButton(838,475);
        pilotFahrgestellField12 = createFieldButton(679,558);
        pilotFahrgestellField34 = createFieldButton(679,464);
        pilotFahrgestellField56 = createFieldButton(679,327);
        pilotBremsenField2 = createFieldButton(842,293);
        pilotBremsenField4 = createFieldButton(933,293);
        pilotBremsenField6 = createFieldButton(1025,293);
        stage.addActor(pilotRadioField);
        stage.addActor(pilotSpeedField);
        stage.addActor(pilotNeigungField);
        stage.addActor(pilotFahrgestellField12);
        stage.addActor(pilotFahrgestellField34);
        stage.addActor(pilotFahrgestellField56);
        stage.addActor(pilotBremsenField2);
        stage.addActor(pilotBremsenField4);
        stage.addActor(pilotBremsenField6);



        copilotSpeedField = createFieldButton(1091,777);
        copilotNeigungField = createFieldButton(1027,475);
        copilotRadioField1 = createFieldButton( 1185, 791);
        copilotRadioField2 = createFieldButton( 1185, 883);
        pilotKlappenField12 = createFieldButton( 1185, 558);
        pilotKlappenField23 = createFieldButton( 1185, 464);
        pilotKlappenField45= createFieldButton( 1185, 327);
        pilotKlappenField56= createFieldButton( 1185, 191);
        stage.addActor(copilotSpeedField);
        stage.addActor(copilotNeigungField);
        stage.addActor(copilotRadioField1);
        stage.addActor(copilotRadioField2);
        stage.addActor(pilotKlappenField12);
        stage.addActor(pilotKlappenField23);
        stage.addActor(pilotKlappenField45);
        stage.addActor(pilotKlappenField56);



        koffe1 = createFieldButton(842, 142);
        koffe2 = createFieldButton(933,142);
        koffe3 = createFieldButton(1025,142);
        stage.addActor(koffe1);
        stage.addActor(koffe2);
        stage.addActor(koffe3);
        useNeuWurf = createFieldButton(681, 873);
        useKoffe1 = createFieldButton(656, 54);
        useKoffe2 = createFieldButton(717,54);
        useKoffe3 = createFieldButton(779,54);
        stage.addActor(useNeuWurf);
        stage.addActor(useKoffe1);
        stage.addActor(useKoffe2);
        stage.addActor(useKoffe3);

    }



}
