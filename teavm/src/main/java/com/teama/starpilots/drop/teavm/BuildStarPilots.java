package com.teama.starpilots.drop.teavm;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.builder.TeaBuilder;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;
import org.teavm.vm.TeaVMOptimizationLevel;

import java.io.File;

/**
 * Build-time entry point. Run via Gradle task :teavm:buildWeb.
 * Invokes TeaVM to compile the game to JavaScript.
 * Output is placed in teavm/build/dist and served by Nginx.
 */
public class BuildStarPilots {

    public static void main(String[] args) throws Exception {
        AssetFileHandle assetsPath = new AssetFileHandle("../assets");

        new TeaBuilder(new WebBackend().setStartJettyAfterBuild(false))
            .addAssets(assetsPath)
            .setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE)
            .setMainClass(StarPilotsWebLauncher.class.getName())
            .setObfuscated(false)
            .build(new File("build/dist"));
    }
}
