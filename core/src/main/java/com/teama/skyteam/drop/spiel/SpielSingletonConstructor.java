package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.flugzeug.Flugzeug;
import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.szenario.Route;


//Erzeugt Spiel-Objekt
public class SpielSingletonConstructor {
    private static Spiel spiel; //Nur ein Objekt ist möglich

    private SpielSingletonConstructor() {
    }

    public static Spiel createSpiel(Route route) {
        if (spiel == null) {
            spiel = new Spiel();
            spiel.setFlugzeug(new Flugzeug());

            Pilot pilot = new Pilot(spiel);
            spiel.setPilot(pilot);

            Copilot copilot = new Copilot(spiel);
            spiel.setCopilot(copilot);

            spiel.setPilotFelds();
            spiel.setCopilotFelds();

            spiel.setSzenario(route);

            spiel.updateCurrentField();
            spiel.updateCurrentHoehe();

            spiel.setRouteSzenario();
            spiel.setHoeheSzenario();
        }
        return spiel;
    }
}
