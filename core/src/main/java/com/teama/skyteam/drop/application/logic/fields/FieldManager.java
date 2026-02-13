package com.teama.skyteam.drop.application.logic.fields;

import com.teama.skyteam.drop.domain.model.feld.Feld;
import com.teama.skyteam.drop.domain.model.game.Spiel;

import java.util.List;

public interface FieldManager {
    /**
     * Очищает многоразовые поля (radio, speed, neigung)
     */
    void clearManyUseFields(Spiel spiel);

    /**
     * Удаляет кубики с одноразовых полей
     */
    void removeDiceFromOneUseFields(List<Feld> oneUseFields);

    /**
     * Получает список одноразовых полей
     */
    List<Feld> getOneUseFields(Spiel spiel);

    /**
     * Очищает кофейные поля на основе количества токенов
     */
    void clearKaffeeFields(Spiel spiel, int kaffeeTokenCount);
}

