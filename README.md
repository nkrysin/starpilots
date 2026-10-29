# Star Pilots by Team-A

This checkout uses the `docker-version` deployment branch: no music playback, Settings screen or Exit buttons. Keep this branch as the source of the website game.

Java 21 is required for compilation. Build and validate web output before building the static runtime image:

```sh
./gradlew :teavm:buildWeb :lwjgl3:classes copyAuditDependencies --no-daemon --max-workers=2
node scripts/check-web.mjs
docker build -t star-pilots .
```

Run builds on a development machine or CI runner. The production container only serves static files; restarting it does not run Gradle. GitHub Checks validates PR/push/manual runs but does not publish or deploy images yet. See [SECURITY-AUDIT.md](SECURITY-AUDIT.md), including the remaining Jetty build-tool findings and default-branch automation requirements.

## How to Start the Project

### Desktop
1. Open the project in **IntelliJ IDEA**.
2. Find the file `Lwjgl3Launcher`:
    - Path: `lwjgl3/src/main/java/com/teama/starpilots/drop/lwjgl3/Lwjgl3Launcher.java`
3. Right-click on `Lwjgl3Launcher` and select **Run 'Lwjgl3Launcher.main()'**.

### Web (Docker)
1. Build the TeaVM web module:
   ```
   ./gradlew :teavm:buildWeb
   ```
2. Build and run the Docker container:
   ```
   docker build -t star-pilots .
   docker run -d --name star-pilots -p 8080:80 star-pilots
   ```
3. Open `http://localhost:8080` in a browser.

## How to Play

### Main Menu
- After launching the game you will see the main menu where you can start the game by clicking **Start Game**.

### Campaign Selection
- Choose a campaign scenario by clicking on it.
- Click **Go back** to return to the main menu.

### Game Screen
- Camera movement is controlled with the **Up** and **Down** arrow keys.
- Select a die by left-clicking on it. Place it on a field by left-clicking the corresponding slot.
- Press **ESC** to open the pause menu.

### Pause Menu
- **Resume** — continue the game.
- **Return to menu** — go back to the main menu.

## Project Description

### Technologies
- **Java 21** — game logic implementation.
- **LibGDX** — graphics and UI framework.
- **TeaVM (gdx-teavm)** — compiles the game to JavaScript for browser deployment.
- **Docker + Nginx** — containerized web hosting.

### Team
The project was developed by **Team A**: **Nataliia Shcherba** and **Nikolai Krysin**.

## Game Rules

**Star Pilots** is a cooperative board game for two players where you work together to pilot a spaceship and land it safely.

### Goal
The goal is to successfully land the spaceship by managing all descent and landing phases. You must maintain control over altitude, speed, landing gear, flaps and other systems, avoid collisions with asteroids, and reach the landing zone at the right moment.

### Gameplay

#### 1. Round Preparation
Before each round you may discuss plans and strategies. During the round, communication is forbidden — each player acts independently.

#### 2. Placing Dice
- **Pilot** may only place dice on **blue fields**, **Co-Pilot** only on **orange fields**.
- Both players may place dice on **coffee fields**.
- Dice are placed in turns. You cannot see the other player's dice.

#### 3. Coffee and Reroll Tokens
- Use a **coffee token** to change a die value by **+1 or -1**.
- Use a **reroll token** to re-roll one of your dice.

#### 4. Activating Systems
- **Landing gear:** Can be activated in any order.
- **Brakes:** Must be activated strictly in order **2 → 4 → 6**.
- **Flaps:** Must also be activated in the required order.

#### 5. Challenges
- **Altitude:** The spaceship descends by **1000 meters (1 unit)** each round.
- **Tilt:** The spaceship must not deviate more than **2 units** left or right.
- **Speed:** Keep the speed under control and brake in time.
- **Collisions:** Clear the path using rockets to avoid asteroids.

### Win Conditions
The final round begins when:
- The **landing zone icon** appears at your **current position**.
- The **spaceship icon** appears at your **current altitude**.

You win if at the end of the final round:
1. **No asteroids remain on the approach track**
2. **Landing gear and flaps show green**
3. **The spaceship is perfectly level**
4. **Speed is lower than braking power**

- `lwjgl3`: Desktop platform using LWJGL3.
- `teavm`: Web platform compiled to JavaScript via TeaVM.

Useful commands:
- `lwjgl3:run` — start the desktop application.
- `teavm:buildWeb` — compile the game to JavaScript for web deployment.
- `lwjgl3:jar` — build a runnable jar at `lwjgl3/build/libs`.
- `test` — run unit tests.
