package com.teama.skyteam.drop.gui.utilities;
import com.teama.skyteam.drop.spieler.Spieler;
//для обратного вызова после завершения хода игрока
public interface PlayerActionCallback {
    void onPlayerActionCompleted(Spieler spieler);
}
