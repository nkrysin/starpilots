package com.teama.skyteam.drop.application.logic.observer;

public interface GameStateObserver {
    void onBrakesActivated(int schalterIndex, int indicatorValue);
    void onLandingGearActivated(int schalterIndex, int count);
    void onFlapsActivated(int schalterIndex, int state);
    void onTiltChanged(float targetRotation);
    void onSpeedChanged(int steps);
}
