package com.teama.skyteam.drop.domain.model.campaign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teama.skyteam.drop.domain.model.campaign.altitude.Altitude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.TreeMap;
/**
 * Die Klasse Campaign verwaltet alle Informationen zu einem bestimmten Spielszenario.
 * Dazu gehören die Route, Flughöhen, die Art des Höhen-Szenarios und die spezifischen Felder auf der Route.
 * Die Klasse ermöglicht den Import eines Szenarios aus einer Datei und bietet Zugriff auf alle relevanten Details.
 */

public class Campaign {
    private String campaignName;
    private TreeMap<Integer, RouteField> currentRoute;
    private List<Altitude> altitudeCampaign;
    private AltitudeCampaignTypes altitudeCampaignType;

    public Campaign(Route campaignName) throws IOException {
        campaignImport(campaignName);
    }

    /**
     * Liest die Route aus einer Datei und erstellt eine Zuordnung von Positionen zu Feldern.
     *
     * @param route Die Route, die gelesen werden soll.
     * @param fileContent Der Inhalt der Datei als String.
     * @return Eine TreeMap, die Positionen zu ReiseFeldern zuordnet.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    private static TreeMap<Integer, RouteField> readRoute(Route route, String fileContent) throws IOException {
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

        TreeMap<Integer, RouteField> treeMap = new TreeMap<>();

        for (int i = 0; i < routeLength; i++) {
            if ((line = reader.readLine()) == null) {
                reader.close();
                throw new IOException("Unerwartetes Ende der Datei. Erwartete " + routeLength + "Schritte.");
            }

            int planes = Integer.parseInt(line);
            boolean istFlughafen = (i == routeLength - 1);
            treeMap.put(i, new RouteField(planes, istFlughafen));
        }

        reader.close();
        return treeMap;
    }

    /**
     * Liest den Typ des Höhen-Szenarios für eine gegebene Route aus einer Datei.
     *
     * @param route Die Route, deren Höhen-Campaign gelesen werden soll.
     * @param fileContent Der Inhalt der Datei als String.
     * @return Der Typ des Höhen-Szenarios als String.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     */
    private static String readAltitudeCampaign(Route route, String fileContent) throws IOException {
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

    public String getCampaignName() {
        return campaignName;
    }

    private void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public TreeMap<Integer, RouteField> getCurrentRoute() {
        return currentRoute;
    }

    private void setCurrentRoute(TreeMap<Integer, RouteField> currentRoute) {
        this.currentRoute = currentRoute;
    }

    public AltitudeCampaignTypes getAltitudeCampaignType() {
        return altitudeCampaignType;
    }

    void setAltitudeCampaignType(String altitudeCampaignType) {
        try {
            this.altitudeCampaignType = AltitudeCampaignTypes.valueOf(altitudeCampaignType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Fehler: Campaign hat keine gültigen Werte: " + altitudeCampaignType, e);
        }
    }

    public List<Altitude> getAltitudeCampaign() {
        return altitudeCampaign;
    }

    private void setAltitudeCampaign(List<Altitude> altitudeCampaign) {
        int campaignRouteLength = currentRoute.size();
        this.altitudeCampaign = adjustAltitudeRoute(altitudeCampaign, campaignRouteLength);
    }

    /**
     * Importiert ein Campaign, indem es die Route und das Höhen-Campaign aus der Datei lädt.
     *
     * @param route Die Route, die importiert werden soll.
     * @throws IOException Wenn die Datei nicht gelesen werden kann.
     */
    private void campaignImport(Route route) throws IOException {
        if (route == null) {
            throw new IOException("Ungültige Route. Route kann nicht null sein.");
        }
        setCampaignName(route.getRouteName());

        FileHandle routesFile = Gdx.files.internal("routes.txt");

        if (!routesFile.exists()) {
            throw new IOException("Die Datei routes.txt konnte nicht gefunden werden.");
        }

        setCurrentRoute(readRoute(route, routesFile.readString()));

        String altitudeCampaignType = readAltitudeCampaign(route, routesFile.readString());
        setAltitudeCampaignType(altitudeCampaignType);
        setAltitudeCampaign(Altitude.createAltitudeCampaign(altitudeCampaignType));
    }

    /**
     * Gibt das aktuelle Field an einer bestimmten Position auf der Route zurück.
     *
     * @param position Die Position auf der Route.
     * @return Das Field an der gegebenen Position.
     */
    public RouteField getCurrentField(int position) {
        if (currentRoute == null) {
            throw new IllegalStateException("No route has been imported. Please import a route first.");
        }

        RouteField field = currentRoute.get(position);
        if (field == null) {
            throw new IllegalArgumentException("Falsche Position: " + position);
        }
        return field;
    }


    public Altitude getCurrentAltitude() {
        List<Altitude> altitudeCampaign = getAltitudeCampaign();
        if (altitudeCampaign.isEmpty()) {
            throw new IllegalStateException("Die altitudeCampaign-Liste ist leer. Keine Altitude vorhanden");
        }
        return altitudeCampaign.getFirst();
    }

    /**
     * Passt die Liste der Flughöhen an die Länge der Campaign-Route an.
     *
     * @param altitudeRoute Die Liste der Flughöhen.
     * @param campaignRouteLength Die Länge der Route.
     * @return Die angepasste Liste der Flughöhen.
     */
    List<Altitude> adjustAltitudeRoute(List<Altitude> altitudeRoute, int campaignRouteLength) {
        int l = altitudeRoute.size();
        if (campaignRouteLength < l) {
            int difference = l - campaignRouteLength;
            for (int i = 0; i < difference; i++) {
                altitudeRoute.removeFirst();
            }
        }
        return altitudeRoute;
    }

}

