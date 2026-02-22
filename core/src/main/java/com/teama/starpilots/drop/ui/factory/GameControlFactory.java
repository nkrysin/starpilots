/**
 * Stellt Methoden zur Konstruktion von GUI-Elementen bereit.
 *
 * Die Klasse `GameControlFactory` bietet Hilfsmethoden, um GUI-Elemente wie Buttons und Kontrollgruppen
 * zu erstellen. Diese Elemente können auf einer `Stage` hinzugefügt werden, um sie im Game darzustellen.
 * Außerdem wird eine Methode bereitgestellt, um die erstellten Ressourcen freizugeben.
 */
package com.teama.starpilots.drop.ui.factory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teama.starpilots.drop.ui.buttons.ControlField;
import com.teama.starpilots.drop.ui.buttons.ControlGroup;
import com.teama.starpilots.drop.domain.action.type.ActionControlGroup;
import com.teama.starpilots.drop.domain.action.type.ActionName;
import com.teama.starpilots.drop.ui.constants.TexturePaths;
import com.teama.starpilots.drop.ui.constants.UILayout;

import java.util.ArrayList;

public class GameControlFactory {

    private final ArrayList<ImageButton> createdButtons = new ArrayList<>(); // Liste der erstellten Buttons.

    /**
     * Erstellt einen neuen Button mit einer Textur und fügt ihn der internen Liste hinzu.
     *
     * @param textureName der Pfad zur Texturdatei.
     * @param x die X-Koordinate des Buttons.
     * @param y die Y-Koordinate des Buttons.
     * @param sizeX die Breite des Buttons.
     * @param sizeY die Höhe des Buttons.
     * @return der erstellte `ImageButton`.
     */
    public ImageButton createButton(String textureName, int x, int y, int sizeX, int sizeY) {
        Texture texture = new Texture(textureName);
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(style);
        button.setBounds(x, y, sizeX, sizeY);
        createdButtons.add(button);
        return button;
    }

    /**
     * Erstellt eine neue Kontrollgruppe mit mehreren Feldern und Buttons.
     *
     * @param controlGroup der Name der Kontrollgruppe.
     * @param actions die Namen der Felder in der Gruppe.
     * @param positions die Positionen der Felder (Array von [x, y]-Koordinaten).
     * @param stage die `Stage`, auf der die Buttons hinzugefügt werden.
     * @param sizeX die Breite der Buttons.
     * @param sizeY die Höhe der Buttons.
     * @return die erstellte `ControlGroup`.
     */
    public ControlGroup createControlGroup(ActionControlGroup controlGroup, ActionName[] actions, int[][] positions, Stage stage, int sizeX, int sizeY) {
        ArrayList<ControlField> fields = new ArrayList<>();
        for (int i = 0; i < actions.length; i++) {
            ImageButton button = createButton(TexturePaths.WITHOUT_HIGHLIGHT_PATH, positions[i][0], positions[i][1], sizeX, sizeY);
            ControlField field = new ControlField(actions[i], button);
            fields.add(field);
            stage.addActor(button);
        }
        return new ControlGroup(controlGroup, fields);
    }

    /**
     * Erstellt eine Kontrollgruppe speziell für Kaffee-Aktionen.
     *
     * @param groupName der Name der Kontrollgruppe.
     * @param positions die Positionen der Buttons (Array von [x, y]-Koordinaten).
     * @param stage die `Stage`, auf der die Buttons hinzugefügt werden.
     * @return die erstellte `ControlGroup` für Kaffee-Aktionen.
     */
    public ControlGroup createUseCoffeeButtons(ActionControlGroup groupName, int[][] positions, Stage stage) {
        ActionName[] coffeeFieldNames = {ActionName.USE_COFFEE_1, ActionName.USE_COFFEE_2, ActionName.USE_COFFEE_3};
        ArrayList<ControlField> fields = new ArrayList<>();
        for (int i = 0; i < coffeeFieldNames.length; i++) {
            ImageButton button = createButton(TexturePaths.WITHOUT_HIGHLIGHT_PATH,
                positions[i][0],
                positions[i][1],
                UILayout.COFFEE_BUTTON_SIZE[0],
                UILayout.COFFEE_BUTTON_SIZE[1]
            );
            ControlField field = new ControlField(coffeeFieldNames[i], button);
            fields.add(field);
            stage.addActor(button);
        }
        return new ControlGroup(groupName, fields);
    }

    /**
     * Erstellt einen speziellen Button für den "Neu Wurf"-Vorgang.
     *
     * @param stage die `Stage`, auf der der Button hinzugefügt wird.
     * @return das erstellte `ControlField`, das den Button enthält.
     */
    public ControlField createRerollButton(Stage stage) {
        ImageButton button = createButton(
            TexturePaths.WITHOUT_HIGHLIGHT_PATH,
            UILayout.REROLL_BUTTON_COORDINATES[0],
            UILayout.REROLL_BUTTON_COORDINATES[1],
            UILayout.REROLL_BUTTON_SIZE[0],
            UILayout.REROLL_BUTTON_SIZE[1]
        );
        ControlField field = new ControlField(ActionName.REROLL, button);
        stage.addActor(button);
        return field;
    }

    public void dispose() {
        for (ImageButton button : createdButtons) {
            if (button != null) {
                button.remove();
            }
        }
        createdButtons.clear();
    }
}
