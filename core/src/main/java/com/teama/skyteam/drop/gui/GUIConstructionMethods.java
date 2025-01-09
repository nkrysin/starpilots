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

    public ImageButton createButton(String textureName, int x, int y, int sizeX, int sizeY) {
        Texture texture = new Texture(textureName);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(style);
        button.setBounds(x, y, sizeX, sizeY);
        return button;
    }

    public ControlGroup createControlGroup(String groupName, String[] fieldNames, int[][] positions, Stage stage, int sizeX, int sizeY) {
        ArrayList<ControlField> fields = new ArrayList<>();
        for (int i = 0; i < fieldNames.length; i++) {
            ImageButton button = createButton("images/spiel/main/empty.png", positions[i][0], positions[i][1], sizeX, sizeY);
            ControlField field = new ControlField(fieldNames[i], button);
            fields.add(field);
            stage.addActor(button);
        }
        return new ControlGroup(groupName, fields);
    }

    public ControlGroup createUseKoffeeButtons(String groupName, int[][] positions, Stage stage) {
        String[] koffeeFieldNames = {"UseKoffe1", "UseKoffe2", "UseKoffe3"};
        ArrayList<ControlField> fields = new ArrayList<>();
        for (int i = 0; i < koffeeFieldNames.length; i++) {
            ImageButton button = createButton("images/spiel/main/empty.png",
                positions[i][0],
                positions[i][1],
                39,
                39
            );
            ControlField field = new ControlField(koffeeFieldNames[i], button);
            fields.add(field);
            stage.addActor(button);
        }
        return new ControlGroup(groupName, fields);
    }

    ControlField createNeuWurfButton(Stage stage) {
        ImageButton button = createButton("images/spiel/main/empty.png", 681, 873, 60, 60);
        ControlField field = new ControlField("neuWurf", button);
        stage.addActor(button);
        return field;
    }
}
