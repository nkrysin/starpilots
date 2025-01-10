package com.teama.skyteam.drop.flugzeug;

import com.teama.skyteam.drop.tokens.Wuerfel;

public class Flugzeug {
    private int speedAktuell;
    private Speed speedUntereGrenze;
    private Speed speedObereGrenze;
    private int neigung;
    private Bremsen bremsenZustand;
    private Klappen klappenZustand;
    private Fahrgestell fahrgestellZustand;


    /*Constructor*/

    public Flugzeug() {
        setSpeedAktuell(0);
        setSpeedUntereGrenze(Speed.FOUR);
        setSpeedObereGrenze(Speed.EIGHT);
        setNeigung(0);
        setBremsenZustand(Bremsen.NONE);
        setKlappenZustand(Klappen.NONE);
        setFahrgestellZustand(new Fahrgestell());

    }

    /*Getters and Setters*/
    public int getSpeedAktuell() {
        return speedAktuell;
    }

    public void setSpeedAktuell(int speedAktuell) {
        this.speedAktuell = speedAktuell;
    }

    public Speed getSpeedUntereGrenze() {
        return speedUntereGrenze;
    }

    void setSpeedUntereGrenze(Speed speedUntereGrenze) {
        this.speedUntereGrenze = speedUntereGrenze;
    }

    public Speed getSpeedObereGrenze() {
        return speedObereGrenze;
    }

    void setSpeedObereGrenze(Speed speedObereGrenze) {
        this.speedObereGrenze = speedObereGrenze;
    }

    public int getNeigung() {
        return neigung;
    }

    void setNeigung(int neigung) {
        this.neigung = neigung;
    }


    public Bremsen getBremsenZustand() {
        return bremsenZustand;
    }

    void setBremsenZustand(Bremsen bremsenZustand) {
        this.bremsenZustand = bremsenZustand;
    }

    public Klappen getKlappenZustand() {
        return klappenZustand;
    }

    void setKlappenZustand(Klappen klappenZustand) {
        this.klappenZustand = klappenZustand;
    }

    public Fahrgestell getFahrgestellZustand() {
        return fahrgestellZustand;
    }

    void setFahrgestellZustand(Fahrgestell fahrgestellZustand) {
        this.fahrgestellZustand = fahrgestellZustand;
    }


    /*Methods*/
    private int zweiWuerfelWertSubtrahieren(Wuerfel[] wuerfel) {
        if (wuerfel == null || wuerfel.length != 2) {
            throw new IllegalArgumentException("Falsche Anzahl von Würfeln wurden gegeben");
        }
        int sub = wuerfel[0].getWert() - wuerfel[1].getWert();
        return sub;
    }

    public void neigen(int neigung, Wuerfel[] wuerfel) {
        int sub = zweiWuerfelWertSubtrahieren(wuerfel);
        setNeigung(neigung + sub);
    }


    public boolean bremsen(Wuerfel wuerfel) {
        int wert = wuerfel.getWert();
        Bremsen aktuellerZustand = getBremsenZustand();

        switch (aktuellerZustand) {
            case NONE:
                if (wert == 2) {
                    setBremsenZustand(Bremsen.LEVEL1);
                    return true;
                }
                break;
            case LEVEL1:
                if (wert == 4) {
                    setBremsenZustand(Bremsen.LEVEL2);
                    return true;
                }
                break;
            case LEVEL2:
                if (wert == 6) {
                    setBremsenZustand(Bremsen.LEVEL3);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    public boolean klappenOeffnen(Wuerfel wuerfel) {
        int wert = wuerfel.getWert();
        Klappen aktuellerZustand = getKlappenZustand();

        switch (aktuellerZustand) {
            case NONE:
                if (wert == 1 || wert == 2) {
                    setKlappenZustand(Klappen.LEVEL1);
                    setSpeedObereGrenze(Speed.NINE);
                    return true;
                }
                break;
            case LEVEL1:
                if (wert == 2 || wert == 3) {
                    setKlappenZustand(Klappen.LEVEL2);
                    setSpeedObereGrenze(Speed.TEN);
                    return true;
                }
                break;
            case LEVEL2:
                if (wert == 4 || wert == 5) {
                    setKlappenZustand(Klappen.LEVEL3);
                    setSpeedObereGrenze(Speed.ELEVEN);
                    return true;
                }
                break;
            case LEVEL3:
                if (wert == 5 || wert == 6) {
                    setKlappenZustand(Klappen.LEVEL4);
                    setSpeedObereGrenze(Speed.TWELVE);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    public boolean fahrgestellAusfahren(Wuerfel wuerfel) {
        int wert = wuerfel.getWert();
        switch (wert) {
            case 1, 2:
                getFahrgestellZustand().fahrgestellAktivieren(1);
                speedUntereGrenzeAktualisieren();
                return true;
            case 3, 4:
                getFahrgestellZustand().fahrgestellAktivieren(2);
                speedUntereGrenzeAktualisieren();
                return true;
            case 5, 6:
                getFahrgestellZustand().fahrgestellAktivieren(3);
                speedUntereGrenzeAktualisieren();
                return true;
            default:
                break;
        }
        return false;
    }

    private void speedUntereGrenzeAktualisieren() {
        int akrivierteFahrgestelle = getFahrgestellZustand().getAkrivierteFahrgestelle();
        switch (akrivierteFahrgestelle) {
            case 1 -> setSpeedUntereGrenze(Speed.FIVE);
            case 2 -> setSpeedUntereGrenze(Speed.SIX);
            case 3 -> setSpeedUntereGrenze(Speed.SEVEN);
        }
    }
}






