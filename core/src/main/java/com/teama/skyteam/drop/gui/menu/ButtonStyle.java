/**
 * Stellt die Stildefinition für GUI-Buttons bereit.
 *
 * Die Klasse `ButtonStyle` wird verwendet, um eine konsistente Gestaltung von TextButtons
 * im GUI sicherzustellen. Der Stil basiert auf einer Skin-Datei und definiert Farben und Zustände
 * (normal, gedrückt, hover) für die Buttons.
 */
package com.teama.skyteam.drop.gui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.teama.skyteam.drop.gui.TexturePaths;


public class ButtonStyle {
    public static TextButton.TextButtonStyle style() {
        Skin skin = new Skin(Gdx.files.internal(TexturePaths.SKIN_PATH));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        // Setzt die Hintergründe für verschiedene Zustände (normal, gedrückt, hover).
        style.up = skin.newDrawable("button-c", Color.SLATE); // Normalzustand
        style.down = skin.newDrawable("button-p", Color.SLATE); // Gedrückt
        style.over = skin.newDrawable("button-h", Color.SKY); // Hover
        style.font = skin.getFont("button");
        style.fontColor = Color.WHITE;
        return style;
    }
}
