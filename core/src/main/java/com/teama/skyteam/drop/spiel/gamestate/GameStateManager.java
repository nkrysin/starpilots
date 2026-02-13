package com.teama.skyteam.drop.spiel.gamestate;

import com.teama.skyteam.drop.spiel.action.command.ActionResult;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.KaffeeToken;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;

public interface GameStateManager {
    /**
     * Обрабатывает радио-действие
     */
    ActionResult handleRadio(Wuerfel wuerfel);

    /**
     * Устанавливает наклон самолета на основе кубиков на полях NEIGEN
     * @return true, если наклон установлен (оба поля заняты)
     */
    ActionResult updateTilt();

    /**
     * Обновляет состояние поля маршрута на основе скорости
     * @return true, если состояние обновлено (оба поля SPEED заняты)
     */
    ActionResult updateTravelFieldState();

    /**
     * Двигает самолет вперед
     */
    ActionResult moveForward(int steps);

    /**
     * Уменьшает высоту полета
     */
    void reduceAltitude();

    /**
     * Добавляет токены перебросов, если они есть на текущей высоте
     */
    void addNeuWurfTokens();

    /**
     * Liefert das erste verfügbare KaffeeToken aus der Liste der KaffeeTokens und entfernt es aus der Liste.
     *
     * @return Das erste verfügbare KaffeeToken.
     */
    KaffeeToken kaffeeToken();

    /**
     * Liefert das erste verfügbare NeuWurfToken aus der Liste der NeuWurfTokens und entfernt es aus der Liste.
     *
     * @return Das erste verfügbare NeuWurfToken.
     */
    NeuWurfToken neuWurfToken();

    /**
     * Setzt neue Würfel für den Spieler, indem ein neuer Wurf generiert wird.
     *
     * @param spieler Der Spieler, dessen Würfel neu gesetzt werden sollen.
     */
    void setNeuWuerfels(Spieler spieler);

    /**
     * Generiert eine Liste neuer Würfel mit zufälligen Werten.
     *
     * @return Eine Liste mit vier neu generierten Würfeln.
     */
    ArrayList<Wuerfel> werfen();

    /**
     * Проверяет и обновляет скорость, если оба поля SPEED заняты
     * @return ActionResult с событием SpeedChangedEvent, если скорость обновлена
     */
    ActionResult checkAndUpdateSpeed();


}
