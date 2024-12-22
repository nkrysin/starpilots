package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teama.skyteam.drop.gui.buttons.ControlField;
import com.teama.skyteam.drop.gui.buttons.ControlGroup;

import java.util.ArrayList;

public class GUIConstructionMethods {

    public ImageButton createDiceButton(String textureName, int x, int y) {
        Texture tex = new Texture(textureName);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new com.badlogic.gdx.graphics.g2d.TextureRegion(tex));
        ImageButton diceButton = new ImageButton(style);
        diceButton.setBounds(x, y, 55, 55);
        return diceButton;
    }

    public Button createFieldButton(int x, int y) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = null;
        style.down = null;
        style.over = null;

        Button button = new Button(style);
        button.setPosition(x, y);
        button.setSize(60, 60);
        return button;
    }

    public ControlGroup createControlGroup(String groupName, String[] fieldNames, int[][] positions, Stage stage) {
        ArrayList<ControlField> fields = new ArrayList<>();
        for (int i = 0; i < fieldNames.length; i++) {
            Button button = createFieldButton(positions[i][0], positions[i][1]);
            ControlField field = new ControlField(fieldNames[i], button);
            fields.add(field);
            stage.addActor(button);
        }
        return new ControlGroup(groupName, fields);
    }

    public void createSingleFieldButton(String name, int x, int y, Stage stage) {
        Button button = createFieldButton(x, y);
        stage.addActor(button);
    }
}
