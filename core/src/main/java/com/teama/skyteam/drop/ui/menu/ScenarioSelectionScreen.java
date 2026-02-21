/**
 * Repräsentiert den Szenarioauswahl-Bildschirm im Game.
 *
 * Die Klasse `ScenarioSelectionScreen` ermöglicht es dem Player, ein Campaign auszuwählen,
 * mit dem das Game gestartet wird. Jedes Campaign wird durch eine Tastet dargestellt,
 * und es wird eine spezifische Route initialisiert. Der Bildschirm enthält außerdem eine Option,
 * zum Hauptmenü zurückzukehren.
 *
 * **Wichtig:** Beim Hinzufügen eines neuen Szenarios muss eine entsprechende Taste
 * mit einer Zuordnung zu diesem Campaign in diese Klasse aufgenommen werden.
 */
package com.teama.skyteam.drop.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teama.skyteam.drop.MainBk;
import com.teama.skyteam.drop.domain.model.campaign.Campaign;
import com.teama.skyteam.drop.domain.model.game.Game;
import com.teama.skyteam.drop.ui.constants.TexturePaths;
import com.teama.skyteam.drop.domain.model.game.GameConstructor;

class ScenarioSelectionScreen implements Screen {
    private final MainBk main;
    private Stage stage;
    private Skin skin;
    private Texture logo;
    private SpriteBatch batch;
    private Texture planeBackground;

    public ScenarioSelectionScreen(MainBk main) {
        this.main = main;
    }

    @Override
    public void show() {
        if (!main.menuMusic.isPlaying()) {
            main.menuMusic.play();
        }
        FitViewport viewport = new FitViewport(1920, 1080);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);


        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton.TextButtonStyle blueButtonStyle = ButtonStyle.style();

        loadCampaigns(table, blueButtonStyle);

        TextButton goBackButton = new TextButton("Go back", blueButtonStyle);
        goBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new MainMenuScreen(main));
            }
        });

        float buttonWidth = 350f;
        table.add(goBackButton).width(buttonWidth).pad(20).row();

        batch = new SpriteBatch();
        logo = new Texture(TexturePaths.LOGO_PATH);
        planeBackground = new Texture(TexturePaths.BACKGROUND_CAMPAIGN_SELECTION_PATH);
    }

    private void loadCampaigns(Table table, TextButton.TextButtonStyle style) {
        JsonValue manifest = new JsonReader().parse(
            Gdx.files.internal("campaigns/campaigns.json")
        );
        for (JsonValue entry : manifest) {
            FileHandle file = Gdx.files.internal("campaigns/" + entry.asString() + ".json");
            Campaign campaign = new Campaign(file);
            TextButton btn = new TextButton(campaign.getCampaignName(), style);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    startGameWithCampaign(campaign);
                }
            });
            table.add(btn).width(350f).pad(20).row();

        }
    }


    private void startGameWithCampaign(Campaign campaign) {
        Game game = GameConstructor.getInstance(campaign);
        main.setScreen(new GameScreen(main, game));
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        batch.draw(planeBackground, 0, 0, 1920, 1080);
        batch.draw(logo, 519, 720, 883, 220);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
    }
}
