package com.teama.skyteam.drop.spiel;

import com.teama.skyteam.drop.spieler.Copilot;
import com.teama.skyteam.drop.spieler.Feld;
import com.teama.skyteam.drop.spieler.Pilot;
import com.teama.skyteam.drop.spieler.Spieler;
import com.teama.skyteam.drop.tokens.KaffeToken;
import com.teama.skyteam.drop.tokens.KaffeTokenAktion;
import com.teama.skyteam.drop.tokens.NeuWurfToken;
import com.teama.skyteam.drop.tokens.Wuerfel;

import java.util.ArrayList;
import java.util.Scanner;

public class SpielTextGUI {

    /*
    private final Spiel spiel;
    private final SpielLogik spielLogik;

    public SpielTextGUI(Spiel spiel) {
        this.spiel = spiel;
        this.spielLogik = new SpielLogik(spiel);
    }

    //TODO в норм GUI надо переделать этот метод
    int wuerfelIndexWaehlen(Spieler spieler) { //passt nicht
        Scanner scanner = new Scanner(System.in);
        ArrayList<Wuerfel> wuerfels = spieler.getWuerfels();

        while (true) {
            System.out.println("Verfügbare Würfel zur Auswahl:");

            for (int i = 0; i < wuerfels.size(); i++) {
                System.out.println("[" + (i + 1) + "] - Wert: " + wuerfels.get(i).getWert());
            }

            System.out.print("Bitte wählen Sie einen Würfel (1-" + wuerfels.size() + "): ");

            int auswahl = scanner.nextInt();

            if (auswahl < 1 || auswahl > wuerfels.size()) {
                System.out.println("Ungültige Auswahl. Bitte wählen Sie erneut.");
                continue;
            }

            return (auswahl - 1);

            //Kann man auch try-catch, aber später wird das fest durch GUI realisiert, also braucht man das nicht
        }
    }
    //TODO в норм GUI надо переделать этот метод. Реализация будет похожей
    int actionWaehlen() {
        Scanner scanner = new Scanner(System.in);
        int anzahlNWT = spiel.getNeuWurfTokenCount();
        int anzahlKT = spiel.getKaffeTokenCount();

        while (true) {
            System.out.println("Bitte wählen:");

            System.out.println("[F] AUFS_SPIELFELD_SETZEN");
            if (anzahlNWT > 0) {
                System.out.println("[EW] ERNEUT_WERFEN");
            }
            if (anzahlKT > 0) {
                System.out.println("[K] KAFFE_BENUTZEN");
            }

            String input = scanner.nextLine().toUpperCase().trim();

            if (input.equals("F")) {
                return 1;
            } else if (input.equals("K") && anzahlKT > 0) {
                return 2;
            } else if (input.equals("EW") && anzahlNWT > 0) {
                return 3;
            } else {
                System.out.println("Bitte wiederholen Sie die Eingabe.");
            }
        }
    }

    //TODO в норм GUI надо переделать этот метод. Реализация будет похожей
    void pilotAufFeldSetzen() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Pilot, bitte wählen Sie eine Aktion:");
                System.out.println("[1] - Radio wählen");
                System.out.println("[2] - Geschwindigkeit wählen");
                System.out.println("[3] - Neigen wählen");
                System.out.println("[4] - Bremsen wählen");
                System.out.println("[5] - Fahrgestell wählen");
                System.out.println("[6] - Kaffee wählen");

                Pilot pilot = spiel.getPilot();

                int auswahl = scanner.nextInt();

                int indexWuerfel = wuerfelIndexWaehlen(pilot);
                Wuerfel gewaehlterWuerfel = pilot.getWuerfels().get(indexWuerfel);
                scanner.nextLine();

                //if(!spielLogik.pilotSchritt(spiel.getPilot(), auswahl, gewaehlterWuerfel, indexWuerfel)){
                    //pilotAufFeldSetzen();
                //}
                break;

            } catch (IllegalArgumentException e) {
                System.out.println("Fehler: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    //TODO в норм GUI надо переделать этот метод. Реализация будет похожей
    void copilotAufFeldSetzen() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Copilot, bitte wählen Sie eine Aktion:");
                System.out.println("[1] - Radio wählen");
                System.out.println("[2] - Geschwindigkeit wählen");
                System.out.println("[3] - Neigen wählen");
                System.out.println("[4] - Klappen wählen");
                System.out.println("[5] - Kaffee wählen");

                Copilot copilot = spiel.getCopilot();

                int auswahl = scanner.nextInt();

                int indexWuerfel = wuerfelIndexWaehlen(copilot);
                Wuerfel gewaehlterWuerfel = copilot.getWuerfels().get(indexWuerfel);
                scanner.nextLine();

               if (!spielLogik.copilotSchritt(spiel.getCopilot(), auswahl, gewaehlterWuerfel, indexWuerfel)){
                   copilotAufFeldSetzen();
               }
                break;

            } catch (IllegalArgumentException e) {
                System.out.println("Fehler: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    //TODO в норм GUI надо переделать этот метод. Реализация будет похожей
    void verpflicteteAktion(Spieler spieler) {
        Scanner scanner = new Scanner(System.in);

        Feld feldNeigung;
        Feld feldSpeed;
        if (spieler instanceof Pilot) {
            feldNeigung = spiel.getFeldPilot("neigenFeld");
            feldSpeed = spiel.getFeldPilot("speedFeld");
            spieler = spiel.getPilot();
        } else {
            feldNeigung = spiel.getFeldCopilot("neigenFeld");
            feldSpeed = spiel.getFeldCopilot("speedFeld");
            spieler = spiel.getCopilot();
        }
        if (!(feldSpeed.isBelegt()) && !(feldNeigung.isBelegt())) {
            System.out.println("Bitte setzen Sie die Würfel auf die Pflichtfelder");
            System.out.println("[1] - Geschwindigkeit wählen");
            System.out.println("[2] - Neigen wählen");

            int auswahl = scanner.nextInt();

            int indexWuerfel = wuerfelIndexWaehlen(spieler);
            Wuerfel gewaehlterWuerfel = spieler.getWuerfels().get(indexWuerfel);

            switch (auswahl) {
                case 1: // Geschwindigkeit wählen
                    spieler.aktionAusfuehren("speed", gewaehlterWuerfel, indexWuerfel);
                    System.out.println("Geschwindigkeit wurde erfolgreich gewählt.");
                    break;
                case 2: // Neigen wählen
                    spieler.aktionAusfuehren("neigen", gewaehlterWuerfel, indexWuerfel);
                    System.out.println("Neigen wurde erfolgreich gewählt.");
                    break;
            }
        } else if (!(feldSpeed.isBelegt())){
            System.out.println("Bitte setzen Sie die Würfel auf die Pflichtfelder");
            System.out.println("Sie können nur Geschwindigkeit wählen");

            int indexWuerfel = wuerfelIndexWaehlen(spieler);
            Wuerfel gewaehlterWuerfel = spieler.getWuerfels().get(indexWuerfel);
            spieler.aktionAusfuehren("speed", gewaehlterWuerfel, indexWuerfel);
            System.out.println("Geschwindigkeit wurde erfolgreich gewählt.");
        } else {
            System.out.println("Bitte setzen Sie die Würfel auf die Pflichtfelder");
            System.out.println("Sie können nur Neigen wählen");
            int indexWuerfel = wuerfelIndexWaehlen(spieler);
            Wuerfel gewaehlterWuerfel = spieler.getWuerfels().get(indexWuerfel);
            spieler.aktionAusfuehren("neigen", gewaehlterWuerfel, indexWuerfel);
            System.out.println("Neigen wurde erfolgreich gewählt.");
        }
    }

    //TODO в норм GUI надо переделать этот метод. Реализация будет похожей
    void checkFeldSituation(Spieler spieler){
        Feld feldNeigung;
        Feld feldSpeed;
        int anzahlWuerfels;
        if (spieler instanceof Pilot) {
            feldNeigung = spiel.getFeldPilot("neigenFeld");
            feldSpeed = spiel.getFeldPilot("speedFeld");
            anzahlWuerfels = spiel.getPilot().getAnzahlWuerfels();
            spieler = spiel.getPilot();
        } else {
            feldNeigung = spiel.getFeldCopilot("neigenFeld");
            feldSpeed = spiel.getFeldCopilot("speedFeld");
            anzahlWuerfels = spiel.getCopilot().getAnzahlWuerfels();
            spieler = spiel.getCopilot();
        }
        if(!feldNeigung.isBelegt() && !feldSpeed.isBelegt() && anzahlWuerfels == 2){
            verpflicteteAktion(spieler);
        }
        else if(!feldNeigung.isBelegt() && anzahlWuerfels == 1) {
            verpflicteteAktion(spieler);
        }
        else if(!feldSpeed.isBelegt() && anzahlWuerfels == 1) {
            verpflicteteAktion(spieler);
        }
        else {
            if (spieler instanceof Pilot) {
                pilotAufFeldSetzen();
            } else {
                copilotAufFeldSetzen();
            }
        }

    }

    //TODO в норм GUI надо переделать этот метод. Реализация будет похожей
    void neuWurfToken(Spieler spieler){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Wählen Sie das Ziel für den NeuWurf-Token:");
        System.out.println("[1] - Pilot");
        System.out.println("[2] - Copilot");
        System.out.println("[3] - Beide");

        int auswahl = scanner.nextInt();

        NeuWurfToken token = spielLogik.neuWurfToken();
        Spieler pilot = spiel.getPilot();
        Spieler copilot = spiel.getCopilot();

        switch (auswahl) {
            case 1:
                spieler.neuWurfTokenBenutzen(token, pilot);
                System.out.println("NeuWurf-Token wurde auf den Piloten angewendet.");
                break;
            case 2:
                spieler.neuWurfTokenBenutzen(token, copilot);
                System.out.println("NeuWurf-Token wurde auf den Copiloten angewendet.");
                break;
            case 3:
                spieler.neuWurfTokenBenutzen(token, pilot, copilot);
                System.out.println("NeuWurf-Token wurde auf beide Spieler angewendet.");
                break;
            default:
                System.out.println("Ungültige Auswahl. Bitte erneut versuchen.");
        }
    }

    //TODO в норм GUI надо переделать этот метод. Реализация будет похожей
    void kaffeToken(Spieler spieler){
        Scanner scanner = new Scanner(System.in);

        KaffeToken token = spielLogik.kaffeToken();

        System.out.println("Wählen Sie einen Würfel:");
        for (int i = 0; i < spieler.getWuerfels().size(); i++) {
            System.out.println("[" + i + "] - Wert: " + spieler.getWuerfels().get(i).getWert());
        }

        int indexWuerfel = scanner.nextInt();

        if (indexWuerfel < 0 || indexWuerfel >= spieler.getWuerfels().size()) {
            System.out.println("Ungültiger Index.");
            return;
        }

        System.out.println("Wählen Sie eine Aktion:");
        System.out.println("[1] - Plus");
        System.out.println("[2] - Minus");
        int aktionWahl = scanner.nextInt();

        KaffeTokenAktion aktion;
        if (aktionWahl == 1) {
            aktion = KaffeTokenAktion.PLUS;
        } else if (aktionWahl == 2) {
            aktion = KaffeTokenAktion.MINUS;
        } else {
            System.out.println("Ungültige Aktion.");
            return;
        }
        try {
            spieler.kaffeeTokenBenutzen(token, indexWuerfel, aktion);
            System.out.println("Kaffee-Token wurde erfolgreich benutzt.");
            System.out.println("Neuer Wert des Würfels: " + spieler.getWuerfels().get(indexWuerfel).getWert());
            spielLogik.clearKaffeFeld();
        } catch (IllegalArgumentException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    //TODO удалить
    void printWuerfel(Spieler spieler){
        System.out.println("\n");
        System.out.println("Deine Wuerfel sind:1");
        for (int i = 0; i < spieler.getWuerfels().size(); i++) {
            System.out.println("[" + i + "] - Wert: " + spieler.getWuerfels().get(i).getWert());
        }
        System.out.println("\n");
    }


     */

}

