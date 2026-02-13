package com.teama.skyteam.drop.application.gamestate;

import com.teama.skyteam.drop.domain.action.command.ActionResult;
import com.teama.skyteam.drop.domain.model.player.Player;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.Dice;
import com.teama.skyteam.drop.domain.model.tokens.RerollToken;

import java.util.ArrayList;

public interface GameStateManager {
    /**
     * Обрабатывает радио-действие
     */
    ActionResult handleRadio(Dice dice);

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
     * Liefert das erste verfügbare CoffeeToken aus der Liste der KaffeeTokens und entfernt es aus der Liste.
     *
     * @return Das erste verfügbare CoffeeToken.
     */
    CoffeeToken kaffeeToken();

    /**
     * Liefert das erste verfügbare RerollToken aus der Liste der NeuWurfTokens und entfernt es aus der Liste.
     *
     * @return Das erste verfügbare RerollToken.
     */
    RerollToken neuWurfToken();

    /**
     * Setzt neue Würfel für den Player, indem ein neuer Wurf generiert wird.
     *
     * @param player Der Player, dessen Würfel neu gesetzt werden sollen.
     */
    void setNeuWuerfels(Player player);

    /**
     * Generiert eine Liste neuer Würfel mit zufälligen Werten.
     *
     * @return Eine Liste mit vier neu generierten Würfeln.
     */
    ArrayList<Dice> werfen();

    /**
     * Проверяет и обновляет скорость, если оба поля SPEED заняты
     * @return ActionResult с событием SpeedChangedEvent, если скорость обновлена
     */
    ActionResult checkAndUpdateSpeed();


}
