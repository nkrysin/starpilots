/**
 * Verwaltert die Eigenschaften und Zustände eines Flugzeugs im Game.
 *
 * Die Klasse `Plane` enthält grundlegende Attribute wie die aktuelle Geschwindigkeit, die Neigung,
 * den Zustand der Brakes, der Flaps und des Fahrgestells. Sie stellt Methoden zur Verwaltung und
 * Aktualisierung dieser Eigenschaften zur Verfügung, die im Game verwendet werden, um das Plane
 * zu steuern und zu verändern.
 */
package com.teama.skyteam.drop.domain.model.plane;

import com.teama.skyteam.drop.domain.model.tokens.Dice;

public class Plane {
    private int currentSpeed;
    private Speed speedLowerLimit;
    private Speed speedUpperLimit;
    private int tilt;
    private Brakes brakesState;
    private Flaps flapsState;
    private LandingGear landingGearState;


    /**
     * Konstruktor für die Klasse Plane.
     *
     * Initialisiert ein neues Plane-Objekt entsprechend den Spielregeln:
     * - Die aktuelle Geschwindigkeit wird auf 0 gesetzt.
     * - Die untere Geschwindigkeitsgrenze wird auf Speed.FOUR gesetzt.
     * - Die obere Geschwindigkeitsgrenze wird auf Speed.EIGHT gesetzt.
     * - Die Neigung wird auf 0 gesetzt.
     * - Der Bremsenzustand wird auf Brakes.NONE gesetzt.
     * - Der Klappenzustand wird auf Flaps.NONE gesetzt.
     * - Der Fahrgestellzustand wird auf ein neues LandingGear-Objekt gesetzt.
     */
    public Plane() {
        setCurrentSpeed(0);
        setSpeedLowerLimit(Speed.FOUR);
        setSpeedUpperLimit(Speed.EIGHT);
        setTilt(0);
        setBremsenZustand(Brakes.NONE);
        setFlapsState(Flaps.NONE);
        setLandingGearState(new LandingGear());
    }

    /*Getters and Setters*/
    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public Speed getSpeedLowerLimit() {
        return speedLowerLimit;
    }

    void setSpeedLowerLimit(Speed speedLowerLimit) {
        this.speedLowerLimit = speedLowerLimit;
    }

    public Speed getSpeedUpperLimit() {
        return speedUpperLimit;
    }

    void setSpeedUpperLimit(Speed speedUpperLimit) {
        this.speedUpperLimit = speedUpperLimit;
    }

    public int getTilt() {
        return tilt;
    }

    void setTilt(int tilt) {
        this.tilt = tilt;
    }


    public Brakes getBrakesState() {
        return brakesState;
    }

    void setBremsenZustand(Brakes brakesState) {
        this.brakesState = brakesState;
    }

    public Flaps getFlapsState() {
        return flapsState;
    }

    void setFlapsState(Flaps FlapsState) {
        this.flapsState = FlapsState;
    }

    public LandingGear getLandingGearState() {
        return landingGearState;
    }

    void setLandingGearState(LandingGear landingGearState) {
        this.landingGearState = landingGearState;
    }


    /**
     * Subtrahiert die Werte zweier Würfel.
     *
     * @param dice ein Array von zwei Würfeln, deren Werte subtrahiert werden.
     * @return die Differenz zwischen den Werten der beiden Würfel.
     * @throws IllegalArgumentException wenn das Array null ist oder nicht genau zwei Würfel enthält.
     */
    private int subtractDiceValues(Dice[] dice) {
        if (dice == null || dice.length != 2) {
            throw new IllegalArgumentException("Falsche Anzahl von Würfeln wurden gegeben");
        }
        int sub = dice[0].getValue() - dice[1].getValue();
        return sub;
    }

    /**
     * Ändert die Neigung des Flugzeugs basierend auf einem gegebenen Wert und den Würfelwerten.
     *
     * @param tilt die Basisneigung, die geändert werden soll.
     * @param dice ein Array von zwei Würfeln, deren Wertunterschied zur Neigung addiert wird.
     */
    public void tilt(int tilt, Dice[] dice) {
        int sub = subtractDiceValues(dice);
        setTilt(tilt + sub);
    }

    /**
     * Aktiviert die Brakes basierend auf dem Wert eines Würfels.
     *
     * @param dice ein Würfel, dessen Wert die nächste Bremsstufe bestimmt.
     * @return true, wenn der Bremszustand erfolgreich geändert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean bremsen(Dice dice) {
        int value = dice.getValue();
        Brakes aktuellerZustand = getBrakesState();

        switch (aktuellerZustand) {
            case NONE:
                if (value == 2) {
                    setBremsenZustand(Brakes.LEVEL1);
                    return true;
                }
                break;
            case LEVEL1:
                if (value == 4) {
                    setBremsenZustand(Brakes.LEVEL2);
                    return true;
                }
                break;
            case LEVEL2:
                if (value == 6) {
                    setBremsenZustand(Brakes.LEVEL3);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Öffnet die Flaps basierend auf dem Wert eines Würfels.
     *
     * @param dice ein Würfel, dessen Wert die nächste Klappenstufe bestimmt.
     * @return true, wenn der Klappenzustand erfolgreich geändert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean deployFlaps(Dice dice) {
        int value = dice.getValue();
        Flaps currentState = getFlapsState();

        switch (currentState) {
            case NONE:
                if (value == 1 || value == 2) {
                    setFlapsState(Flaps.LEVEL1);
                    setSpeedUpperLimit(Speed.NINE);
                    return true;
                }
                break;
            case LEVEL1:
                if (value == 2 || value == 3) {
                    setFlapsState(Flaps.LEVEL2);
                    setSpeedUpperLimit(Speed.TEN);
                    return true;
                }
                break;
            case LEVEL2:
                if (value == 4 || value == 5) {
                    setFlapsState(Flaps.LEVEL3);
                    setSpeedUpperLimit(Speed.ELEVEN);
                    return true;
                }
                break;
            case LEVEL3:
                if (value == 5 || value == 6) {
                    setFlapsState(Flaps.LEVEL4);
                    setSpeedUpperLimit(Speed.TWELVE);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Aktiviert ein bestimmtes LandingGear basierend auf dem Wert eines Würfels.
     *
     * @param dice ein Würfel, dessen Wert das zu aktivierende LandingGear bestimmt.
     * @return true, wenn das LandingGear erfolgreich aktiviert wurde; false, wenn keine Änderung möglich war.
     */
    public boolean deployLandingGear(Dice dice) {
        int value = dice.getValue();
        switch (value) {
            case 1, 2:
                getLandingGearState().activateLandingGear(1);
                updateSpeedLowerLimit();
                return true;
            case 3, 4:
                getLandingGearState().activateLandingGear(2);
                updateSpeedLowerLimit();
                return true;
            case 5, 6:
                getLandingGearState().activateLandingGear(3);
                updateSpeedLowerLimit();
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * Aktualisiert die untere Geschwindigkeitsgrenze basierend auf der Anzahl der aktivierten Fahrgestelle.
     */
    private void updateSpeedLowerLimit() {
        int akrivierteFahrgestelle = getLandingGearState().getActivatedLandingGearCount();
        switch (akrivierteFahrgestelle) {
            case 1 -> setSpeedLowerLimit(Speed.FIVE);
            case 2 -> setSpeedLowerLimit(Speed.SIX);
            case 3 -> setSpeedLowerLimit(Speed.SEVEN);
        }
    }
}






