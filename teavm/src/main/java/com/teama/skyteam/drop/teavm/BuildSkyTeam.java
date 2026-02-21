package com.teama.skyteam.drop.teavm;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.compiler.TeaCompiler;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;
import org.teavm.vm.TeaVMOptimizationLevel;

import java.io.File;

/**
 * Build-time entry point. Run via Gradle task :teavm:buildWeb.
 * Invokes TeaVM to compile the game to JavaScript.
 * Output is placed in teavm/build/dist and served by Nginx.
 */
public class BuildSkyTeam {

    public static void main(String[] args) throws Exception {
        AssetFileHandle assetsPath = new AssetFileHandle("../assets");

        new TeaCompiler(new WebBackend())
            .addAssets(assetsPath)
            .setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE)
            .setMainClass(SkyTeamWebLauncher.class.getName())
            .setObfuscated(false)
            .build(new File("build/dist"));
    }
}
