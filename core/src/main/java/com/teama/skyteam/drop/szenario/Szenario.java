package com.teama.skyteam.drop.szenario;

import com.teama.skyteam.drop.szenario.flughoehe.Flughoehe;

import java.io.*;
import java.util.*;




public class Szenario {


    //TODO должен быть использован позже в Spiel для импорта графики
    private String szenarioName;


    private TreeMap<Integer, ReiseFeld> currentRoute;

    private List<Flughoehe> hoeheSzenario;
    private HoeheSzenarioTypes hoeheSzenarioType;



    public Szenario(Route szenarioName) throws IOException {
        scenarioImport(szenarioName);
    }

    private void setSzenarioName(String szenarioName) {
        this.szenarioName = szenarioName;
    }




    public TreeMap<Integer, ReiseFeld> getCurrentRoute() {
        return currentRoute;
    }

    private void setCurrentRoute(TreeMap<Integer, ReiseFeld> currentRoute) {
        this.currentRoute = currentRoute;
    }


    public HoeheSzenarioTypes getHoeheSzenarioType() {
        return hoeheSzenarioType;
    }

    private void setHoeheSzenarioType(String hoeheSzenarioType) {
        try {
            this.hoeheSzenarioType = HoeheSzenarioTypes.valueOf(hoeheSzenarioType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Fehler: Szenario hat keine gültigen Werte: " + hoeheSzenarioType, e);
        }
    }

    public List<Flughoehe> getHoeheSzenario() {
        return hoeheSzenario;
    }

    private void setHoeheSzenario(List<Flughoehe> hoeheSzenario) {
        int szenarioRouteLength = currentRoute.size();
        //Ist die Länge des Szenarios kürzer als die Standardanzahl der Höhen,
        // dann sollte sie entsprechend reduziert werden, da die Höhe nur einmal pro Runde reduziert wird
        this.hoeheSzenario = adjustHoeheRoute(hoeheSzenario, szenarioRouteLength);
    }



    private void scenarioImport(Route route) throws IOException {
        if (route == null) {
            throw new IOException("Ungültige Route. Route kann nicht null sein.");
        }

        setSzenarioName(route.getRouteName());
        //TODO внимательно собрабоать маршрут, где лежит сценарий
        //Вроде так должно быть
        setCurrentRoute(readRoute(route, "routes.txt"));

        String hoeheSzenarioType = readHoeheSzenario(route, "routes.txt");
        setHoeheSzenarioType(hoeheSzenarioType);
        setHoeheSzenario(Flughoehe.createHoeheSzenario(hoeheSzenarioType));

    }

    private static TreeMap<Integer, ReiseFeld> readRoute(Route route, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int routeLength = 0;

        String routeName = route.getRouteName();

        while ((line = reader.readLine()) != null) {
            if (line.equals(routeName)) {
                reader.readLine();
                routeLength = Integer.parseInt(reader.readLine());
                break;
            }
        }

        if (routeLength == 0) {
            reader.close();
            throw new IllegalArgumentException("Route " + routeName + " wurde nicht in der Datei gefunden.");
        }

        TreeMap<Integer, ReiseFeld> treeMap = new TreeMap<>();

        for (int i = 0; i < routeLength; i++) {
            if ((line = reader.readLine()) == null) {
                reader.close();
                throw new IOException("Unerwartetes Ende der Datei. Erwartete " + routeLength + "Schritte.");
            }

            int planes = Integer.parseInt(line);
            boolean istFlughafen = (i == routeLength - 1);
            treeMap.put(i, new ReiseFeld(planes, istFlughafen));
        }

        reader.close();
        return treeMap;
    }

    private static String readHoeheSzenario(Route route, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        String routeName = route.getRouteName();
        String type = null;

        while ((line = reader.readLine()) != null) {
            if (line.equals(routeName)) {
                type = reader.readLine();
                break;
            }
        }

        reader.close();

        if (type == null) {
            throw new IllegalArgumentException("Das Höhenskript für die Route " + routeName + " wurde nicht gefunden.");
        }

        return type.trim().toUpperCase();
    }


    public ReiseFeld getCurrentField(int position) {
        if (currentRoute == null) {
            throw new IllegalStateException("No route has been imported. Please import a route first.");
        }

        ReiseFeld field = currentRoute.get(position);
        if (field == null) {
            throw new IllegalArgumentException("Falsche Position: " + position);
        }
        return field;
    }



    public Flughoehe getCurrentFlughoehe() {
        List<Flughoehe> hoeheRoute = getHoeheSzenario();
        if (hoeheRoute.isEmpty()) {
            throw new IllegalStateException("Die hoeheRoute-Liste ist leer. Keine Flughoehe vorhanden");
        }
        return hoeheRoute.getFirst();
    }

    private List<Flughoehe> adjustHoeheRoute(List<Flughoehe> hoeheRoute, int szenarioRouteLength) {
        int l = hoeheRoute.size();
        if (szenarioRouteLength < l) {
            int difference = l - szenarioRouteLength;
            for (int i = 0; i < difference; i++) {
                hoeheRoute.removeFirst();
            }
        }
        return hoeheRoute;
    }

}

