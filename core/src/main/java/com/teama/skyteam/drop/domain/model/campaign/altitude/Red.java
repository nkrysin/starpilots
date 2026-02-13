package com.teama.skyteam.drop.domain.model.campaign.altitude;

enum Red {
    RED_6000(6000, true),
    RED_5000(5000, false),
    RED_4000(4000, false),
    RED_3000(3000, false),
    RED_2000(2000, false),
    RED_1000(1000, false),
    RED_0(0, false);

    private final int hoehe;
    private final boolean hatErneuWerfen;

    Red(int hoehe, boolean hatErneuWerfen) {
        this.hoehe = hoehe;
        this.hatErneuWerfen = hatErneuWerfen;
    }

    int getHoehe() {
        return hoehe;
    }

    boolean hatErneuWerfen() {
        return hatErneuWerfen;
    }
}
