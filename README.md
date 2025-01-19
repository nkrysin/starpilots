# ✈️ SkyTeam by Team-A

## ⚠️ Anleitung zum Starten des Projekts ⚠️
Früher musste das Programm wie unten beschrieben gestartet werden, da ein Bug in `ButtonStyle` vorhanden war.  
Jetzt kann das Programm direkt über das **Gradle-Menü** auf der rechten Seite gestartet werden.  
Falls dabei ein **JSON-Formatierungsfehler** auftritt, kann der folgende Startvorgang zuverlässig genutzt werden:

1. Öffnen Sie das Projekt in **IntelliJ IDEA**.
2. Suchen Sie die Datei `Lwjgl3Launcher`:
    - Pfad: `java/com/teama/skyteam/drop/lwjgl3/Lwjgl3Launcher.java`.
3. Klicken Sie mit der rechten Maustaste auf die Datei `Lwjgl3Launcher`.
4. Wählen Sie **Run 'Lwjgl3Launcher.main()'**.

# 📝 Projektbeschreibung

## 🗂️ Link zum alten Repository ohne LibGDX

🔗 **Alter Repository:** [https://gitlab.hof-university.de/nkrysin/team-a-sse](https://gitlab.hof-university.de/nkrysin/team-a-sse)

## 💾 Dokumentation
Die Dokumentation, einschließlich **Diagrammen**, **Anwendungsfalldefinition** und **Spezifikation**, befindet sich im Projektverzeichnis im Verzeichnis  **`documentation`** im Hauptverzeichnis.

## ✨ Kurze Beschreibung des Projekts
Im Rahmen des Kurses **Software System Entwicklung** wurde in diesem Projekt eine Implementierung der beliebten Brettspiel-Adaption **Sky Team** als Desktop-Spiel für den PC entwickelt. Das Originalspiel wurde von **Luc Rémond** entworfen und 2023 von **Le Scorpion Masqué** veröffentlicht. Zudem erhielt es den Preis **Spiel des Jahres 2024**. Das Hauptziel des Projekts war es, ein funktionierendes, optimiertes und ästhetisch ansprechendes Spiel zu schaffen.

## 💻 Verwendete Technologien:
- **Java 21** – für die Implementierung der internen Spiellogik.
- **LibGDX** – für die Umsetzung der grafischen Komponenten.

## 👥 Wer hat das Projekt erstellt?
Das Projekt wurde von **Team A** entwickelt.

Das Team bestand aus zwei Personen: **Nataliia Shcherba** und **Nikolai Krysin**. Alle notwendigen Aufgaben wurden gleichmäßig zwischen den Mitgliedern aufgeteilt. Jeder übernahm in jedem Moment die Aufgaben, die entweder am dringendsten waren oder für die mehr Interesse bestand: von der Entwicklung der Architektur, Implementierung und Tests bis hin zur finalen Verfeinerung der Dokumentation.

## ⚙️ Herausforderungen im Projekt

1. **Großer Arbeitsumfang:** Der Umfang des Projekts stellte an sich bereits eine Herausforderung dar.

2. **Notwendigkeit, alle Aspekte zu testen:** Insbesondere die Logik des Spiels erforderte umfangreiche Tests. Da zu Beginn kein GUI vorhanden war, wurde die Spiel-Logik über Konsolenausgaben getestet. Obwohl diese Methode praktikabel erschien, war es jedes Mal frustrierend, nach vielen Versuchen immer wieder neue Bugs zu entdecken.

3. **LibGDX:** Der Framework bietet viele Möglichkeiten, aber etwas optisch ansprechend zu gestalten, ist eine ganz andere Herausforderung. Die Fehlersuche nach der Implementierung des GUI war eine besonders "spannende" Abendbeschäftigung.

## 💡 Interessante Lösungen im Projekt

- **🛠️ Modularität:** Die Idee bestand darin, relativ unabhängige Komponenten wie Spieler, Flugzeug oder Szenarien zu erstellen und sie anschließend über eine separate Klasse **"Spiel"** zu verbinden. Dadurch können neue Funktionen, wie z. B. die Eingabe eines Spielernamens, problemlos in das Projekt integriert werden. Die Modularität zeigt sich auch in der Möglichkeit, neue Spielszenarien hinzuzufügen. Um ein neues Szenario zu integrieren, sind nur vier einfache Schritte erforderlich:
    1. Das neue Szenario in die Datei `routes.txt` hinzufügen.
    2. Das Szenario in die ENUM-Klasse `Route` eintragen.
    3. Den neuen Fall im `setSzenario`-Methodenblock der Klasse `Spiel` einfügen.
    4. Eine neue Taste im `ScenarioSelectionScreen` hinzufügen und das passende Bild in den Ordner `images/szenario/` legen.

  Diese Schritte ermöglichen die Auswahl eines neuen Szenarios, ohne den Pfad zur Datei manuell angeben zu müssen!

- **🚀 Optimierung:** Das Spiel wurde für Monitore mit einer Auflösung von **1920x1080** optimiert, was dem Industriestandard entspricht und in den meisten Haushalten vorhanden sein dürfte.

  Außerdem wurde auf eine effiziente Nutzung der PC-Ressourcen geachtet: Während der ersten Tests wurde festgestellt, dass der häufige Aufruf kleiner Texturen (85x85 Pixel, 8KB) selbst auf einem System mit **Ryzen 7** und **16 GB RAM** FPS-Drops verursachen konnte, was in der modernen Welt inakzeptabel ist. Zur Lösung dieses Problems wurde die Klasse **TextureCache** implementiert, die eine **HashMap** verwendet. Dabei dient der Pfad der Textur als Schlüssel und die Textur selbst als Wert. Diese Implementierung ist ein hervorragendes Beispiel für die Nutzung der Vorteile einer HashMap: konstante Suchzeiten und garantierte Eindeutigkeit.

  In der Klasse **SpielGUI** wurde der `switchTexture`-Methodenblock eingeführt, der die Textur einer Schaltfläche durch eine andere ersetzt, indem er sie aus dem Cache abruft. Das Ergebnis: Das Spiel kann stundenlang ohne FPS-Einbrüche oder Abstürze gespielt werden.

# 📜 Sky Team – Spielregeln

**Sky Team** ist ein kooperatives Brettspiel für zwei Spieler, bei dem Sie gemeinsam ein Flugzeug steuern, um es sicher zu landen.

## 🎯 Ziel des Spiels:
Das Ziel ist es, das Flugzeug erfolgreich zu landen, indem Sie alle Phasen des Sinkflugs und der Landung meistern. Dabei müssen Sie die Kontrolle über Höhe, Geschwindigkeit, Fahrgestell, Klappen und andere Systeme behalten, Kollisionen mit anderen Flugzeugen vermeiden und den Flughafen im richtigen Moment erreichen.

## 🎮 Spielablauf:

### 1. Vorbereitung einer Runde:
Vor Beginn jeder Runde können Sie Ihre Pläne und Strategien besprechen. Während der Runde ist jedoch jegliche Kommunikation verboten, und jeder Spieler handelt eigenständig.

### 2. Platzieren der Würfel:
- **Pilot** darf Würfel nur auf **blaue Felder** legen, **Kopilot** nur auf **orange Felder**.
- Auf **Kaffeefelder** dürfen beide Spieler Würfel legen.
- Würfel werden abwechselnd platziert, und es ist nicht erlaubt, die Würfel des anderen Spielers zu sehen.

### 3. Verwendung von Kaffee und Neuwurf:
- Sie können jederzeit ein **Kaffeetoken** verwenden, um den Wert eines Würfels um **+1 oder -1** zu verändern.
- Mit einem **Neuwurf-Token** kann ein Spieler einen seiner Würfel erneut würfeln.

### 4. Aktivierung von Systemen:
- **Fahrgestell:** Kann in beliebiger Reihenfolge aktiviert werden.
- **Bremsen:** Müssen streng in der Reihenfolge **2 → 4 → 6** aktiviert werden.
- **Klappen:** Müssen ebenfalls in der vorgegebenen Reihenfolge aktiviert werden.

### 5. Besondere Herausforderungen:
- **Höhe:** Die Höhe des Flugzeugs sinkt in jeder Runde automatisch um **1000 Meter (1 Einheit)**.
- **Fluglage:** Das Flugzeug darf nicht mehr als **2 Einheiten** nach links oder rechts abweichen.
- **Geschwindigkeit:** Achten Sie darauf, dass die Geschwindigkeit kontrolliert bleibt und rechtzeitig gebremst wird.
- **Kollisionen:** Räumen Sie den Weg über das Funkgerät frei, um Kollisionen mit anderen Flugzeugen zu vermeiden.

## 🏆 Gewinnbedingungen:
Die letzte Runde beginnt, sobald:
- Das **Flughafen-Bild** auf Ihrer **Aktuellen Position** ist.
- Das **Flugzeug-Bild** auf Ihrer **Aktuellen Höhe** ist.

Sie gewinnen das Spiel, wenn am Ende der letzten Runde folgende Bedingungen erfüllt sind:
1. **Kein Flugzeug verbleibt auf der Entfernungsliste**
2. **Fahrgestell und Klappen grünes Licht zeigen**
3. **Das Flugzeug ist genau waagerecht ausgerichtet**
4. **Die Geschwindigkeit ist geringer als die Stärke der Bremsen**

**Glückwunsch!** Wenn alle Kriterien erfüllt sind, applaudieren die Passagiere begeistert, und Sie haben das Spiel gewonnen! 🎉

# LibGDX default README

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
