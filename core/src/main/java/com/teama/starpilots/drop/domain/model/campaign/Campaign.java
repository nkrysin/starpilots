package com.teama.starpilots.drop.domain.model.campaign;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
/**
 * Die Klasse Campaign verwaltet alle Informationen zu einem bestimmten Spielszenario.
 * Dazu gehören die Route, Flughöhen, die Art des Höhen-Szenarios und die spezifischen Felder auf der Route.
 * Die Klasse ermöglicht den Import eines Szenarios aus einer Datei und bietet Zugriff auf alle relevanten Details.
 */

public class Campaign {
    private String campaignName;
    private String campaignAssetId;
    private TreeMap<Integer, RouteField> currentRoute;
    private List<Altitude> altitudeCampaign;
    private AltitudeCampaignTypes altitudeCampaignType;

    public Campaign(FileHandle jsonFile) {
        JsonValue json = new JsonReader().parse(jsonFile);
        this.campaignName = json.getString("name");
        this.campaignAssetId = json.getString("assetId");
        this.altitudeCampaignType = AltitudeCampaignTypes.valueOf(json.getString("altitudeType").toUpperCase());
        this.currentRoute = parseRoute(json.get("fields"));
        this.altitudeCampaign = parseAltitudes(json.get("altitudes"));
    }

    private static TreeMap<Integer, RouteField> parseRoute(JsonValue fields) {
        TreeMap<Integer, RouteField> route = new TreeMap<>();
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            route.put(i, new RouteField(fields.getInt(i), i == size -1));
        }
        return route;
    }

    private static List<Altitude> parseAltitudes(JsonValue altitudes) {
        List<Altitude> list = new ArrayList<>();
        for (JsonValue entry : altitudes) {
            list.add(Altitude.of(entry.getInt("altitude"), entry.getBoolean("rerollToken")));
        }
        return list;
    }


    public String getCampaignName() {
        return campaignName;
    }

    public String getCampaignAssetId() {
        return campaignAssetId;
    }

    public TreeMap<Integer, RouteField> getCurrentRoute() {
        return currentRoute;
    }

    public AltitudeCampaignTypes getAltitudeCampaignType() {
        return altitudeCampaignType;
    }


    public List<Altitude> getAltitudeCampaign() {
        return altitudeCampaign;
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


}

