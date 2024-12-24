package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SchalterGUI {
    Image[] fahrgestellSchalter = new Image[3];
    Image[] bremsenSchalter = new Image[3];
    Image[] klappenSchalter = new Image[4];
    private int[][] fahrgestellKoordinaten = {{705,545}, {705,409}, {705,273}};
    private int[][] bremsenKoordinaten = {{867,242}, {960,242}, {1051,242}};
    private int[][] klappenKoordinaten = {{1213,545}, {1213,409}, {1213,273}, {1213,139}};
    private Stage stage;
    Texture texture = new Texture("images/spiel/main/plane_switch.png");
    private int step = 30;

    public SchalterGUI(Stage stage) {
        this.stage = stage;
    }

    public void addToStage(){
        for (int i = 0; i< fahrgestellKoordinaten.length; i++){
            fahrgestellSchalter[i] = new Image(texture);
            fahrgestellSchalter[i].setPosition(fahrgestellKoordinaten[i][0], fahrgestellKoordinaten[i][1]);
            stage.addActor(fahrgestellSchalter[i]);
        }

        for (int i = 0; i< bremsenKoordinaten.length; i++){
            bremsenSchalter[i] = new Image(texture);
            bremsenSchalter[i].setPosition(bremsenKoordinaten[i][0], bremsenKoordinaten[i][1]);
            stage.addActor(bremsenSchalter[i]);
        }

        for (int i = 0; i< klappenKoordinaten.length; i++){
            klappenSchalter[i] = new Image(texture);
            klappenSchalter[i].setPosition(klappenKoordinaten[i][0], klappenKoordinaten[i][1]);
            stage.addActor(klappenSchalter[i]);
        }

    }

    public void activateFahrgestellSchalter(int index){
        Image schalter = fahrgestellSchalter[index];
        float currentX = schalter.getX();
        float newX = currentX - step;
        schalter.setPosition(newX, schalter.getY());
    }

    public void activateBremsenSchalter(int index){
        Image schalter = bremsenSchalter[index];
        float currentX = schalter.getX();
        float newX = currentX - step;
        schalter.setPosition(newX, schalter.getY());
    }

    public void activateKlappenSchalter(int index){
        Image schalter = klappenSchalter[index];
        float currentX = schalter.getX();
        float newX = currentX - step;
        schalter.setPosition(newX, schalter.getY());
    }
}
