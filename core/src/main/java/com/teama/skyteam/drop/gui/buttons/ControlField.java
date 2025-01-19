/**
 * Repräsentiert ein GUI-Spielfeld, auf dem Spieler Würfel platzieren können.
 *
 * Die Klasse `ControlField` wird verwendet, um interaktive Spielfelder für das GUI zu erstellen,
 * auf denen Spieler ihre Würfel ablegen können. Jedes Spielfeld hat einen einzigartigen Namen,
 * einen zugehörigen Button zur Interaktion und einen Status, der angibt, ob das Feld bereits belegt ist.
 */
package com.teama.skyteam.drop.gui.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class ControlField {
    private String name;
    private ImageButton button;
    private boolean occupied;

    /**
     * Konstruktor zur Erstellung eines GUI-Spielfelds.
     *
     * @param name der Name des Spielfelds.
     * @param button der Button, der mit diesem Spielfeld verknüpft ist.
     */
    public ControlField(String name, ImageButton button) {
        this.name = name;
        this.button = button;
        this.occupied = false;
    }

    public String getName() {
        return name;
    }

    public ImageButton getButton() {
        return button;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
