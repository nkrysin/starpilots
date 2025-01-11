package com.teama.skyteam.drop.gui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class ButtonStyle {
    public static TextButton.TextButtonStyle style() {
        Skin skin = new Skin(Gdx.files.internal("assets/skin/flat-earth-ui.json"));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.newDrawable("button-c", Color.SLATE);
        style.down = skin.newDrawable("button-p", Color.SLATE);
        style.over = skin.newDrawable("button-h", Color.SKY);
        style.font = skin.getFont("button");
        style.fontColor = Color.WHITE;
        return style;
    }
}
