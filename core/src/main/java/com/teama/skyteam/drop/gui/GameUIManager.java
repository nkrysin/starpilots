package com.teama.skyteam.drop.gui;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameUIManager {
    private SzenarioGUI szenarioGUI;

    public GameUIManager() {
        this.szenarioGUI = new SzenarioGUI();
    }

    public void updateScenario(int currentSpeed) {
        szenarioGUI.moveStep(currentSpeed);
    }

    public void render(SpriteBatch batch) {
        szenarioGUI.render(batch);
    }

    public void dispose() {
        szenarioGUI.dispose();
    }
}
