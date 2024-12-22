package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teama.skyteam.drop.gui.buttons.ControlField;
import com.teama.skyteam.drop.gui.buttons.ControlGroup;

import java.util.ArrayList;


public class GUIConstructionMethods {

    public ImageButton createButton(String textureName, int x, int y) {
        Texture texture = new Texture(textureName);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(style);
        button.setBounds(x, y, 55, 55);
        return button;
    }

    public ControlGroup createControlGroup(String groupName, String[] fieldNames, int[][] positions, Stage stage) {
        ArrayList<ControlField> fields = new ArrayList<>();
        for (int i = 0; i < fieldNames.length; i++) {
            ImageButton button = createButton("images/spiel/main/empty.png", positions[i][0], positions[i][1]);
            ControlField field = new ControlField(fieldNames[i], button);
            fields.add(field);
            stage.addActor(button);
        }
        return new ControlGroup(groupName, fields);
    }

    public ControlGroup createKoffeeButtons(String groupName, Stage stage) {
        String[] koffeeFieldNames = {"UseKoffe1", "UseKoffe2", "UseKoffe3"};
        int[][] koffeePositions = {
            {656, 54},
            {717, 54},
            {779, 54}
        };
        return createControlGroup(groupName, koffeeFieldNames, koffeePositions, stage);
    }
}
