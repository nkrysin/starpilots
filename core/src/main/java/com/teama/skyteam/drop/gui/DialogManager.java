package com.teama.skyteam.drop.gui;

import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface DialogManager {
    void showMessage(String title, String message);
    void showKaffeeTokenWindow(Wuerfel wuerfel, Spieler spieler, Consumer<Integer> callback);
    void showNeuWurfTokenWindow(Spieler spieler, BiConsumer<Spieler, Spieler> callback);
    void showFinalWindow(boolean win);
}
