package com.teama.skyteam.drop.domain.model.campaign;

public enum Route {
    MONTREAL_TRUDEAU("MontrealTrudeau"),
    HANEDA("Haneda"),
    KUALA_LUMPUR("KualaLumpur");

    private final String routeName;

    Route(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }
}
