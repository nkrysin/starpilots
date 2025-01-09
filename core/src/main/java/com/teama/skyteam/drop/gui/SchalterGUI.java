package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.teama.skyteam.drop.gui.utilities.TextureCache;

public class SchalterGUI {
    Image[] fahrgestellSchalter = new Image[3];
    Image[] bremsenSchalter = new Image[3];
    Image[] klappenSchalter = new Image[4];
    Image bremsenIndicator;
    Image fahrgestellIndicator;
    Image klappenIndicator;
    private final int[][] fahrgestellKoordinaten = {{705,545}, {705,410}, {705,275}};
    private final int[][] bremsenKoordinaten = {{867,242}, {960,242}, {1051,242}};
    private final int[][] klappenKoordinaten = {{1213,545}, {1213,410}, {1213,275}, {1213,139}};
    private final int [][] bremsenIndicatorKoordinaten = {{823,403}, {866,373}, {970,355},{1067,394}};
    private final int [][] fahrgestellIndicatorKoordinaten = {{822,570}, {865,539}, {916,522},{970,520}};
    private final int [][] klappenIndicatorKoordinaten = {{1020,532}, {1067,560}, {1103,599},{1127,644}, {1137,699}};
    private Stage stage;
    Texture texture = new Texture("images/spiel/main/plane_switch.png");
    private int step = 30;
    private float rotation;
    float newX, newY;
    private final TextureCache textureCache = TextureCache.getInstance();

    public SchalterGUI(Stage stage) {
        this.stage = stage;
        bremsenIndicator = new Image(textureCache.get("images/spiel/markers/red_marker.png"));
        fahrgestellIndicator = new Image(textureCache.get("images/spiel/markers/blue_marker.png"));
        klappenIndicator = new Image(textureCache.get("images/spiel/markers/orange_marker.png"));
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

        addIndicators(bremsenIndicator,bremsenIndicatorKoordinaten[0][0], bremsenIndicatorKoordinaten[0][1],-40f);

        addIndicators(fahrgestellIndicator,fahrgestellIndicatorKoordinaten[0][0], fahrgestellIndicatorKoordinaten[0][1], -40f);

        addIndicators(klappenIndicator,klappenIndicatorKoordinaten[0][0], klappenIndicatorKoordinaten[0][1],25f);
    }

    private void addIndicators(Image indicator, int x, int y, float angle){
        indicator.setPosition(x,y);
        indicator.setOrigin(indicator.getWidth()*0.09f/2, indicator.getHeight()*0.09f/2);
        indicator.setRotation(angle);
        indicator.setScale(0.09f);
        stage.addActor(indicator);
    }

    public void activateFahrgestellSchalter(int index){
        Image schalter = fahrgestellSchalter[index];
        float currentX = schalter.getX();
        newX = currentX - step;
        schalter.addAction(Actions.moveTo(newX, schalter.getY(), 0.3f));
    }

    public void activateBremsenSchalter(int index){
        Image schalter = bremsenSchalter[index];
        float currentX = schalter.getX();
        newX = currentX - step;
        schalter.addAction(Actions.moveTo(newX, schalter.getY(), 0.3f));
    }

    public void activateKlappenSchalter(int index){
        Image schalter = klappenSchalter[index];
        float currentX = schalter.getX();
        newX = currentX - step;
        schalter.addAction(Actions.moveTo(newX, schalter.getY(), 0.3f));
    }

    public void moveBremsenIndicator(int value){
        switch (value){
            case 2:
                newX = bremsenIndicatorKoordinaten[1][0];
                newY = bremsenIndicatorKoordinaten[1][1];
                rotation = -25f;
                bremsenIndicator.setRotation(rotation);
                bremsenIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;

            case 4:
                newX = bremsenIndicatorKoordinaten[2][0];
                newY = bremsenIndicatorKoordinaten[2][1];
                rotation = 10f;
                bremsenIndicator.setRotation(rotation);
                bremsenIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;

            case 6:
                newX = bremsenIndicatorKoordinaten[3][0];
                newY = bremsenIndicatorKoordinaten[3][1];
                rotation = 45f;
                bremsenIndicator.setRotation(rotation);
                bremsenIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;
        }
    }

    public void moveFahrgestellIndicator(int value){
        switch (value){
            case 1:
                newX = fahrgestellIndicatorKoordinaten[1][0];
                newY = fahrgestellIndicatorKoordinaten[1][1];
                rotation = -25f;
                fahrgestellIndicator.setRotation(rotation);
                fahrgestellIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;

            case 2:
                newX = fahrgestellIndicatorKoordinaten[2][0];
                newY = fahrgestellIndicatorKoordinaten[2][1];
                rotation = -10f;
                fahrgestellIndicator.setRotation(rotation);
                fahrgestellIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;

            case 3:
                newX = fahrgestellIndicatorKoordinaten[3][0];
                newY = fahrgestellIndicatorKoordinaten[3][1];
                rotation = 10f;
                fahrgestellIndicator.setRotation(rotation);
                fahrgestellIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;

        }
    }

    public void moveKlappenIndicator(int value){
        switch (value){
            case 9:
                newX = klappenIndicatorKoordinaten[1][0];
                newY = klappenIndicatorKoordinaten[1][1];
                rotation = 45f;
                klappenIndicator.setRotation(rotation);
                klappenIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;

            case 10:
                newX = klappenIndicatorKoordinaten[2][0];
                newY = klappenIndicatorKoordinaten[2][1];
                rotation = 60f;
                klappenIndicator.setRotation(rotation);
                klappenIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;

            case 11:
                newX = klappenIndicatorKoordinaten[3][0];
                newY = klappenIndicatorKoordinaten[3][1];
                rotation = 75f;
                klappenIndicator.setRotation(rotation);
                klappenIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;

            case 12:
                newX = klappenIndicatorKoordinaten[4][0];
                newY = klappenIndicatorKoordinaten[4][1];
                rotation = 90f;
                klappenIndicator.setRotation(rotation);
                klappenIndicator.addAction(Actions.moveTo(newX, newY, 0.3f));
                break;
        }
    }


    public void dispose() {
        for (Image schalter : fahrgestellSchalter) {
            if (schalter != null) {
                schalter.remove();
            }
        }

        for (Image schalter : bremsenSchalter) {
            if (schalter != null) {
                schalter.remove();
            }
        }

        for (Image schalter : klappenSchalter) {
            if (schalter != null) {
                schalter.remove();
            }
        }

        if (bremsenIndicator != null) {
            bremsenIndicator.remove();
        }
        if (fahrgestellIndicator != null) {
            fahrgestellIndicator.remove();
        }
        if (klappenIndicator != null) {
            klappenIndicator.remove();
        }
    }
}
