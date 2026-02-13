package com.teama.skyteam.drop.application.logic.fields;

import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.game.Game;

import java.util.ArrayList;
import java.util.List;

public class StandardFieldManager implements FieldManager {
    @Override
    public void clearManyUseFields(Game game) {
        game.getFeldPilot(FieldName.NEIGEN).setWuerfel(null);
        game.getFeldPilot(FieldName.SPEED).setWuerfel(null);
        game.getFeldPilot(FieldName.RADIO_PILOT).setWuerfel(null);
        game.getFeldCopilot(FieldName.NEIGEN).setWuerfel(null);
        game.getFeldCopilot(FieldName.SPEED).setWuerfel(null);
        game.getFeldCopilot(FieldName.RADIO_COPILOT_1).setWuerfel(null);
        game.getFeldCopilot(FieldName.RADIO_COPILOT_2).setWuerfel(null);
    }
    @Override
    public void removeDiceFromOneUseFields(List<Field> oneUseFields) {
        int i = 0;
        while (i < oneUseFields.size()) {
            Field f = oneUseFields.get(i);
            if (f.isBelegt()) {
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
        Field fg12 = game.getFeldPilot(FieldName.FAHRGESTELL_1_2);
        Field fg34 = game.getFeldPilot(FieldName.FAHRGESTELL_3_4);
        Field fg56 = game.getFeldPilot(FieldName.FAHRGESTELL_5_6);
        Field b2 = game.getFeldPilot(FieldName.BREMSEN_2);
        Field b4 = game.getFeldPilot(FieldName.BREMSEN_4);
        Field b6 = game.getFeldPilot(FieldName.BREMSEN_6);
        Field k12 = game.getFeldCopilot(FieldName.KLAPPEN_1_2);
        Field k23 = game.getFeldCopilot(FieldName.KLAPPEN_2_3);
        Field k45 = game.getFeldCopilot(FieldName.KLAPPEN_4_5);
        Field k56 = game.getFeldCopilot(FieldName.KLAPPEN_5_6);
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
    public void clearKaffeeFields(Game game, int kaffeeTokenCount) {
        int kaffeeCount = game.getKaffeeTokenCount();
        switch (kaffeeCount) {
            case 0:
                game.getFeldPilot(FieldName.KAFFEE_1).setWuerfel(null);
                game.getFeldCopilot(FieldName.KAFFEE_1).setWuerfel(null);
                game.getFeldPilot(FieldName.KAFFEE_2).setWuerfel(null);
                game.getFeldCopilot(FieldName.KAFFEE_2).setWuerfel(null);
                game.getFeldPilot(FieldName.KAFFEE_3).setWuerfel(null);
                game.getFeldCopilot(FieldName.KAFFEE_3).setWuerfel(null);
                break;
            case 1:
                game.getFeldPilot(FieldName.KAFFEE_2).setWuerfel(null);
                game.getFeldCopilot(FieldName.KAFFEE_2).setWuerfel(null);
                game.getFeldPilot(FieldName.KAFFEE_3).setWuerfel(null);
                game.getFeldCopilot(FieldName.KAFFEE_3).setWuerfel(null);
                break;
            case 2:
                game.getFeldPilot(FieldName.KAFFEE_3).setWuerfel(null);
                game.getFeldCopilot(FieldName.KAFFEE_3).setWuerfel(null);
                break;
        }
    }


}
