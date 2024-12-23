package com.teama.skyteam.drop.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//TODO найти причину, почему при загрузке окна с уведомлением на секунду темнеет экран
public class ErrorDialog {

    private Stage stage;
    private Skin skin;

    public ErrorDialog(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
    }

    public void print(String title, String message) {
        Dialog dialog = new Dialog(title, skin);
        dialog.text(message);
        dialog.button("OK");
        dialog.show(stage);
    }
}

