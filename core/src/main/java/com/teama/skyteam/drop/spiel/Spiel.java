package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.flugzeug.Flugzeug;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.szenario.HoeheSzenarioTypes;
import com.teama.skyteam.drop.szenario.ReiseFeld;
import com.teama.skyteam.drop.szenario.Route;
import com.teama.skyteam.drop.szenario.Szenario;
import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.NeuWurfToken;

import java.io.IOException;
import java.util.*;

public class Spiel {
    private Pilot pilot;
    private Copilot copilot;
    private HashMap<String, Feld> pilotFelds;
    private HashMap<String, Feld> copilotFelds;
    private ReiseFeld currentField;
    private int position = 0;
    //TODO ОБЯЗАТЕЛЬНО присвоить сюда значение, чтобы потом отрисовывать его
    private HoeheSzenarioTypes hoeheSzenarioType;
    private Flughoehe currentHoehe;
    private List<Flughoehe> hoeheSzenario;
    private TreeMap<Integer, ReiseFeld> routeSzenario;
    private Flugzeug flugzeug;
    private int runde = 1;
    private boolean endBedingung = false;
    private boolean kollision = false;
    private ArrayList<KaffeToken> kaffeTokens = new ArrayList<KaffeToken>();
    private ArrayList<NeuWurfToken> neuWurfToken = new ArrayList<NeuWurfToken>();

    private int currentSpeed;


    boolean isKollision() {
        return kollision;
    }

    void setKollision(boolean kollision) {
        this.kollision = kollision;
    }

    public ArrayList<KaffeToken> getKaffeTokens() {
        return kaffeTokens;
    }

    public int getKaffeTokenCount(){
        return kaffeTokens.size();
    }

    public void kaffeTokenAnzahlErhoehen(KaffeToken token) {
        kaffeTokens.add(token);
    }



    public ArrayList<NeuWurfToken> getNeuWurfTokens() {
        return neuWurfToken;
    }

    public int getNeuWurfTokenCount(){
        return neuWurfToken.size();
    }

    void neuWurfTokenAnzahlErhoehen() {
        neuWurfToken.add(new NeuWurfToken());
    }




    void setPilotFelds(){
        HashMap<String, Feld> pilotFelds = new HashMap<>();
        pilotFelds.put("radioFeld", new Feld());
        pilotFelds.put("neigenFeld", new Feld());
        pilotFelds.put("speedFeld", new Feld());
        pilotFelds.put("fahrgestellFeld1_2", new Feld()); //drei neue Felder hinzugefügt,um Fahrgestell zu nutzen
        pilotFelds.put("fahrgestellFeld3_4", new Feld());
        pilotFelds.put("fahrgestellFeld5_6", new Feld());
        pilotFelds.put("bremsenFeld2", new Feld());
        pilotFelds.put("bremsenFeld4", new Feld());
        pilotFelds.put("bremsenFeld6", new Feld());
        pilotFelds.put("Kaffee1", new Feld());
        pilotFelds.put("Kaffee2", new Feld());
        pilotFelds.put("Kaffee3", new Feld());
        this.pilotFelds = pilotFelds;

    }

    void setCopilotFelds(){
        HashMap<String, Feld> copilotFelds = new HashMap<>();
        copilotFelds.put("radioFeld1", new Feld());
        copilotFelds.put("radioFeld2", new Feld());
        copilotFelds.put("neigenFeld", new Feld());
        copilotFelds.put("speedFeld", new Feld());
        copilotFelds.put("klappenFeld1_2", new Feld());
        copilotFelds.put("klappenFeld2_3", new Feld());
        copilotFelds.put("klappenFeld4_5", new Feld());
        copilotFelds.put("klappenFeld5_6", new Feld());
        copilotFelds.put("Kaffee1", new Feld());
        copilotFelds.put("Kaffee2", new Feld());
        copilotFelds.put("Kaffee3", new Feld());
        this.copilotFelds = copilotFelds;
    }

    public Feld getFeldPilot(String name) {
        return pilotFelds.get(name);
    }

    public Feld getFeldCopilot(String name) {
        return copilotFelds.get(name);
    }



    private Szenario szenario;

    void setSzenario(Route route) {
        try {
            //TODO Поменять это на нормальный
            /*
             *
             * Logik wie man das Szenario auf Bildschirm wählt
             *
             * */
            switch (route){
                case HANEDA ->
                        this.szenario = new Szenario(Route.HANEDA);
                case MONTREAL_TRUDEAU ->
                        this.szenario = new Szenario(Route.MONTREAL_TRUDEAU);
                case KUALA_LUMPUR ->
                        this.szenario = new Szenario(Route.KUALA_LUMPUR);
        }

        } catch (IOException e) {
            //TODO Поменять это на нормальный вывод без println
            System.err.println("Fehler beim Erstellen des Szenarios: " + e.getMessage());
            e.printStackTrace();
        }
    }




    void updateCurrentField(){
        this.currentField = getCurrentField();
    }

    ReiseFeld getCurrentField() {
        return szenario.getCurrentField(getCurrentPosition());
    }

    public int getCurrentPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    void updateCurrentHoehe(){
        this.currentHoehe = getCurrentHoehe();
    }
    Flughoehe getCurrentHoehe() {
        return szenario.getCurrentFlughoehe();
    }



    void setRouteSzenario(){
        this.routeSzenario = getRouteSzenario();
    }
    public TreeMap<Integer, ReiseFeld> getRouteSzenario() {
        return szenario.getCurrentRoute();
    }


    void setHoeheSzenario(){
        this.hoeheSzenario = getHoeheSzenario();
    }
    List<Flughoehe> getHoeheSzenario() {
        return szenario.getHoeheSzenario();
    }

    //TODO не забыть его использовать при отрисовке игры
    private HoeheSzenarioTypes getHoeheSzenarioType() {
        return szenario.getHoeheSzenarioType();
    }

    //TODO понять нужен ли он вообще тут
    public void setKaffeTokens(ArrayList<KaffeToken> kaffeTokens) {
        this.kaffeTokens = kaffeTokens;
    }


    public int getRunde() {
        return runde;
    }

    public void setRunde(int runde) {
        this.runde = runde;
    }

    //TODO не забыть его использовать при отрисовке игры
    public Szenario getSzenario() {
        return szenario;
    }

    public Copilot getCopilot() {
        return copilot;
    }

    public void setCopilot(Copilot copilot) {
        this.copilot = copilot;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public Flugzeug getFlugzeug() {
        return flugzeug;
    }

    public void setFlugzeug(Flugzeug flugzeug) {
        this.flugzeug = flugzeug;
    }
    //TODO понять нужен ли он вообще тут
    public boolean isEndBedingung() {
        return endBedingung;
    }

    public void setEndBedingung(boolean endBedingung) {
        this.endBedingung = endBedingung;
    }


    public int getCurrentSpeed() {
        return currentSpeed;
    }

    void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}
