/**
 * Die Hauptklasse, die alle Komponenten des Spiels zusammenführt.
 *
 * Die Klasse `Spiel` integriert alle wichtigen Spielkomponenten wie Spieler, Flugzeug, Szenarien,
 * Spielfelder und Token, um sie später im Controller weiterzuverarbeiten.
 * Sie stellt grundlegende Methoden zur Verwaltung und Aktualisierung der Spielzustände bereit,
 * die für die Steuerung des Spiels benötigt werden.
 */
package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.feld.FeldName;
import com.teama.skyteam.drop.flugzeug.Flugzeug;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.feld.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.szenario.ReiseFeld;
import com.teama.skyteam.drop.szenario.Route;
import com.teama.skyteam.drop.szenario.Szenario;
import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;
import com.teama.skyteam.drop.tokens.KaffeeToken;
import com.teama.skyteam.drop.tokens.NeuWurfToken;

import java.io.IOException;
import java.util.*;

public class Spiel {
    private Pilot pilot;
    private Copilot copilot;
    private EnumMap<FeldName, Feld> pilotFelds;
    private EnumMap<FeldName, Feld> copilotFelds;
    private ReiseFeld currentField;
    private int position = 0;
    private Flughoehe currentHoehe;
    private List<Flughoehe> hoeheSzenario;
    private TreeMap<Integer, ReiseFeld> routeSzenario;
    private Flugzeug flugzeug;
    private int runde = 1;
    private boolean endBedingung = false;
    private boolean kollision = false;
    private ArrayList<KaffeeToken> kaffeeTokens = new ArrayList<KaffeeToken>();
    private ArrayList<NeuWurfToken> neuWurfToken = new ArrayList<NeuWurfToken>();

    private int currentSpeed;
    private Szenario szenario;

    /**
     * Prüft, ob eine Kollision im Spiel aufgetreten ist.
     *
     * @return `true`, wenn eine Kollision aufgetreten ist, sonst `false`.
     */
    public boolean isKollision() {
        return kollision;
    }

    /**
     * Setzt den Kollisionsstatus des Spiels.
     *
     * @param kollision `true`, um eine Kollision anzuzeigen, sonst `false`.
     */
    void setKollision(boolean kollision) {
        this.kollision = kollision;
    }


    /**
     * Gibt die Liste aller Kaffee-Tokens zurück.
     *
     * @return Eine Liste der Kaffee-Tokens.
     */
    public ArrayList<KaffeeToken> getKaffeeTokens() {
        return kaffeeTokens;
    }

    /**
     * Gibt die Anzahl der verfügbaren Kaffee-Tokens zurück.
     *
     * @return Die Anzahl der Kaffee-Tokens.
     */
    public int getKaffeeTokenCount() {
        return kaffeeTokens.size();
    }

    /**
     * Erhöht die Anzahl der Kaffee-Tokens durch Hinzufügen eines neuen Tokens.
     *
     * @param token Das hinzuzufügende Kaffee-Token.
     */
    public void kaffeeTokenAnzahlErhoehen(KaffeeToken token) {
        kaffeeTokens.add(token);
    }

    /**
     * Gibt die Liste aller NeuWurf-Tokens zurück.
     *
     * @return Eine Liste der NeuWurf-Tokens.
     */
    public ArrayList<NeuWurfToken> getNeuWurfTokens() {
        return neuWurfToken;
    }

    /**
     * Gibt die Anzahl der verfügbaren NeuWurf-Tokens zurück.
     *
     * @return Die Anzahl der NeuWurf-Tokens.
     */
    public int getNeuWurfTokenCount() {
        return neuWurfToken.size();
    }

    /**
     * Erhöht die Anzahl der NeuWurf-Tokens durch Hinzufügen eines neuen Tokens.
     */
    void neuWurfTokenAnzahlErhoehen() {
        neuWurfToken.add(new NeuWurfToken());
    }

    /**
     * Initialisiert die Spielfelder für den Piloten
     */
    void setPilotFelds() {
        EnumMap<FeldName, Feld> pilotFelds = new EnumMap<>(FeldName.class);
        pilotFelds.put(FeldName.RADIO_PILOT, new Feld());
        pilotFelds.put(FeldName.NEIGEN, new Feld());
        pilotFelds.put(FeldName.SPEED, new Feld());
        pilotFelds.put(FeldName.FAHRGESTELL_1_2, new Feld());
        pilotFelds.put(FeldName.FAHRGESTELL_3_4, new Feld());
        pilotFelds.put(FeldName.FAHRGESTELL_5_6, new Feld());
        pilotFelds.put(FeldName.BREMSEN_2, new Feld());
        pilotFelds.put(FeldName.BREMSEN_4, new Feld());
        pilotFelds.put(FeldName.BREMSEN_6, new Feld());
        pilotFelds.put(FeldName.KAFFEE_1, new Feld());
        pilotFelds.put(FeldName.KAFFEE_2, new Feld());
        pilotFelds.put(FeldName.KAFFEE_3, new Feld());
        this.pilotFelds = pilotFelds;

    }

    /**
     * Initialisiert die Spielfelder für den Copiloten
     */
    void setCopilotFelds() {
        EnumMap<FeldName, Feld> copilotFelds = new EnumMap<>(FeldName.class);
        copilotFelds.put(FeldName.RADIO_COPILOT_1, new Feld());
        copilotFelds.put(FeldName.RADIO_COPILOT_2, new Feld());
        copilotFelds.put(FeldName.NEIGEN, new Feld());
        copilotFelds.put(FeldName.SPEED, new Feld());
        copilotFelds.put(FeldName.KLAPPEN_1_2, new Feld());
        copilotFelds.put(FeldName.KLAPPEN_2_3, new Feld());
        copilotFelds.put(FeldName.KLAPPEN_4_5, new Feld());
        copilotFelds.put(FeldName.KLAPPEN_5_6, new Feld());
        copilotFelds.put(FeldName.KAFFEE_1, new Feld());
        copilotFelds.put(FeldName.KAFFEE_2, new Feld());
        copilotFelds.put(FeldName.KAFFEE_3, new Feld());
        this.copilotFelds = copilotFelds;
    }

    /**
     * Gibt ein bestimmtes Feld des Piloten basierend auf seinem Namen zurück.
     *
     * @param key Der Name des Spielfeldes.
     * @return Das entsprechende Spielfeld des Piloten.
     */
    public Feld getFeldPilot(FeldName key) {
        return pilotFelds.get(key);
    }

    /**
     * Gibt ein bestimmtes Feld des Copiloten basierend auf seinem Namen zurück.
     *
     * @param key Der Name des Spielfeldes.
     * @return Das entsprechende Spielfeld des Copiloten.
     */
    public Feld getFeldCopilot(FeldName key) {
        return copilotFelds.get(key);
    }


    /**
     * Aktualisiert das aktuelle Reisefeld basierend auf der aktuellen Position.
     */
    void updateCurrentField() {
        this.currentField = getCurrentField();
    }

    /**
     * Gibt das aktuelle Reisefeld zurück.
     *
     * @return Das aktuelle Reisefeld.
     */
    public ReiseFeld getCurrentField() {
        return szenario.getCurrentField(getCurrentPosition());
    }


    /**
     * Gibt die aktuelle Position des Spiels zurück.
     *
     * @return Die aktuelle Position.
     */
    public int getCurrentPosition() {
        return position;
    }

    /**
     * Setzt die aktuelle Position des Spiels.
     *
     * @param position Die neue Position.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Aktualisiert die aktuelle Flughöhe basierend auf dem Szenario.
     */
    void updateCurrentHoehe() {
        this.currentHoehe = getCurrentHoehe();
    }

    /**
     * Gibt die aktuelle Flughöhe zurück.
     *
     * @return Die aktuelle Flughöhe.
     */
    public Flughoehe getCurrentHoehe() {
        return szenario.getCurrentFlughoehe();
    }

    /**
     * Initialisiert das Routen-Szenario basierend auf dem aktuellen Szenario.
     */
    void setRouteSzenario() {
        this.routeSzenario = getRouteSzenario();
    }

    /**
     * Gibt die aktuelle Route des Szenarios zurück.
     *
     * @return Die Route des Szenarios als TreeMap.
     */
    public TreeMap<Integer, ReiseFeld> getRouteSzenario() {
        return szenario.getCurrentRoute();
    }

    /**
     * Initialisiert das Höhen-Szenario basierend auf dem aktuellen Szenario.
     */
    void setHoeheSzenario() {
        this.hoeheSzenario = getHoeheSzenario();
    }

    /**
     * Gibt die Liste der Höhen-Szenarien zurück.
     *
     * @return Eine Liste der Höhen-Szenarien.
     */
    List<Flughoehe> getHoeheSzenario() {
        return szenario.getHoeheSzenario();
    }

    /**
     * Gibt die aktuelle Runde des Spiels zurück.
     *
     * @return Die aktuelle Runde.
     */
    public int getRunde() {
        return runde;
    }


    /**
     * Setzt die aktuelle Runde des Spiels.
     *
     * @param runde Die neue Runde.
     */
    public void setRunde(int runde) {
        this.runde = runde;
    }

    /**
     * Gibt das aktuelle Szenario des Spiels zurück.
     *
     * @return Das aktuelle Szenario.
     */
    public Szenario getSzenario() {
        return szenario;
    }

    /**
     * Setzt das Szenario basierend auf einer bestimmten Route.
     *
     * @param route Die Route, die für das Szenario verwendet werden soll.
     */
    void setSzenario(Route route) {
        try {
            switch (route) {
                case HANEDA -> this.szenario = new Szenario(Route.HANEDA);
                case MONTREAL_TRUDEAU -> this.szenario = new Szenario(Route.MONTREAL_TRUDEAU);
                case KUALA_LUMPUR -> this.szenario = new Szenario(Route.KUALA_LUMPUR);
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Erstellen des Szenarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gibt den Copiloten zurück.
     *
     * @return Der Copilot.
     */
    public Copilot getCopilot() {
        return copilot;
    }

    /**
     * Setzt den Copiloten des Spiels.
     *
     * @param copilot Der neue Copilot.
     */
    public void setCopilot(Copilot copilot) {
        this.copilot = copilot;
    }

    /**
     * Gibt den Piloten zurück.
     *
     * @return Der Pilot.
     */
    public Pilot getPilot() {
        return pilot;
    }

    /**
     * Setzt den Piloten des Spiels.
     *
     * @param pilot Der neue Pilot.
     */
    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    /**
     * Gibt das Flugzeug des Spiels zurück.
     *
     * @return Das aktuelle Flugzeug.
     */
    public Flugzeug getFlugzeug() {
        return flugzeug;
    }

    /**
     * Setzt das Flugzeug des Spiels.
     *
     * @param flugzeug Das neue Flugzeug.
     */
    public void setFlugzeug(Flugzeug flugzeug) {
        this.flugzeug = flugzeug;
    }

    /**
     * Setzt die Endbedingung des Spiels.
     *
     * @param endBedingung `true`, wenn die Endbedingung erreicht wurde, sonst `false`.
     */
    public void setEndBedingung(boolean endBedingung) {
        this.endBedingung = endBedingung;
    }

    /**
     * Setzt die aktuelle Geschwindigkeit des Spiels.
     *
     * @param currentSpeed Die neue Geschwindigkeit.
     */
    void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}
