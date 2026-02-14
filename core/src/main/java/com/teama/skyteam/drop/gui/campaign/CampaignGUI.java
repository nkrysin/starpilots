/**
 * Repräsentiert die GUI-Komponente für das Campaign im Game.
 *
 * Die Klasse `CampaignGUI` rendert die aktuelle Route des Szenarios und die Flugzeugpositionen
 * basierend auf dem aktuellen Spielzustand. Sie ermöglicht auch die Bewegung der Ansicht
 * durch Schritte entlang der Route.
 */
package com.teama.skyteam.drop.gui.campaign;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teama.skyteam.drop.domain.model.campaign.RouteField;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.gui.TexturePaths;
import com.teama.skyteam.drop.gui.UILayout;

import java.util.TreeMap;

public class CampaignGUI {

    private Texture campaignTexture;
    private Texture planeTexture;
    private Texture emptyTexture;
    private Game game;
    private String campaignName;
    private int currentPosititon;
    private float cardX, cardY;
    private float offsetY = UILayout.CAMPAIGN_PLANE_Y_OFFSET;

    public CampaignGUI(Game game) {
        this.game = game;
        planeTexture = new Texture(TexturePaths.PLANE_PATH);
        emptyTexture = new Texture(TexturePaths.WITHOUT_HIGHLIGHT_PATH);
        campaignName = game.getCampaign().getCampaignName();
        campaignTexture = new Texture(TexturePaths.ASSETS_CAMPAIGN_PATH + campaignName + TexturePaths.IMG_FORMAT);
        currentPosititon = game.getCurrentPosition();

        cardX = UILayout.CAMPAIGN_COORDINATES[0];
        cardY = UILayout.CAMPAIGN_COORDINATES[1];
    }


    public void render(SpriteBatch batch) {
        TreeMap<Integer, RouteField> routeSzenario = game.getRouteCampaign();
        batch.draw(campaignTexture, cardX, cardY - offsetY);

        int plane_y = UILayout.CAMPAIGN_PLANE_Y_COORDINATE;
        for (int i = currentPosititon; i < routeSzenario.size(); i++) {
            RouteField currentField = routeSzenario.get(i);
            if (currentField != null) {
                int planesCount = currentField.getPlaneCount();
                for (int j = 0; j < 3; j++) {
                    float x = UILayout.PLANE_POSITIONS[j][0];
                    float y = plane_y - offsetY;

                    if (j < planesCount) {
                        batch.draw(planeTexture, x, y, UILayout.CAMPAIGN_PLANE_SIZE[0], UILayout.CAMPAIGN_PLANE_SIZE[1]);
                    } else {
                        batch.draw(emptyTexture, x, y, UILayout.CAMPAIGN_PLANE_SIZE[0], UILayout.CAMPAIGN_PLANE_SIZE[1]);
                    }
                }
                plane_y += UILayout.MOVE_STEP;
            }
        }
    }

    /**
     * Bewegt die Karte um eine bestimmte Anzahl von Schritten.
     *
     * @param steps Die Anzahl der Schritte, um die die Karte bewegt werden soll.
     */
    public void moveStep(int steps) {
        offsetY += steps * UILayout.MOVE_STEP;
    }

    public void dispose() {
        campaignTexture.dispose();
        planeTexture.dispose();
        emptyTexture.dispose();
    }
}
