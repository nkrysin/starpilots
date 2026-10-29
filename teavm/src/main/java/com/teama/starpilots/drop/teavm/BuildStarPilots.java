package com.teama.starpilots.drop.teavm;

import com.badlogic.gdx.files.FileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.builder.TeaBuilder;
import com.github.xpenatan.gdx.teavm.backends.shared.config.builder.TeaBuilderData;
import com.github.xpenatan.gdx.teavm.backends.shared.config.plugin.GdxTeaVMPluginConfig;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;
import org.teavm.tooling.TeaVMTargetType;
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

        new TeaBuilder(new StaticWebBackend().setStartJettyAfterBuild(false))
            .addAssets(assetsPath)
            .setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE)
            .setMainClass(StarPilotsWebLauncher.class.getName())
            .setObfuscated(false)
            .build(new File("build/dist"));
    }

    private static final class StaticWebBackend extends WebBackend {
        @Override
        protected void setup(TeaBuilderData data) {
            // Upstream setup constructs Jetty even when its preview server is disabled.
            tool.getProperties().setProperty(GdxTeaVMPluginConfig.LOGO_PATH, logoPath);
            targetType = TeaVMTargetType.JAVASCRIPT;
            tool.setTargetFileName(data.outputName + ".js");
            releasePath = new FileHandle(new File(data.output, webappFolderName));
            tool.setTargetDirectory(releasePath.file());
            setupWebapp(data);
        }
    }
}
