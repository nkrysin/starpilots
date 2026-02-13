/**
 * Repräsentiert ein GUI-Spielfeld, auf dem Player Würfel platzieren können.
 *
 * Die Klasse `ControlField` wird verwendet, um interaktive Spielfelder für das GUI zu erstellen,
 * auf denen Player ihre Würfel ablegen können. Jedes Spielfeld hat einen einzigartigen Namen,
 * einen zugehörigen Button zur Interaktion und einen Status, der angibt, ob das Field bereits belegt ist.
 */
package com.teama.skyteam.drop.gui.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.teama.skyteam.drop.domain.action.type.ActionName;

public class ControlField {
    private ActionName action;
    private ImageButton button;
    private boolean occupied;

    /**
     * Konstruktor zur Erstellung eines GUI-Spielfelds.
     *
     * @param action der Name des Spielfelds.
     * @param button der Button, der mit diesem Spielfeld verknüpft ist.
     */
    public ControlField(ActionName action, ImageButton button) {
        this.action = action;
        this.button = button;
        this.occupied = false;
    }

    public ActionName getAction() {
        return action;
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
