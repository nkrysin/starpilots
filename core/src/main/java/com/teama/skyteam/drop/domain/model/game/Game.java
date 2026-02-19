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
import com.teama.skyteam.drop.domain.model.plane.Plane;
import com.teama.skyteam.drop.domain.model.player.Copilot;
import com.teama.skyteam.drop.domain.model.player.Pilot;
import com.teama.skyteam.drop.domain.model.tokens.CoffeeToken;
import com.teama.skyteam.drop.domain.model.tokens.RerollToken;

import java.util.*;

public class Game {
    private Pilot pilot;
    private Copilot copilot;
    private EnumMap<FieldName, Field> pilotFields;
    private EnumMap<FieldName, Field> copilotFuelds;
    private RouteField currentField;
    private int position = 0;
    private Altitude currentAltitude;
    private List<Altitude> altitudeCampaign;
    private TreeMap<Integer, RouteField> routeCampaign;
    private Plane plane;
    private int round = 1;
    private boolean endCondition = false;
    private boolean collision = false;
    private ArrayList<CoffeeToken> coffeeTokens = new ArrayList<CoffeeToken>();
    private ArrayList<RerollToken> rerollToken = new ArrayList<RerollToken>();

    private int currentSpeed;
    private Campaign campaign;

    /**
     * Prüft, ob eine Kollision im Game aufgetreten ist.
     *
     * @return `true`, wenn eine Kollision aufgetreten ist, sonst `false`.
     */
    public boolean isCollision() {
        return collision;
    }

    /**
     * Setzt den Kollisionsstatus des Spiels.
     *
     * @param collision `true`, um eine Kollision anzuzeigen, sonst `false`.
     */
    public void setCollision(boolean collision) {
        this.collision = collision;
    }


    /**
     * Gibt die Liste aller Kaffee-Tokens zurück.
     *
     * @return Eine Liste der Kaffee-Tokens.
     */
    public ArrayList<CoffeeToken> getCoffeeTokens() {
        return coffeeTokens;
    }

    /**
     * Gibt die Anzahl der verfügbaren Kaffee-Tokens zurück.
     *
     * @return Die Anzahl der Kaffee-Tokens.
     */
    public int getCoffeeTokenCount() {
        return coffeeTokens.size();
    }

    /**
     * Erhöht die Anzahl der Kaffee-Tokens durch Hinzufügen eines neuen Tokens.
     *
     * @param token Das hinzuzufügende Kaffee-Token.
     */
    public void incrementCoffeeTokenCount(CoffeeToken token) {
        coffeeTokens.add(token);
    }

    /**
     * Gibt die Liste aller NeuWurf-Tokens zurück.
     *
     * @return Eine Liste der NeuWurf-Tokens.
     */
    public ArrayList<RerollToken> getRerollTokens() {
        return rerollToken;
    }

    /**
     * Gibt die Anzahl der verfügbaren NeuWurf-Tokens zurück.
     *
     * @return Die Anzahl der NeuWurf-Tokens.
     */
    public int getRerollTokenCount() {
        return rerollToken.size();
    }

    /**
     * Erhöht die Anzahl der NeuWurf-Tokens durch Hinzufügen eines neuen Tokens.
     */
    public void incrementRerollTokenCount() {
        rerollToken.add(new RerollToken());
    }

    /**
     * Initialisiert die Spielfelder für den Piloten
     */
    void setPilotFields() {
        EnumMap<FieldName, Field> pilotFields = new EnumMap<>(FieldName.class);
        pilotFields.put(FieldName.RADIO_PILOT, new Field());
        pilotFields.put(FieldName.TILT, new Field());
        pilotFields.put(FieldName.SPEED, new Field());
        pilotFields.put(FieldName.LANDING_GEAR_1_2, new Field());
        pilotFields.put(FieldName.LANDING_GEAR_3_4, new Field());
        pilotFields.put(FieldName.LANDING_GEAR_5_6, new Field());
        pilotFields.put(FieldName.BRAKES_2, new Field());
        pilotFields.put(FieldName.BRAKES_4, new Field());
        pilotFields.put(FieldName.BRAKES_6, new Field());
        pilotFields.put(FieldName.COFFEE_1, new Field());
        pilotFields.put(FieldName.COFFEE_2, new Field());
        pilotFields.put(FieldName.COFFEE_3, new Field());
        this.pilotFields = pilotFields;

    }

    /**
     * Initialisiert die Spielfelder für den Copiloten
     */
    void setCopilotFields() {
        EnumMap<FieldName, Field> copilotFields = new EnumMap<>(FieldName.class);
        copilotFields.put(FieldName.RADIO_COPILOT_1, new Field());
        copilotFields.put(FieldName.RADIO_COPILOT_2, new Field());
        copilotFields.put(FieldName.TILT, new Field());
        copilotFields.put(FieldName.SPEED, new Field());
        copilotFields.put(FieldName.FLIPS_1_2, new Field());
        copilotFields.put(FieldName.FLIPS_2_3, new Field());
        copilotFields.put(FieldName.FLIPS_4_5, new Field());
        copilotFields.put(FieldName.FLIPS_5_6, new Field());
        copilotFields.put(FieldName.COFFEE_1, new Field());
        copilotFields.put(FieldName.COFFEE_2, new Field());
        copilotFields.put(FieldName.COFFEE_3, new Field());
        this.copilotFuelds = copilotFields;
    }

    /**
     * Gibt ein bestimmtes Field des Piloten basierend auf seinem Namen zurück.
     *
     * @param key Der Name des Spielfeldes.
     * @return Das entsprechende Spielfeld des Piloten.
     */
    public Field getFieldPilot(FieldName key) {
        return pilotFields.get(key);
    }

    /**
     * Gibt ein bestimmtes Field des Copiloten basierend auf seinem Namen zurück.
     *
     * @param key Der Name des Spielfeldes.
     * @return Das entsprechende Spielfeld des Copiloten.
     */
    public Field getFieldCopilot(FieldName key) {
        return copilotFuelds.get(key);
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
    void updateCurrentAltitude() {
        this.currentAltitude = getCurrentAltitude();
    }

    /**
     * Gibt die aktuelle Flughöhe zurück.
     *
     * @return Die aktuelle Flughöhe.
     */
    public Altitude getCurrentAltitude() {
        return campaign.getCurrentAltitude();
    }

    /**
     * Initialisiert das Routen-Campaign basierend auf dem aktuellen Campaign.
     */
    void setRouteCampaign() {
        this.routeCampaign = getRouteCampaign();
    }

    /**
     * Gibt die aktuelle Route des Szenarios zurück.
     *
     * @return Die Route des Szenarios als TreeMap.
     */
    public TreeMap<Integer, RouteField> getRouteCampaign() {
        return campaign.getCurrentRoute();
    }

    /**
     * Initialisiert das Höhen-Campaign basierend auf dem aktuellen Campaign.
     */
    void setAltitudeCampaign() {
        this.altitudeCampaign = getAltitudeCampaign();
    }

    /**
     * Gibt die Liste der Höhen-Szenarien zurück.
     *
     * @return Eine Liste der Höhen-Szenarien.
     */
    public List<Altitude> getAltitudeCampaign() {
        return campaign.getAltitudeCampaign();
    }

    /**
     * Gibt die aktuelle Runde des Spiels zurück.
     *
     * @return Die aktuelle Runde.
     */
    public int getRound() {
        return round;
    }


    /**
     * Setzt die aktuelle Runde des Spiels.
     *
     * @param round Die neue Runde.
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * Gibt das aktuelle Campaign des Spiels zurück.
     *
     * @return Das aktuelle Campaign.
     */
    public Campaign getCampaign() {
        return campaign;
    }


    void setCampaign(Campaign campaign) {
        this.campaign = campaign;

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
    public Plane getPlane() {
        return plane;
    }

    /**
     * Setzt das Plane des Spiels.
     *
     * @param plane Das neue Plane.
     */
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    /**
     * Setzt die Endbedingung des Spiels.
     *
     * @param endCondition `true`, wenn die Endbedingung erreicht wurde, sonst `false`.
     */
    public void setEndCondition(boolean endCondition) {
        this.endCondition = endCondition;
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
