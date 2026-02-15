/**
 * Verwaltet die Caching-Logik für Texturen im Game.
 *
 * Die Klasse `TextureCache` sorgt dafür, dass Texturen nur einmal geladen und
 * wiederverwendet werden, um Speicher- und Performanceprobleme zu minimieren.
 * Sie stellt Methoden bereit, um Texturen zu erhalten und den gesamten Cache freizugeben.
 */
package com.teama.skyteam.drop.ui.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureCache {
    private static final Map<String, Texture> cache = new HashMap<>();

    /**
     * Gibt eine Textur basierend auf dem angegebenen Pfad zurück.
     * Wenn die Textur nicht im Cache vorhanden ist, wird sie geladen und hinzugefügt.
     *
     * @param path Der Pfad zur Texturdatei.
     * @return Die Textur, die mit dem angegebenen Pfad verknüpft ist.
     */
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
