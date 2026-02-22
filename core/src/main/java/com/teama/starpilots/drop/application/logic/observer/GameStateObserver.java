package com.teama.starpilots.drop.application.logic.observer;

public interface GameStateObserver {
    void onBrakesActivated(int switchIndex, int indicatorValue);
    void onLandingGearActivated(int switchIndex, int count);
    void onFlapsActivated(int switchIndex, int state);
    void onTiltChanged(float targetRotation);
    void onSpeedChanged(int steps);
}
