# Security verification — 2026-10-29

Deployment source: `docker-version`, based on `cc57201`. These results describe the updated source and locally built image; the hosted game has not been replaced.

## Preserved browser behavior

Compared `main` and `docker-version`: the deployment branch removes menu music and its playback/disposal calls, the Settings screen, Settings/Exit buttons in the main menu and Exit in the pause menu. Game rules are unchanged. These core-source files remain byte-for-byte unchanged relative to `origin/docker-version` in this update.

## Updated and checked

- Gradle 8.11.1 → 8.14.6, regenerated wrapper and verified distribution checksum.
- LibGDX pinned consistently at 1.14.2, gdx-teavm 1.5.1 → 1.6.2, TeaVM resolves to 0.15.0; migrated the manual builder API to TeaBuilder.
- Construo 1.5.1 → 2.2.3. Updated bundled desktop JDK URLs from Java 17 to current Java 21 builds and configured their SHA-256 verification. Desktop packaging itself has not been executed.
- Removed unused gdx-dialogs, JUnit/Mockito dependencies and legacy/local/snapshot repositories. Fixed project-version interpolation and moved asset-list generation into one declared Gradle task.
- Web compilation and desktop-class compilation pass. The web artifact self-check verifies all 65 assets and three campaign JSON files. There are no existing game unit tests.
- Gitleaks 8.30.1: no detected secrets in Git history. Semgrep 1.182.0 / selected OWASP ERROR-level rules: no blocking source findings.
- Final Nginx runtime image: Trivy 0.76.1 reports 0 known vulnerabilities after upgrading Alpine packages. The image contains static game files, not a JVM or Gradle.
- Headless Chromium loaded the menu, selected a campaign and rendered the game board with no JavaScript errors or missing-resource responses. The main menu has only Start Game. Full gameplay, audio hardware and desktop execution were not tested.

## Jetty findings resolved through static-only compilation

The earlier toolchain scan found 2 HIGH, 3 MEDIUM and 1 LOW. The HIGH findings were CVE-2026-2332 and CVE-2026-10050 in Jetty 11.0.26. Maven Central still only publishes the 11.x line through 11.0.26 at the time of this check, so bumping that BOM cannot provide the listed public fixes.

Inspection of backend-web 1.6.2 sources showed that WebBackend.setup constructs JettyServer unconditionally, even when startJettyAfterBuild is false. BuildStarPilots now supplies a private static-only WebBackend subclass through the existing protected setup lifecycle hook. It configures JavaScript output and the loading asset, then reuses the backend's HTML/assets and TeaVM build pipeline. It does not construct a preview server. Core game source, launcher, asset set and compiler/library versions are unchanged.

Removed the unused Jetty dependency groups and BOM. This is feature/dependency removal, not a CVE suppression or a replacement class shadowing upstream code. A build-time check rejects resolved Jetty artifacts. A negative test injected a direct Jetty runtime dependency and confirmed that buildWeb fails before launching the web compiler.

Clean web compilation, desktop-class compilation, all 65 assets and three campaigns pass. Fresh ARM64 and AMD64 Nginx images were built. Chromium loaded the menu, selected Montreal Prime and rendered the board without JavaScript errors or failed resource responses; screenshots were inspected. The full deployment scan script now passes at its unchanged HIGH/CRITICAL threshold across eight runtime images and the resolved build jars. Semgrep's selected ERROR rules also pass (23 rules, 97 files).

The final full-severity jar scan identifies 89 packages and reports **0 HIGH, 0 CRITICAL and 1 MEDIUM**: CVE-2025-48924 in Construo's shaded commons-lang3 3.16.0. That remaining desktop-packaging-tool finding is visible and has not been suppressed. The project's own core.jar remains excluded from the external-dependency inventory because generic jar coordinates previously falsely matched the unrelated io.kestra:core package; source checks still scan our code.

Source inspected: [published backend-web 1.6.2 sources](https://repo.maven.apache.org/maven2/com/github/xpenatan/gdx-teavm/backend-web/1.6.2/backend-web-1.6.2-sources.jar).

## Automation limits

Checks runs on push/PR/manual execution for docker-version; it does not deploy. Scheduled GitHub workflows run from the repository's default branch, so daily scanning must later be installed on that branch or orchestrated from portfolio-deploy. Dependabot configuration must also be present on the default branch to be activated. Version PRs target docker-version with a two-day cooldown; GitHub security PRs normally target the default branch and need separate deployment-branch coverage. Do not claim automatic security installation from these files alone.

GitHub workflow syntax was checked locally with actionlint. Production VPS, real deployment/rollback and full desktop distributions remain untested.
