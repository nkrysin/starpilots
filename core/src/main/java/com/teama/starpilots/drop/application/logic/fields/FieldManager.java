package com.teama.starpilots.drop.application.logic.fields;

import com.teama.starpilots.drop.domain.model.field.Field;
import com.teama.starpilots.drop.domain.model.game.Game;

import java.util.List;

public interface FieldManager {
    /**
     * Очищает многоразовые поля (radio, speed, neigung)
     */
    void clearManyUseFields(Game game);

    /**
     * Удаляет кубики с одноразовых полей
     */
    void removeDiceFromOneUseFields(List<Field> oneUseFields);

    /**
     * Получает список одноразовых полей
     */
    List<Field> getOneUseFields(Game game);

    /**
     * Очищает кофейные поля на основе количества токенов
     */
    void clearCoffeeFields(Game game, int coffeeTokenCount);
}

