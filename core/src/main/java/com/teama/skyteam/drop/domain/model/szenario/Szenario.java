package com.teama.skyteam.drop.domain.model.szenario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teama.skyteam.drop.domain.model.szenario.flughoehe.Flughoehe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.TreeMap;
/**
 * Die Klasse Szenario verwaltet alle Informationen zu einem bestimmten Spielszenario.
 * Dazu gehören die Route, Flughöhen, die Art des Höhen-Szenarios und die spezifischen Felder auf der Route.
 * Die Klasse ermöglicht den Import eines Szenarios aus einer Datei und bietet Zugriff auf alle relevanten Details.
 */

public class Szenario {
    private String szenarioName;
    private TreeMap<Integer, ReiseFeld> currentRoute;
    private List<Flughoehe> hoeheSzenario;
    private HoeheSzenarioTypes hoeheSzenarioType;

    public Szenario(Route szenarioName) throws IOException {
        scenarioImport(szenarioName);
    }

    /**
     * Liest die Route aus einer Datei und erstellt eine Zuordnung von Positionen zu Feldern.
     *
     * @param route Die Route, die gelesen werden soll.
     * @param fileContent Der Inhalt der Datei als String.
     * @return Eine TreeMap, die Positionen zu ReiseFeldern zuordnet.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    private static TreeMap<Integer, ReiseFeld> readRoute(Route route, String fileContent) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(fileContent));
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

    /**
     * Liest den Typ des Höhen-Szenarios für eine gegebene Route aus einer Datei.
     *
     * @param route Die Route, deren Höhen-Szenario gelesen werden soll.
     * @param fileContent Der Inhalt der Datei als String.
     * @return Der Typ des Höhen-Szenarios als String.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    private static String readHoeheSzenario(Route route, String fileContent) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(fileContent));
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

    public String getSzenarioName() {
        return szenarioName;
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

    void setHoeheSzenarioType(String hoeheSzenarioType) {
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
        this.hoeheSzenario = adjustHoeheRoute(hoeheSzenario, szenarioRouteLength);
    }

    /**
     * Importiert ein Szenario, indem es die Route und das Höhen-Szenario aus der Datei lädt.
     *
     * @param route Die Route, die importiert werden soll.
     * @throws IOException Wenn die Datei nicht gelesen werden kann.
     */
    private void scenarioImport(Route route) throws IOException {
        if (route == null) {
            throw new IOException("Ungültige Route. Route kann nicht null sein.");
        }
        setSzenarioName(route.getRouteName());

        FileHandle routesFile = Gdx.files.internal("routes.txt");

        if (!routesFile.exists()) {
            throw new IOException("Die Datei routes.txt konnte nicht gefunden werden.");
        }

        setCurrentRoute(readRoute(route, routesFile.readString()));

        String hoeheSzenarioType = readHoeheSzenario(route, routesFile.readString());
        setHoeheSzenarioType(hoeheSzenarioType);
        setHoeheSzenario(Flughoehe.createHoeheSzenario(hoeheSzenarioType));
    }

    /**
     * Gibt das aktuelle Feld an einer bestimmten Position auf der Route zurück.
     *
     * @param position Die Position auf der Route.
     * @return Das Feld an der gegebenen Position.
     */
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

    /**
     * Passt die Liste der Flughöhen an die Länge der Szenario-Route an.
     *
     * @param hoeheRoute Die Liste der Flughöhen.
     * @param szenarioRouteLength Die Länge der Route.
     * @return Die angepasste Liste der Flughöhen.
     */
    List<Flughoehe> adjustHoeheRoute(List<Flughoehe> hoeheRoute, int szenarioRouteLength) {
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

