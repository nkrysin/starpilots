package com.teama.skyteam.drop.spiel.logik.observer;

import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.List;

public interface GameStateObserver {
    void onBrakesActivated(int schalterIndex, int indicatorValue);
    void onChassisActivated(int schalterIndex, int count);
    void onFlapsActivated(int schalterIndex, int state);
    void onTiltChanged(float targetRotation);
    void onSpeedChanged(int steps);
}
