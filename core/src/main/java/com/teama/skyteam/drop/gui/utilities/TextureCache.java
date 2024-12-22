package com.teama.skyteam.drop.gui.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    private static final Map<String, Texture> cache = new HashMap<>();

    public static Texture get(String path) {
        if (!cache.containsKey(path)) {
            cache.put(path, new Texture(Gdx.files.internal(path)));
        }
        return cache.get(path);
    }

    public static void dispose() {
        for (Texture t : cache.values()) {
            t.dispose();
        }
        cache.clear();
    }
}
