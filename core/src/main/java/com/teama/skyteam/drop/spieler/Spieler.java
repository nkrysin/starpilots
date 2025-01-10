package com.teama.skyteam.drop.spieler;

import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.KaffeTokenAktion;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;

public abstract class Spieler {

    private ArrayList<Wuerfel> wuerfels =  new ArrayList<Wuerfel>(4);


    public Spieler(){

    }

    public ArrayList<Wuerfel> getWuerfels() {
        return wuerfels;
    }

    public void setWuerfels(ArrayList<Wuerfel> wuerfels) {
        this.wuerfels = wuerfels;
    }


    public int getAnzahlWuerfels() {
        return wuerfels.size();
    }


    public void neuWurfTokenBenutzen(NeuWurfToken token, Spieler target){
        target.setWuerfels(token.erneutWerfen(target));
    }

    public void neuWurfTokenBenutzen(NeuWurfToken token, Spieler target1, Spieler target2){
        target1.setWuerfels(token.erneutWerfen(target1));
        target2.setWuerfels(token.erneutWerfen(target2));
    }


    public void kaffeeTokenBenutzen(KaffeToken token, int indexWuerfel, KaffeTokenAktion aktion){
        Wuerfel wuerfel = wuerfels.get(indexWuerfel);
        if (aktion == KaffeTokenAktion.PLUS && wuerfel.getWert() == 6){
            throw new IllegalArgumentException("Wuerfelwert ist schon maximal");
        }
        else if (aktion == KaffeTokenAktion.MINUS && wuerfel.getWert() == 1){
            throw new IllegalArgumentException("Wuerfelwert ist schon minimal");
        }
        else {
            wuerfel = token.wertAendern(aktion, wuerfel);
            wuerfels.set(indexWuerfel, wuerfel);
        }
    }

    protected abstract void kaffeeWaehlen(Wuerfel wuerfel, int indexWuerfel, String kaffeeFeld);
    protected abstract void radioWaehlen(Wuerfel wuerfel, int indexWuerfel);
    protected abstract void speedWaehlen(Wuerfel wuerfel, int indexWuerfel);
    protected abstract void  neigenWaehlen(Wuerfel wuerfel, int indexWuerfel);

}
