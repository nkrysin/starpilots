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
    void onCoffeTokenUsed(Spieler spieler, int wuerfelIndex, int newValue, int remainingTokens);
    void onDiceRerolled(Spieler spieler, List<Wuerfel> newDice);
    void onPilotRadioActivated();
    void onCopilotRadioActivated();

}
