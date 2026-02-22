package com.teama.starpilots.drop.ui.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextureUtils {
    /**
     * Ändert die Textur eines ImageButtons.
     *
     * @param button Der Button, dessen Textur geändert werden soll.
     * @param newTexturePath Der Pfad zur neuen Textur.
     */
    public static void switchTexture(ImageButton button, String newTexturePath) {
        Texture newTexture = TextureCache.get(newTexturePath);
        TextureRegionDrawable newDrawable = new TextureRegionDrawable(new TextureRegion(newTexture));
        button.getStyle().imageUp = newDrawable;
    }
}
