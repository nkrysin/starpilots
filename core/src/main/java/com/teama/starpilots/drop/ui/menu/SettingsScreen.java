package com.teama.starpilots.drop.ui.menu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.teama.starpilots.drop.MainBk;
import com.teama.starpilots.drop.ui.constants.TexturePaths;
import com.teama.starpilots.drop.ui.settings.SettingsManager;

public class SettingsScreen implements Screen {

    private final MainBk main;
    private Stage stage;
    private SpriteBatch batch;
    private Texture background;
    private Skin skin;
    private TextButton.TextButtonStyle buttonStyle;
    private Table table;

    private static final int[][] RESOLUTIONS = {
        {1280, 720}, {1600, 900}, {1920, 1080}, {2560, 1440}
    };

    public SettingsScreen(MainBk main) {
        this.main = main;
    }

    @Override
    public void show() {
        FitViewport viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal(TexturePaths.SKIN_PATH));
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.newDrawable("button-c", Color.SLATE);
        buttonStyle.down = skin.newDrawable("button-p", Color.SLATE);
        buttonStyle.over = skin.newDrawable("button-h", Color.SKY);
        buttonStyle.font = skin.getFont("button");
        buttonStyle.fontColor = Color.WHITE;

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        batch = new SpriteBatch();
        background = new Texture(TexturePaths.BACKGROUND_MENU_PATH);

        showMain();
    }

    private void showMain() {
        table.clear();

        TextButton resolutionBtn = new TextButton("Resolution", buttonStyle);
        resolutionBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showResolution();
            }
        });

        TextButton audioBtn = new TextButton("Audio", buttonStyle);
        audioBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAudio();
            }
        });

        TextButton backBtn = new TextButton("Back", buttonStyle);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new MainMenuScreen(main));
            }
        });

        table.add(resolutionBtn).width(350f).pad(20).row();
        table.add(audioBtn).width(350f).pad(20).row();
        table.add(backBtn).width(350f).pad(20).row();
    }

    private void showResolution() {
        table.clear();

        if (Gdx.app.getType() != Application.ApplicationType.WebGL) {
            for (int[] res : RESOLUTIONS) {
                int w = res[0], h = res[1];
                TextButton btn = new TextButton(w + "x" + h, buttonStyle);
                btn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        SettingsManager.saveAndApply(w, h, false);
                    }
                });
                table.add(btn).width(350f).pad(10).row();
            }
        }

        TextButton fullscreenBtn = new TextButton(
            SettingsManager.isFullscreen() ? "Windowed" : "Fullscreen", buttonStyle
        );
        fullscreenBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (SettingsManager.isFullscreen()) {
                    SettingsManager.saveAndApply(SettingsManager.getSavedWidth(), SettingsManager.getSavedHeight(), false);
                } else {
                    SettingsManager.saveAndApply(0, 0, true);
                }
            }
        });
        table.add(fullscreenBtn).width(350f).pad(10).row();

        TextButton backBtn = new TextButton("Back", buttonStyle);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMain();
            }
        });
        table.add(backBtn).width(350f).pad(10).row();
    }

    private void showAudio() {
        table.clear();

        TextButton musicToggleBtn = new TextButton(
            SettingsManager.isMusicEnabled() ? "Music: ON" : "Music: OFF", buttonStyle
        );
        musicToggleBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean nowEnabled = !SettingsManager.isMusicEnabled();
                SettingsManager.saveMusicEnabled(nowEnabled);
                if (nowEnabled) {
                    main.menuMusic.setVolume(SettingsManager.getMusicVolume());
                    if (!main.menuMusic.isPlaying()) main.menuMusic.play();
                    musicToggleBtn.setText("Music: ON");
                } else {
                    main.menuMusic.pause();
                    musicToggleBtn.setText("Music: OFF");
                }
            }
        });

        Slider volumeSlider = new Slider(0f, 1f, 0.05f, false, skin);
        volumeSlider.setValue(SettingsManager.getMusicVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float vol = volumeSlider.getValue();
                SettingsManager.saveMusicVolume(vol);
                if (SettingsManager.isMusicEnabled()) {
                    main.menuMusic.setVolume(vol);
                }
            }
        });

        TextButton backBtn = new TextButton("Back", buttonStyle);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMain();
            }
        });

        table.add(musicToggleBtn).width(350f).pad(20).row();
        table.add(volumeSlider).width(350f).pad(20).row();
        table.add(backBtn).width(350f).pad(20).row();
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0, 1920, 1080);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (skin != null) skin.dispose();
    }
}
