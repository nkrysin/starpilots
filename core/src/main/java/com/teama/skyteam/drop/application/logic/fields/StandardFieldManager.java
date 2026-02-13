package com.teama.skyteam.drop.application.logic.fields;

import com.teama.skyteam.drop.domain.model.feld.Feld;
import com.teama.skyteam.drop.domain.model.feld.FeldName;
import com.teama.skyteam.drop.domain.model.game.Spiel;

import java.util.ArrayList;
import java.util.List;

public class StandardFieldManager implements FieldManager {
    @Override
    public void clearManyUseFields(Spiel spiel) {
        spiel.getFeldPilot(FeldName.NEIGEN).setWuerfel(null);
        spiel.getFeldPilot(FeldName.SPEED).setWuerfel(null);
        spiel.getFeldPilot(FeldName.RADIO_PILOT).setWuerfel(null);
        spiel.getFeldCopilot(FeldName.NEIGEN).setWuerfel(null);
        spiel.getFeldCopilot(FeldName.SPEED).setWuerfel(null);
        spiel.getFeldCopilot(FeldName.RADIO_COPILOT_1).setWuerfel(null);
        spiel.getFeldCopilot(FeldName.RADIO_COPILOT_2).setWuerfel(null);
    }
    @Override
    public void removeDiceFromOneUseFields(List<Feld> oneUseFields) {
        int i = 0;
        while (i < oneUseFields.size()) {
            Feld f = oneUseFields.get(i);
            if (f.isBelegt()) {
                f.clearOneUseFeld();
                oneUseFields.remove(i);
            } else {
                i++;
            }
        }
    }
    @Override
    public List<Feld> getOneUseFields(Spiel spiel) {
        ArrayList<Feld> oneUseFelds = new ArrayList<>();
        Feld fg12 = spiel.getFeldPilot(FeldName.FAHRGESTELL_1_2);
        Feld fg34 = spiel.getFeldPilot(FeldName.FAHRGESTELL_3_4);
        Feld fg56 = spiel.getFeldPilot(FeldName.FAHRGESTELL_5_6);
        Feld b2 = spiel.getFeldPilot(FeldName.BREMSEN_2);
        Feld b4 = spiel.getFeldPilot(FeldName.BREMSEN_4);
        Feld b6 = spiel.getFeldPilot(FeldName.BREMSEN_6);
        Feld k12 = spiel.getFeldCopilot(FeldName.KLAPPEN_1_2);
        Feld k23 = spiel.getFeldCopilot(FeldName.KLAPPEN_2_3);
        Feld k45 = spiel.getFeldCopilot(FeldName.KLAPPEN_4_5);
        Feld k56 = spiel.getFeldCopilot(FeldName.KLAPPEN_5_6);
        oneUseFelds.add(fg12);
        oneUseFelds.add(fg34);
        oneUseFelds.add(fg56);
        oneUseFelds.add(b2);
        oneUseFelds.add(b4);
        oneUseFelds.add(b6);
        oneUseFelds.add(k12);
        oneUseFelds.add(k23);
        oneUseFelds.add(k45);
        oneUseFelds.add(k56);
        return oneUseFelds;
    }

    @Override
    public void clearKaffeeFields(Spiel spiel, int kaffeeTokenCount) {
        int kaffeeCount = spiel.getKaffeeTokenCount();
        switch (kaffeeCount) {
            case 0:
                spiel.getFeldPilot(FeldName.KAFFEE_1).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_1).setWuerfel(null);
                spiel.getFeldPilot(FeldName.KAFFEE_2).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_2).setWuerfel(null);
                spiel.getFeldPilot(FeldName.KAFFEE_3).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_3).setWuerfel(null);
                break;
            case 1:
                spiel.getFeldPilot(FeldName.KAFFEE_2).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_2).setWuerfel(null);
                spiel.getFeldPilot(FeldName.KAFFEE_3).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_3).setWuerfel(null);
                break;
            case 2:
                spiel.getFeldPilot(FeldName.KAFFEE_3).setWuerfel(null);
                spiel.getFeldCopilot(FeldName.KAFFEE_3).setWuerfel(null);
                break;
        }
    }


}
