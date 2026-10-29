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

## Unresolved build-tool findings

Trivy scanned 98 identified packages in copied, resolved external dependency jars, including the shaded desktop packaging tool. The project's own core.jar is excluded from this external-dependency inventory: generic jar coordinates otherwise caused a false match to the unrelated io.kestra:core package. Source security checks still scan our code.

The current gdx-teavm build backend requires Jetty 11 classes even when its preview server is disabled. Upgraded its BOM to the latest 11.x version available from Maven Central, 11.0.26. The remaining findings are **2 HIGH, 3 MEDIUM and 1 LOW**:

- HIGH CVE-2026-2332: jetty-http 11.0.26.
- HIGH CVE-2026-10050: jetty-security 11.0.26.
- MEDIUM CVE-2024-6763, CVE-2026-6790; LOW CVE-2025-11143 in Jetty.
- MEDIUM CVE-2025-48924 in the packaging tool's shaded commons-lang3 3.16.0.

Jetty 11.0.29/11.0.31 fixes listed by the advisory database are not published in Maven Central's public 11.x line. Replacing it with Jetty 12 requires changes in the upstream backend's servlet API integration. Removing Jetty entirely was tested and fails during WebBackend initialization. No broad exclusions or vulnerability suppressions were added.

The preview server is explicitly disabled and Jetty is absent from the static production image. These findings therefore concern the build toolchain, not an exposed Java service on the VPS. The strict dependency-scan step intentionally remains blocking until the toolchain finding is resolved or a separately reviewed, scoped policy is adopted.

## Automation limits

Checks runs on push/PR/manual execution for docker-version; it does not deploy. Scheduled GitHub workflows run from the repository's default branch, so daily scanning must later be installed on that branch or orchestrated from portfolio-deploy. Dependabot configuration must also be present on the default branch to be activated. Version PRs target docker-version with a two-day cooldown; GitHub security PRs normally target the default branch and need separate deployment-branch coverage. Do not claim automatic security installation from these files alone.

GitHub workflow syntax was checked locally with actionlint. Production VPS, real deployment/rollback and full desktop distributions remain untested.
