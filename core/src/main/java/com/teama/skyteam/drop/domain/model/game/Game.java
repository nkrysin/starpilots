/**
 * Die Hauptklasse, die alle Komponenten des Spiels zusammenführt.
 *
 * Die Klasse `Game` integriert alle wichtigen Spielkomponenten wie Player, Plane, Szenarien,
 * Spielfelder und Token, um sie später im Controller weiterzuverarbeiten.
 * Sie stellt grundlegende Methoden zur Verwaltung und Aktualisierung der Spielzustände bereit,
 * die für die Steuerung des Spiels benötigt werden.
 */
package com.teama.skyteam.drop.domain.model.game;

import com.teama.skyteam.drop.domain.model.campaign.Campaign;
import com.teama.skyteam.drop.domain.model.campaign.RouteField;
import com.teama.skyteam.drop.domain.model.campaign.altitude.Altitude;
import com.teama.skyteam.drop.domain.model.field.FieldName;
import com.teama.skyteam.drop.domain.model.field.Field;
import com.teama.skyteam.drop.domain.model.flugzeug.Plane;
import com.teama.skyteam.drop.domain.model.player.Copilot;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.campaign.Route;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.RerollToken;

import java.io.IOException;
import java.util.*;

public class Game {
    private Pilot pilot;
    private Copilot copilot;
    private EnumMap<FieldName, Field> pilotFelds;
    private EnumMap<FieldName, Field> copilotFelds;
    private RouteField currentField;
    private int position = 0;
    private Altitude currentHoehe;
    private List<Altitude> hoeheSzenario;
    private TreeMap<Integer, RouteField> routeSzenario;
    private Plane plane;
    private int runde = 1;
    private boolean endBedingung = false;
    private boolean kollision = false;
    private ArrayList<CoffeeToken> coffeeTokens = new ArrayList<CoffeeToken>();
    private ArrayList<RerollToken> rerollToken = new ArrayList<RerollToken>();

    private int currentSpeed;
    private Campaign campaign;

    /**
     * Prüft, ob eine Kollision im Game aufgetreten ist.
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
    public void setKollision(boolean kollision) {
        this.kollision = kollision;
    }


    /**
     * Gibt die Liste aller Kaffee-Tokens zurück.
     *
     * @return Eine Liste der Kaffee-Tokens.
     */
    public ArrayList<CoffeeToken> getKaffeeTokens() {
        return coffeeTokens;
    }

    /**
     * Gibt die Anzahl der verfügbaren Kaffee-Tokens zurück.
     *
     * @return Die Anzahl der Kaffee-Tokens.
     */
    public int getKaffeeTokenCount() {
        return coffeeTokens.size();
    }

    /**
     * Erhöht die Anzahl der Kaffee-Tokens durch Hinzufügen eines neuen Tokens.
     *
     * @param token Das hinzuzufügende Kaffee-Token.
     */
    public void kaffeeTokenAnzahlErhoehen(CoffeeToken token) {
        coffeeTokens.add(token);
    }

    /**
     * Gibt die Liste aller NeuWurf-Tokens zurück.
     *
     * @return Eine Liste der NeuWurf-Tokens.
     */
    public ArrayList<RerollToken> getNeuWurfTokens() {
        return rerollToken;
    }

    /**
     * Gibt die Anzahl der verfügbaren NeuWurf-Tokens zurück.
     *
     * @return Die Anzahl der NeuWurf-Tokens.
     */
    public int getNeuWurfTokenCount() {
        return rerollToken.size();
    }

    /**
     * Erhöht die Anzahl der NeuWurf-Tokens durch Hinzufügen eines neuen Tokens.
     */
    public void neuWurfTokenAnzahlErhoehen() {
        rerollToken.add(new RerollToken());
    }

    /**
     * Initialisiert die Spielfelder für den Piloten
     */
    void setPilotFelds() {
        EnumMap<FieldName, Field> pilotFelds = new EnumMap<>(FieldName.class);
        pilotFelds.put(FieldName.RADIO_PILOT, new Field());
        pilotFelds.put(FieldName.NEIGEN, new Field());
        pilotFelds.put(FieldName.SPEED, new Field());
        pilotFelds.put(FieldName.FAHRGESTELL_1_2, new Field());
        pilotFelds.put(FieldName.FAHRGESTELL_3_4, new Field());
        pilotFelds.put(FieldName.FAHRGESTELL_5_6, new Field());
        pilotFelds.put(FieldName.BREMSEN_2, new Field());
        pilotFelds.put(FieldName.BREMSEN_4, new Field());
        pilotFelds.put(FieldName.BREMSEN_6, new Field());
        pilotFelds.put(FieldName.KAFFEE_1, new Field());
        pilotFelds.put(FieldName.KAFFEE_2, new Field());
        pilotFelds.put(FieldName.KAFFEE_3, new Field());
        this.pilotFelds = pilotFelds;

    }

    /**
     * Initialisiert die Spielfelder für den Copiloten
     */
    void setCopilotFelds() {
        EnumMap<FieldName, Field> copilotFelds = new EnumMap<>(FieldName.class);
        copilotFelds.put(FieldName.RADIO_COPILOT_1, new Field());
        copilotFelds.put(FieldName.RADIO_COPILOT_2, new Field());
        copilotFelds.put(FieldName.NEIGEN, new Field());
        copilotFelds.put(FieldName.SPEED, new Field());
        copilotFelds.put(FieldName.KLAPPEN_1_2, new Field());
        copilotFelds.put(FieldName.KLAPPEN_2_3, new Field());
        copilotFelds.put(FieldName.KLAPPEN_4_5, new Field());
        copilotFelds.put(FieldName.KLAPPEN_5_6, new Field());
        copilotFelds.put(FieldName.KAFFEE_1, new Field());
        copilotFelds.put(FieldName.KAFFEE_2, new Field());
        copilotFelds.put(FieldName.KAFFEE_3, new Field());
        this.copilotFelds = copilotFelds;
    }

    /**
     * Gibt ein bestimmtes Field des Piloten basierend auf seinem Namen zurück.
     *
     * @param key Der Name des Spielfeldes.
     * @return Das entsprechende Spielfeld des Piloten.
     */
    public Field getFeldPilot(FieldName key) {
        return pilotFelds.get(key);
    }

    /**
     * Gibt ein bestimmtes Field des Copiloten basierend auf seinem Namen zurück.
     *
     * @param key Der Name des Spielfeldes.
     * @return Das entsprechende Spielfeld des Copiloten.
     */
    public Field getFeldCopilot(FieldName key) {
        return copilotFelds.get(key);
    }


    /**
     * Aktualisiert das aktuelle Reisefeld basierend auf der aktuellen Position.
     */
    public void updateCurrentField() {
        this.currentField = getCurrentField();
    }

    /**
     * Gibt das aktuelle Reisefeld zurück.
     *
     * @return Das aktuelle Reisefeld.
     */
    public RouteField getCurrentField() {
        return campaign.getCurrentField(getCurrentPosition());
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
     * Aktualisiert die aktuelle Flughöhe basierend auf dem Campaign.
     */
    void updateCurrentHoehe() {
        this.currentHoehe = getCurrentHoehe();
    }

    /**
     * Gibt die aktuelle Flughöhe zurück.
     *
     * @return Die aktuelle Flughöhe.
     */
    public Altitude getCurrentHoehe() {
        return campaign.getCurrentFlughoehe();
    }

    /**
     * Initialisiert das Routen-Campaign basierend auf dem aktuellen Campaign.
     */
    void setRouteSzenario() {
        this.routeSzenario = getRouteSzenario();
    }

    /**
     * Gibt die aktuelle Route des Szenarios zurück.
     *
     * @return Die Route des Szenarios als TreeMap.
     */
    public TreeMap<Integer, RouteField> getRouteSzenario() {
        return campaign.getCurrentRoute();
    }

    /**
     * Initialisiert das Höhen-Campaign basierend auf dem aktuellen Campaign.
     */
    void setHoeheSzenario() {
        this.hoeheSzenario = getHoeheSzenario();
    }

    /**
     * Gibt die Liste der Höhen-Szenarien zurück.
     *
     * @return Eine Liste der Höhen-Szenarien.
     */
    public List<Altitude> getHoeheSzenario() {
        return campaign.getHoeheSzenario();
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
     * Gibt das aktuelle Campaign des Spiels zurück.
     *
     * @return Das aktuelle Campaign.
     */
    public Campaign getSzenario() {
        return campaign;
    }

    /**
     * Setzt das Campaign basierend auf einer bestimmten Route.
     *
     * @param route Die Route, die für das Campaign verwendet werden soll.
     */
    void setSzenario(Route route) {
        try {
            switch (route) {
                case HANEDA -> this.campaign = new Campaign(Route.HANEDA);
                case MONTREAL_TRUDEAU -> this.campaign = new Campaign(Route.MONTREAL_TRUDEAU);
                case KUALA_LUMPUR -> this.campaign = new Campaign(Route.KUALA_LUMPUR);
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
     * Gibt das Plane des Spiels zurück.
     *
     * @return Das aktuelle Plane.
     */
    public Plane getFlugzeug() {
        return plane;
    }

    /**
     * Setzt das Plane des Spiels.
     *
     * @param plane Das neue Plane.
     */
    public void setFlugzeug(Plane plane) {
        this.plane = plane;
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
    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }
}
