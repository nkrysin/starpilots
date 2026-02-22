package com.teama.starpilots.drop.teavm;

import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import com.teama.starpilots.drop.MainBk;

/**
 * Web (browser) entry point — this class is compiled to JavaScript by TeaVM.
 * Note: audio is not supported in gdx-teavm; music will be silently skipped.
 */
public class StarPilotsWebLauncher {

    public static void main(String[] args) {
        WebApplicationConfiguration config = new WebApplicationConfiguration();
        config.width = 0;
        config.height = 0;
        new WebApplication(new MainBk(), config);
    }
}
