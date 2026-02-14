package com.teama.skyteam.drop.domain.model.campaign.altitude;

enum Green {
    GREEN_6000(6000, true),
    GREEN_5000(5000, false),
    GREEN_4000(4000, false),
    GREEN_3000(3000, false),
    GREEN_2000(2000, true),
    GREEN_1000(1000, false),
    GREEN_0(0, false);

    private final int altitude;
    private final boolean hasRerollToken;

    Green(int altitude, boolean hasRerollToken) {
        this.altitude = altitude;
        this.hasRerollToken = hasRerollToken;
    }

    int getAltitude() {
        return altitude;
    }

    boolean hasRerollToken() {
        return hasRerollToken;
    }
}
