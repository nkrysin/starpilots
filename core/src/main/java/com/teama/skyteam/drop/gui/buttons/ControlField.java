package com.teama.skyteam.drop.gui.buttons;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ControlField {
    private String name;
    private Button button;
    private boolean occupied;

    public ControlField(String name, Button button) {
        this.name = name;
        this.button = button;
        this.occupied = false;
    }

    public String getName() {
        return name;
    }

    public Button getButton() {
        return button;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
