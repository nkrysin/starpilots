package com.teama.skyteam.drop.application.logic.fields;

import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.game.Game;

import java.util.ArrayList;
import java.util.List;

public class StandardFieldManager implements FieldManager {
    @Override
    public void clearManyUseFields(Game game) {
        game.getFieldPilot(FieldName.TILT).setDice(null);
        game.getFieldPilot(FieldName.SPEED).setDice(null);
        game.getFieldPilot(FieldName.RADIO_PILOT).setDice(null);
        game.getFieldCopilot(FieldName.TILT).setDice(null);
        game.getFieldCopilot(FieldName.SPEED).setDice(null);
        game.getFieldCopilot(FieldName.RADIO_COPILOT_1).setDice(null);
        game.getFieldCopilot(FieldName.RADIO_COPILOT_2).setDice(null);
    }
    @Override
    public void removeDiceFromOneUseFields(List<Field> oneUseFields) {
        int i = 0;
        while (i < oneUseFields.size()) {
            Field f = oneUseFields.get(i);
            if (f.isOccupied()) {
                f.clearOneUseFeld();
                oneUseFields.remove(i);
            } else {
                i++;
            }
        }
    }
    @Override
    public List<Field> getOneUseFields(Game game) {
        ArrayList<Field> oneUseFields = new ArrayList<>();
        Field fg12 = game.getFieldPilot(FieldName.LANDING_GEAR_1_2);
        Field fg34 = game.getFieldPilot(FieldName.LANDING_GEAR_3_4);
        Field fg56 = game.getFieldPilot(FieldName.LANDING_GEAR_5_6);
        Field b2 = game.getFieldPilot(FieldName.BRAKES_2);
        Field b4 = game.getFieldPilot(FieldName.BRAKES_4);
        Field b6 = game.getFieldPilot(FieldName.BRAKES_6);
        Field k12 = game.getFieldCopilot(FieldName.FLIPS_1_2);
        Field k23 = game.getFieldCopilot(FieldName.FLIPS_2_3);
        Field k45 = game.getFieldCopilot(FieldName.FLIPS_4_5);
        Field k56 = game.getFieldCopilot(FieldName.FLIPS_5_6);
        oneUseFields.add(fg12);
        oneUseFields.add(fg34);
        oneUseFields.add(fg56);
        oneUseFields.add(b2);
        oneUseFields.add(b4);
        oneUseFields.add(b6);
        oneUseFields.add(k12);
        oneUseFields.add(k23);
        oneUseFields.add(k45);
        oneUseFields.add(k56);
        return oneUseFields;
    }

    @Override
    public void clearCoffeeFields(Game game, int coffeeTokenCount) {
        int kaffeeCount = game.getCoffeeTokenCount();
        switch (kaffeeCount) {
            case 0:
                game.getFieldPilot(FieldName.COFFEE_1).setDice(null);
                game.getFieldCopilot(FieldName.COFFEE_1).setDice(null);
                game.getFieldPilot(FieldName.COFFEE_2).setDice(null);
                game.getFieldCopilot(FieldName.COFFEE_2).setDice(null);
                game.getFieldPilot(FieldName.COFFEE_3).setDice(null);
                game.getFieldCopilot(FieldName.COFFEE_3).setDice(null);
                break;
            case 1:
                game.getFieldPilot(FieldName.COFFEE_2).setDice(null);
                game.getFieldCopilot(FieldName.COFFEE_2).setDice(null);
                game.getFieldPilot(FieldName.COFFEE_3).setDice(null);
                game.getFieldCopilot(FieldName.COFFEE_3).setDice(null);
                break;
            case 2:
                game.getFieldPilot(FieldName.COFFEE_3).setDice(null);
                game.getFieldCopilot(FieldName.COFFEE_3).setDice(null);
                break;
        }
    }


}
