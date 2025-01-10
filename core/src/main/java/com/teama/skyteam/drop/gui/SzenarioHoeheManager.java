package com.teama.skyteam.drop.gui;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.gui.szenario.HoeheGUI;
import com.teama.skyteam.drop.gui.szenario.SzenarioGUI;
import com.teama.skyteam.drop.spiel.Spiel;

public class SzenarioHoeheManager {
    private SzenarioGUI szenarioGUI;
    private HoeheGUI hoeheGUI;

    public SzenarioHoeheManager(Spiel spiel) {
        this.szenarioGUI = new SzenarioGUI(spiel);
        this.hoeheGUI = new HoeheGUI(spiel);
    }

    public void updateScenario(int currentSpeed) {
        szenarioGUI.moveStep(currentSpeed);
    }

    public void updateHoehe(){
        hoeheGUI.moveStep();
    }

    public void render(SpriteBatch batch) {
        szenarioGUI.render(batch);
        hoeheGUI.render(batch);
    }

    public void dispose() {
        szenarioGUI.dispose();
        hoeheGUI.dispose();
    }
}
