package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.flugzeug.*;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.szenario.Route;


//Erzeugt Spiel-Objekt
public class SpielFactory {
    private static Spiel spiel; //Nur ein Objekt ist möglich

    private SpielFactory() { //Keine weitere Spiele können erzeugt werden
    }


    public static Spiel createSpiel() {
        if (spiel == null) {
            spiel = new Spiel();
            spiel.setFlugzeug(new Flugzeug());


            Pilot pilot = new Pilot(spiel);
            spiel.setPilot(pilot);


            Copilot copilot = new Copilot(spiel);
            spiel.setCopilot(copilot);

            spiel.setPilotFelds();
            spiel.setCopilotFelds();


            /*
             * Logik wie man kann eine andere Szenario auswählen
             * */
            /*
             * Loading von assets durch getHoeheSzenarioType + etwas wie getSzenarioName
             */
            //TODO change this
            Route route1 = Route.MONTREAL_TRUDEAU;
            spiel.setSzenario(route1);

            spiel.updateCurrentField(); //Erster Feld von Szenario
            spiel.updateCurrentHoehe(); //Erseter Feld no Hoehe

            spiel.setRouteSzenario(); //Feldern von Szenario
            spiel.setHoeheSzenario(); //Felder von Hoehe



        }
        return spiel;
    }
}
