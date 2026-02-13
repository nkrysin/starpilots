/**
 * Stellt die GUI-Komponente für die Schalter und Indikatoren im Game bereit.
 *
 * Die Klasse `SwitchGUI` verwaltet die Schalter für LandingGear, Brakes und Flaps sowie
 * deren visuelle Indikatoren. Schalter können aktiviert und Indikatoren basierend auf den Spielwerten
 * bewegt werden. Alle GUI-Elemente werden auf der Bühne (`Stage`) dargestellt.
 */
package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.teama.skyteam.drop.gui.utilities.TextureCache;

public class SwitchGUI {
    Image[] fahrgestellSchalter = new Image[3];
    Image[] bremsenSchalter = new Image[3];
    Image[] klappenSchalter = new Image[4];
    Image bremsenIndicator;
    Image fahrgestellIndicator;
    Image klappenIndicator;
    Texture texture = new Texture(TexturePaths.PLANE_SWITCH_PATH);
    float newX, newY;
    private Stage stage;

    public SwitchGUI(Stage stage) {
        this.stage = stage;
        bremsenIndicator = new Image(TextureCache.get(TexturePaths.RED_MARKER));
        fahrgestellIndicator = new Image(TextureCache.get(TexturePaths.BLUE_MARKER));
        klappenIndicator = new Image(TextureCache.get(TexturePaths.ORANGE_MARKER));
    }

    /**
     * Fügt die Schalter und Indikatoren der Bühne hinzu.
     */
    public void addToStage() {
        for (int i = 0; i < UILayout.FAHRGESTELL_KOORDINATEN.length; i++) {
            fahrgestellSchalter[i] = new Image(texture);
            fahrgestellSchalter[i].setPosition(UILayout.FAHRGESTELL_KOORDINATEN[i][0], UILayout.FAHRGESTELL_KOORDINATEN[i][1]);
            stage.addActor(fahrgestellSchalter[i]);
        }

        for (int i = 0; i < UILayout.BREMSEN_KOORDINATEN.length; i++) {
            bremsenSchalter[i] = new Image(texture);
            bremsenSchalter[i].setPosition(UILayout.BREMSEN_KOORDINATEN[i][0], UILayout.BREMSEN_KOORDINATEN[i][1]);
            stage.addActor(bremsenSchalter[i]);
        }

        for (int i = 0; i < UILayout.KLAPPEN_KOORDINATEN.length; i++) {
            klappenSchalter[i] = new Image(texture);
            klappenSchalter[i].setPosition(UILayout.KLAPPEN_KOORDINATEN[i][0], UILayout.KLAPPEN_KOORDINATEN[i][1]);
            stage.addActor(klappenSchalter[i]);
        }

        addIndicators(bremsenIndicator, UILayout.BREMSEN_INDICATOR_KOORDINATEN[0][0], UILayout.BREMSEN_INDICATOR_KOORDINATEN[0][1], UILayout.BREMSEN_INDICATOR_ANGLE);

        addIndicators(fahrgestellIndicator, UILayout.FAHRGESTELL_INDICATOR_KOORDINATEN[0][0], UILayout.FAHRGESTELL_INDICATOR_KOORDINATEN[0][1], UILayout.FAHRGESTELL_INDICATOR_ANGLE);

        addIndicators(klappenIndicator, UILayout.KLAPPEN_INDICATOR_KOORDINATEN[0][0], UILayout.KLAPPEN_INDICATOR_KOORDINATEN[0][1], UILayout.KLAPPEN_INDICATOR_ANGLE);
    }

    /**
     * Fügt einen Indikator zur Bühne hinzu und konfiguriert Position, Rotation und Skalierung.
     *
     * @param indicator Der Indikator (Image), der hinzugefügt wird.
     * @param x X-Koordinate der Position.
     * @param y Y-Koordinate der Position.
     * @param angle Rotationswinkel des Indikators (in Grad).
     */
    private void addIndicators(Image indicator, int x, int y, float angle) {
        indicator.setPosition(x, y);
        indicator.setOrigin(indicator.getWidth() * 0.09f / 2, indicator.getHeight() * 0.09f / 2);
        indicator.setRotation(angle);
        indicator.setScale(0.09f);
        stage.addActor(indicator);
    }

    /**
     * Aktiviert den Schalter für das LandingGear.
     *
     * @param index Der Index des Schalters.
     */
    public void activateFahrgestellSchalter(int index) {
        Image schalter = fahrgestellSchalter[index];
        float currentX = schalter.getX();
        newX = currentX - UILayout.SCHALTER_STEP;
        schalter.addAction(Actions.moveTo(newX, schalter.getY(), UILayout.MOVE_DURATION));
    }

    /**
     * Aktiviert den Schalter für die Brakes.
     *
     * @param index Der Index des Schalters.
     */
    public void activateBremsenSchalter(int index) {
        Image schalter = bremsenSchalter[index];
        float currentX = schalter.getX();
        newX = currentX - UILayout.SCHALTER_STEP;
        schalter.addAction(Actions.moveTo(newX, schalter.getY(), UILayout.MOVE_DURATION));
    }

    /**
     * Aktiviert den Schalter für die Flaps.
     *
     * @param index Der Index des Schalters.
     */
    public void activateKlappenSchalter(int index) {
        Image schalter = klappenSchalter[index];
        float currentX = schalter.getX();
        newX = currentX - UILayout.SCHALTER_STEP;
        schalter.addAction(Actions.moveTo(newX, schalter.getY(), UILayout.MOVE_DURATION));
    }

    /**
     * Bewegt den Brakes-Indikator basierend auf dem angegebenen Wert.
     *
     * @param value Der Wert, der die Position bestimmt.
     */
    public void moveBremsenIndicator(int value) {
        switch (value) {
            case 2:
                newX = UILayout.BREMSEN_INDICATOR_KOORDINATEN[1][0];
                newY = UILayout.BREMSEN_INDICATOR_KOORDINATEN[1][1];
                bremsenIndicator.setRotation(UILayout.BREMSEN_ROTATION_CASE_2);
                bremsenIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 4:
                newX = UILayout.BREMSEN_INDICATOR_KOORDINATEN[2][0];
                newY = UILayout.BREMSEN_INDICATOR_KOORDINATEN[2][1];
                bremsenIndicator.setRotation(UILayout.BREMSEN_ROTATION_CASE_4);
                bremsenIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 6:
                newX = UILayout.BREMSEN_INDICATOR_KOORDINATEN[3][0];
                newY = UILayout.BREMSEN_INDICATOR_KOORDINATEN[3][1];
                bremsenIndicator.setRotation(UILayout.BREMSEN_ROTATION_CASE_6);
                bremsenIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;
        }
    }

    /**
     * Bewegt den LandingGear-Indikator basierend auf dem angegebenen Wert.
     *
     * @param value Der Wert, der die Position bestimmt.
     */
    public void moveFahrgestellIndicator(int value) {
        switch (value) {
            case 1:
                newX = UILayout.FAHRGESTELL_INDICATOR_KOORDINATEN[1][0];
                newY = UILayout.FAHRGESTELL_INDICATOR_KOORDINATEN[1][1];
                fahrgestellIndicator.setRotation(UILayout.FAHRGESTELL_ROTATION_CASE_1);
                fahrgestellIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 2:
                newX = UILayout.FAHRGESTELL_INDICATOR_KOORDINATEN[2][0];
                newY = UILayout.FAHRGESTELL_INDICATOR_KOORDINATEN[2][1];
                fahrgestellIndicator.setRotation(UILayout.FAHRGESTELL_ROTATION_CASE_2);
                fahrgestellIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 3:
                newX = UILayout.FAHRGESTELL_INDICATOR_KOORDINATEN[3][0];
                newY = UILayout.FAHRGESTELL_INDICATOR_KOORDINATEN[3][1];
                fahrgestellIndicator.setRotation(UILayout.FAHRGESTELL_ROTATION_CASE_3);
                fahrgestellIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

        }
    }

    /**
     * Bewegt den Flaps-Indikator basierend auf dem angegebenen Wert.
     *
     * @param value Der Wert, der die Position bestimmt.
     */
    public void moveKlappenIndicator(int value) {
        switch (value) {
            case 9:
                newX = UILayout.KLAPPEN_INDICATOR_KOORDINATEN[1][0];
                newY = UILayout.KLAPPEN_INDICATOR_KOORDINATEN[1][1];
                klappenIndicator.setRotation(UILayout.KLAPPEN_ROTATION_CASE_9);
                klappenIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 10:
                newX = UILayout.KLAPPEN_INDICATOR_KOORDINATEN[2][0];
                newY = UILayout.KLAPPEN_INDICATOR_KOORDINATEN[2][1];
                klappenIndicator.setRotation(UILayout.KLAPPEN_ROTATION_CASE_10);
                klappenIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 11:
                newX = UILayout.KLAPPEN_INDICATOR_KOORDINATEN[3][0];
                newY = UILayout.KLAPPEN_INDICATOR_KOORDINATEN[3][1];
                klappenIndicator.setRotation(UILayout.KLAPPEN_ROTATION_CASE_11);
                klappenIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 12:
                newX = UILayout.KLAPPEN_INDICATOR_KOORDINATEN[4][0];
                newY = UILayout.KLAPPEN_INDICATOR_KOORDINATEN[4][1];
                klappenIndicator.setRotation(UILayout.KLAPPEN_ROTATION_CASE_12);
                klappenIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
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
