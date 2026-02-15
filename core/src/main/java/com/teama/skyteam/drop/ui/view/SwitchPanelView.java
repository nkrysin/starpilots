/**
 * Stellt die GUI-Komponente für die Schalter und Indikatoren im Game bereit.
 *
 * Die Klasse `SwitchPanelView` verwaltet die Schalter für LandingGear, Brakes und Flaps sowie
 * deren visuelle Indikatoren. Schalter können aktiviert und Indikatoren basierend auf den Spielwerten
 * bewegt werden. Alle GUI-Elemente werden auf der Bühne (`Stage`) dargestellt.
 */
package com.teama.skyteam.drop.ui.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.teama.skyteam.drop.ui.constants.TexturePaths;
import com.teama.skyteam.drop.ui.constants.UILayout;
import com.teama.skyteam.drop.ui.utilities.TextureCache;

public class SwitchPanelView {
    Image[] landingGearSwitch = new Image[3];
    Image[] brakesSwitch = new Image[3];
    Image[] flapsSwitch = new Image[4];
    Image brakesIndicator;
    Image landingGearIndicator;
    Image flapsIndicator;
    Texture texture = new Texture(TexturePaths.PLANE_SWITCH_PATH);
    float newX, newY;
    private Stage stage;

    public SwitchPanelView(Stage stage) {
        this.stage = stage;
        brakesIndicator = new Image(TextureCache.get(TexturePaths.RED_MARKER));
        landingGearIndicator = new Image(TextureCache.get(TexturePaths.BLUE_MARKER));
        flapsIndicator = new Image(TextureCache.get(TexturePaths.ORANGE_MARKER));
    }

    /**
     * Fügt die Schalter und Indikatoren der Bühne hinzu.
     */
    public void addToStage() {
        for (int i = 0; i < UILayout.LANDING_GEAR_COORDINATES.length; i++) {
            landingGearSwitch[i] = new Image(texture);
            landingGearSwitch[i].setPosition(UILayout.LANDING_GEAR_COORDINATES[i][0], UILayout.LANDING_GEAR_COORDINATES[i][1]);
            stage.addActor(landingGearSwitch[i]);
        }

        for (int i = 0; i < UILayout.BRAKES_COORDINATES.length; i++) {
            brakesSwitch[i] = new Image(texture);
            brakesSwitch[i].setPosition(UILayout.BRAKES_COORDINATES[i][0], UILayout.BRAKES_COORDINATES[i][1]);
            stage.addActor(brakesSwitch[i]);
        }

        for (int i = 0; i < UILayout.FLAPS_COORDINATES.length; i++) {
            flapsSwitch[i] = new Image(texture);
            flapsSwitch[i].setPosition(UILayout.FLAPS_COORDINATES[i][0], UILayout.FLAPS_COORDINATES[i][1]);
            stage.addActor(flapsSwitch[i]);
        }

        addIndicators(brakesIndicator, UILayout.BRAKES_INDICATOR_COORDINATES[0][0], UILayout.BRAKES_INDICATOR_COORDINATES[0][1], UILayout.BRAKES_INDICATOR_ANGLE);

        addIndicators(landingGearIndicator, UILayout.LANDING_GEAR_INDICATOR_COORDINATES[0][0], UILayout.LANDING_GEAR_INDICATOR_COORDINATES[0][1], UILayout.LANDING_GEAR_INDICATOR_ANGLE);

        addIndicators(flapsIndicator, UILayout.FLAPS_INDICATOR_COORDINATES[0][0], UILayout.FLAPS_INDICATOR_COORDINATES[0][1], UILayout.FLAPS_INDICATOR_ANGLE);
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
    public void activateLandingGearSwitch(int index) {
        Image lgSwitch = landingGearSwitch[index];
        float currentX = lgSwitch.getX();
        newX = currentX - UILayout.SWITCH_STEP;
        lgSwitch.addAction(Actions.moveTo(newX, lgSwitch.getY(), UILayout.MOVE_DURATION));
    }

    /**
     * Aktiviert den Schalter für die Brakes.
     *
     * @param index Der Index des Schalters.
     */
    public void activateBrakesSwitch(int index) {
        Image bSwitch = brakesSwitch[index];
        float currentX = bSwitch.getX();
        newX = currentX - UILayout.SWITCH_STEP;
        bSwitch.addAction(Actions.moveTo(newX, bSwitch.getY(), UILayout.MOVE_DURATION));
    }

    /**
     * Aktiviert den Schalter für die Flaps.
     *
     * @param index Der Index des Schalters.
     */
    public void aktivateFlapsSwitch(int index) {
        Image fSwitch = flapsSwitch[index];
        float currentX = fSwitch.getX();
        newX = currentX - UILayout.SWITCH_STEP;
        fSwitch.addAction(Actions.moveTo(newX, fSwitch.getY(), UILayout.MOVE_DURATION));
    }

    /**
     * Bewegt den Brakes-Indikator basierend auf dem angegebenen Wert.
     *
     * @param value Der Wert, der die Position bestimmt.
     */
    public void moveBrakesIndicator(int value) {
        switch (value) {
            case 2:
                newX = UILayout.BRAKES_INDICATOR_COORDINATES[1][0];
                newY = UILayout.BRAKES_INDICATOR_COORDINATES[1][1];
                brakesIndicator.setRotation(UILayout.BRAKES_ROTATION_CASE_2);
                brakesIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 4:
                newX = UILayout.BRAKES_INDICATOR_COORDINATES[2][0];
                newY = UILayout.BRAKES_INDICATOR_COORDINATES[2][1];
                brakesIndicator.setRotation(UILayout.BRAKES_ROTATION_CASE_4);
                brakesIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 6:
                newX = UILayout.BRAKES_INDICATOR_COORDINATES[3][0];
                newY = UILayout.BRAKES_INDICATOR_COORDINATES[3][1];
                brakesIndicator.setRotation(UILayout.BRAKES_ROTATION_CASE_6);
                brakesIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;
        }
    }

    /**
     * Bewegt den LandingGear-Indikator basierend auf dem angegebenen Wert.
     *
     * @param value Der Wert, der die Position bestimmt.
     */
    public void moveLandingGearIndicator(int value) {
        switch (value) {
            case 1:
                newX = UILayout.LANDING_GEAR_INDICATOR_COORDINATES[1][0];
                newY = UILayout.LANDING_GEAR_INDICATOR_COORDINATES[1][1];
                landingGearIndicator.setRotation(UILayout.LANDING_GEAR_ROTATION_CASE_1);
                landingGearIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 2:
                newX = UILayout.LANDING_GEAR_INDICATOR_COORDINATES[2][0];
                newY = UILayout.LANDING_GEAR_INDICATOR_COORDINATES[2][1];
                landingGearIndicator.setRotation(UILayout.LANDING_GEAR_ROTATION_CASE_2);
                landingGearIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 3:
                newX = UILayout.LANDING_GEAR_INDICATOR_COORDINATES[3][0];
                newY = UILayout.LANDING_GEAR_INDICATOR_COORDINATES[3][1];
                landingGearIndicator.setRotation(UILayout.LANDING_GEAR_ROTATION_CASE_3);
                landingGearIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

        }
    }

    /**
     * Bewegt den Flaps-Indikator basierend auf dem angegebenen Wert.
     *
     * @param value Der Wert, der die Position bestimmt.
     */
    public void moveFlapsIndicator(int value) {
        switch (value) {
            case 9:
                newX = UILayout.FLAPS_INDICATOR_COORDINATES[1][0];
                newY = UILayout.FLAPS_INDICATOR_COORDINATES[1][1];
                flapsIndicator.setRotation(UILayout.FLAPS_ROTATION_CASE_9);
                flapsIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 10:
                newX = UILayout.FLAPS_INDICATOR_COORDINATES[2][0];
                newY = UILayout.FLAPS_INDICATOR_COORDINATES[2][1];
                flapsIndicator.setRotation(UILayout.FLAPS_ROTATION_CASE_10);
                flapsIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 11:
                newX = UILayout.FLAPS_INDICATOR_COORDINATES[3][0];
                newY = UILayout.FLAPS_INDICATOR_COORDINATES[3][1];
                flapsIndicator.setRotation(UILayout.FLAPS_ROTATION_CASE_11);
                flapsIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;

            case 12:
                newX = UILayout.FLAPS_INDICATOR_COORDINATES[4][0];
                newY = UILayout.FLAPS_INDICATOR_COORDINATES[4][1];
                flapsIndicator.setRotation(UILayout.FLAPS_ROTATION_CASE_12);
                flapsIndicator.addAction(Actions.moveTo(newX, newY, UILayout.MOVE_DURATION));
                break;
        }
    }


    public void dispose() {
        for (Image schalter : landingGearSwitch) {
            if (schalter != null) {
                schalter.remove();
            }
        }

        for (Image schalter : brakesSwitch) {
            if (schalter != null) {
                schalter.remove();
            }
        }

        for (Image schalter : flapsSwitch) {
            if (schalter != null) {
                schalter.remove();
            }
        }

        if (brakesIndicator != null) {
            brakesIndicator.remove();
        }
        if (landingGearIndicator != null) {
            landingGearIndicator.remove();
        }
        if (flapsIndicator != null) {
            flapsIndicator.remove();
        }
    }
}
