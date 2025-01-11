# Hausaufgaben zwischen Tag 1 und Tag 2

Diese Aufgaben vertiefen die Themen aus Tag 1: Multi-Project-Builds, Java-Plugins, Projektabhängigkeiten, `implementation` vs. `api`, Tasks, Inputs/Outputs und Configurations. Der Repository-Server aus Tag 2 wird dafür noch nicht benötigt.

Zum Ausprobieren:

```bash
./gradlew :project:run
```

## Aufgabe 1: Ein weiteres Modul anlegen

**Ziel:** Du erweiterst den bestehenden Multi-Project-Build um ein weiteres Java-Library-Modul und nutzt dieses Modul aus `library` heraus. Dabei wiederholst du die Projektstruktur, `settings.gradle.kts`, das `java-library` Plugin und Projektabhängigkeiten.

**Bedingung:** Wenn du das Hauptprogramm startest, ist die Ausgabe anders als vorher. Die neue Ausgabe muss durch Code aus dem neuen Modul `tools` beeinflusst werden.

Lege ein neues Modul `tools` an. Es ist eine gute Idee das Modul gleich in die `settings.gradle.kts` einzutragen und erstmal eine `build.gradle.kts` mit nur dem Plugin `java-library` in dem Ordner anzulegen, damit IntelliJ es (nach einem Gradle Reload) richtig als Modul erkennt.

**Bedingung:** Prüfen ob `main` und `java` hervorgehoben werden.

Die `library` soll `tools` mit `implementation` verwenden.

Du kannst in `tools` zum Beispiel diese Klasse anlegen:

```java
package com.example.gradle.tools;

public class MessageTools {

    public static String emphasize(String message) {
        return "*** " + message.toUpperCase() + " ***";
    }

}
```

Nutze diese Klasse anschließend in `GradleLibrary`. Die Ausgabe des Hauptprogramms soll sich dadurch sichtbar ändern.

Zum Ausprobieren:

```bash
./gradlew :project:run
```

## Aufgabe 2: Configurations untersuchen

**Ziel:** Du untersuchst, welche Abhängigkeiten auf welchen Classpaths landen. Dabei soll der Unterschied zwischen deklarierter Abhängigkeit, Compile-Classpath und Runtime-Classpath sichtbar werden.

**Bedingung:** Du kannst erklären, warum `tools` bei `library` auftaucht und warum `tools` bei `project` zunächst im Runtime-Classpath, aber nicht im Compile-Classpath sichtbar ist.

Führe die folgenden Befehle aus und vergleiche die Ausgabe:

```bash
./gradlew :library:dependencies --configuration api
./gradlew :library:dependencies --configuration implementation
./gradlew :library:dependencies --configuration compileClasspath
./gradlew :library:dependencies --configuration runtimeClasspath
./gradlew :project:dependencies --configuration compileClasspath
./gradlew :project:dependencies --configuration runtimeClasspath
```

Nutze danach `dependencyInsight`, um einzelne Abhängigkeiten gezielt zu untersuchen:

```bash
./gradlew :project:dependencyInsight --dependency tools --configuration compileClasspath
./gradlew :project:dependencyInsight --dependency tools --configuration runtimeClasspath
./gradlew :library:dependencyInsight --dependency tools --configuration compileClasspath
./gradlew :library:dependencyInsight --dependency tools --configuration runtimeClasspath
```

Wenn du zusätzlich eine externe Abhängigkeit ausprobieren willst, füge sie in `tools` hinzu und untersuche sie genauso:

```kotlin
dependencies {
    implementation("org.apache.commons:commons-lang3:3.14.0")
}
```

Und im Root-Skript, damit die Abhängigkeit bei Maven Central geladen werden kann:

```kotlin
subprojects {
    repositories {
        mavenCentral()
    }
}
```

Passende Befehle dazu:

```bash
./gradlew :project:dependencyInsight --dependency commons-lang3 --configuration compileClasspath
./gradlew :project:dependencyInsight --dependency commons-lang3 --configuration runtimeClasspath
```

Beobachtung: Nach Aufgabe 1 ist `tools` eine `implementation`-Abhängigkeit von `library`. Deshalb braucht `library` sie zum Kompilieren, `project` aber noch nicht. Im `runtimeClasspath` von `project` taucht `tools` trotzdem auf, weil der Code zur Laufzeit gebraucht wird. Erst in Aufgabe 3 machst du `tools` zu einem Teil der öffentlichen API von `library`; dann muss `tools` auch im `compileClasspath` von `project` sichtbar werden.

## Aufgabe 3: `implementation` durch `api` ersetzen

**Ziel:** Du machst sichtbar, warum es in Gradle neben `implementation` auch `api` gibt. Eine Abhängigkeit, die Teil der öffentlichen Schnittstelle einer Library ist, muss für konsumierende Projekte auf dem Compile-Classpath sichtbar sein.

**Bedingung:** Das Hauptprogramm nutzt direkt einen Rückgabe-Typ aus `tools`. Mit `implementation(project(":tools"))` in `library` kompiliert `project` nicht mehr. Mit `api(project(":tools"))` kompiliert es wieder und gibt eine erneut veränderte Ausgabe aus.

Ändere die Klasse aus `tools` so, dass sie nicht nur intern in `library` genutzt wird, sondern als Rückgabe-Typ einer öffentlichen Methode aus `library` auftaucht.

Beispiel für eine Klasse in `tools`:

```java
package com.example.gradle.tools;

public class Message {

    private final String text;

    public Message(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

}
```

Beispiel für eine öffentliche Methode in `library`:

```java
package com.example.gradle.library;

import com.example.gradle.tools.Message;

public class GradleLibrary {

    public static Message createMessage() {
        return new Message("Hallo aus der Library");
    }

}
```

Nutze den Rückgabe-Typ anschließend direkt im Hauptprogramm:

```java
package com.example.gradle.project;

import com.example.gradle.library.GradleLibrary;
import com.example.gradle.tools.Message;

public class GradleProject {

    public static void main(String[] args) {
        Message message = GradleLibrary.createMessage();
        System.out.println(message.text() + " und aus dem Hauptprogramm");
    }

}
```

Prüfe zuerst, was mit `implementation` in `library` passiert:

```kotlin
dependencies {
    implementation(project(":tools"))
}
```

Danach ändere die Abhängigkeit in `library` auf `api`:

```kotlin
dependencies {
    api(project(":tools"))
}
```

Führe danach das Hauptprogramm erneut aus:

```bash
./gradlew :project:run
```

## Aufgabe 4: Prüfsummen für alle JARs erzeugen

**Ziel:** Du erstellst einen Task, der nach dem Bauen eines JARs eine MD5-Prüfsumme erzeugt. Dabei wiederholst du Task-Konfiguration, Task-Abhängigkeiten, `finalizedBy` und die gemeinsame Konfiguration mehrerer Subprojekte.

**Bedingung:** Nach `./gradlew clean assemble` liegt zu jedem erzeugten JAR auch eine `.md5`-Datei im jeweiligen `build/checksums` Verzeichnis. Beim zweiten Ausführen kannst du beobachten, welche Tasks erneut laufen und welche `UP-TO-DATE` sind.

Da alle Java-Subprojekte gleich behandelt werden sollen, ist `subprojects` im Root-Build-Skript hier sinnvoll. Konfiguriere den Checksum-Task zentral in `build.gradle.kts`.

Du kannst das Plugin aus dem Kurs verwenden:

```kotlin
plugins {
    id("org.gradle.crypto.checksum") version "1.4.0" apply false
}

subprojects {
    // ...

    plugins.withId("java") {
        apply(plugin = "org.gradle.crypto.checksum")

        val jar by tasks.getting

        val createChecksums by tasks.registering(org.gradle.crypto.checksum.Checksum::class) {
            dependsOn(jar)
            inputFiles = jar.outputs.files
            outputDirectory = layout.buildDirectory.dir("checksums")
            checksumAlgorithm.set(org.gradle.crypto.checksum.Checksum.Algorithm.MD5)
        }

        jar.finalizedBy(createChecksums)
    }
}
```

Führe anschließend aus:

```bash
./gradlew clean assemble
./gradlew assemble
```

Untersuche danach die erzeugten Dateien:

```bash
find . -path "*/build/checksums/*" -type f
```

Achte darauf, ob die Checksum-Tasks wirklich nach den `jar`-Tasks laufen. Ändere danach eine Java-Datei und führe `assemble` erneut aus. Achte auch darauf welche Tasks aufgerufen werden, wenn Du jetzt `run` ausführst.

## Aufgabe 5: Compiler-Abhängigkeiten in eine Datei schreiben

**Ziel:** Du schreibst einen einfachen eigenen Task, der die Compiler-Abhängigkeiten eines Projekts in eine Datei schreibt. Danach beobachtest du, warum Gradle sowohl Outputs als auch Inputs kennen muss.

**Bedingung:** Der Task erzeugt eine Datei mit den JARs des `compileClasspath`. Nur mit `outputs.file(...)` wird der Task übersprungen, obwohl sich Abhängigkeiten ändern. Erst mit `inputs.files(...)` und `outputs.file(...)` erkennt Gradle korrekt, wann die Datei neu geschrieben werden muss.

Füge diesen Task zuerst in `library/build.gradle.kts` ein. Der Task deklariert absichtlich nur den Output, aber noch nicht den `compileClasspath` als Input:

```kotlin
val writeCompileClasspath by tasks.registering {
    val compileClasspath = configurations.named("compileClasspath")
    val outputFile = layout.buildDirectory.file("reports/compile-classpath.txt")

    outputs.file(outputFile)

    doLast {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(
            compileClasspath.get().files
                .sortedBy { it.name }
                .joinToString(System.lineSeparator()) { it.absolutePath }
        )
    }
}
```

Führe den Task zweimal aus:

```bash
./gradlew :library:writeCompileClasspath
./gradlew :library:writeCompileClasspath
```

Beobachtung: Beim zweiten Lauf ist der Task `UP-TO-DATE`.

Ändere jetzt die Abhängigkeit in `tools/build.gradle.kts` von `implementation` auf `api`:

```kotlin
dependencies {
    api("org.apache.commons:commons-lang3:3.14.0")
}
```

Führe den Task danach erneut aus:

```bash
./gradlew :library:writeCompileClasspath
```

Untersuche die erzeugte Datei:

```bash
cat library/build/reports/compile-classpath.txt
```

Beobachtung: Der Task bleibt `UP-TO-DATE`, obwohl sich der `compileClasspath` von `library` geändert hat. `commons-lang3` müsste jetzt eigentlich in der Datei auftauchen, weil `tools` die Abhängigkeit über `api` weitergibt. Gradle kennt bisher aber nur die Output-Datei, nicht die Daten, aus denen diese Datei erzeugt wird.

Ergänze danach die Input-Deklaration:

```kotlin
val writeCompileClasspath by tasks.registering {
    val compileClasspath = configurations.named("compileClasspath")
    val outputFile = layout.buildDirectory.file("reports/compile-classpath.txt")

    inputs.files(compileClasspath) // <--- Input-Deklaration
    outputs.file(outputFile)

    doLast {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(
            compileClasspath.get().files
                .sortedBy { it.name }
                .joinToString(System.lineSeparator()) { it.absolutePath }
        )
    }
}
```

Führe den Task erneut zweimal aus:

```bash
./gradlew :library:writeCompileClasspath
./gradlew :library:writeCompileClasspath
```

Untersuche die erzeugte Datei erneut:

```bash
cat library/build/reports/compile-classpath.txt
```

Beobachtung: Der Task läuft jetzt wieder, weil Gradle die geänderten Inputs erkennt. Beim zweiten Lauf ist er wieder `UP-TO-DATE`.

Ändere anschließend die Abhängigkeit in `tools/build.gradle.kts` wieder zurück auf `implementation`:

```kotlin
dependencies {
    implementation("org.apache.commons:commons-lang3:3.14.0")
}
```

Führe den Task danach erneut aus:

```bash
./gradlew :library:writeCompileClasspath
```

Beobachtung: Sobald sich der `compileClasspath` ändert, wird die Datei neu geschrieben. `commons-lang3` verschwindet wieder aus der Datei. Danach ist der Task wieder `UP-TO-DATE`.

Entferne zum Schluss testweise die Output-Deklaration und lasse nur den Input stehen:

```kotlin
val writeCompileClasspath by tasks.registering {
    val compileClasspath = configurations.named("compileClasspath")
    val outputFile = layout.buildDirectory.file("reports/compile-classpath.txt")
    
    inputs.files(compileClasspath)
    // outputs.file(outputFile) // <--- Output-Deklaration

    doLast {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(
            compileClasspath.get().files
                .sortedBy { it.name }
                .joinToString(System.lineSeparator()) { it.absolutePath }
        )
    }
}
```

Führe den Task zweimal aus:

```bash
./gradlew :library:writeCompileClasspath
./gradlew :library:writeCompileClasspath
```

Beobachtung: Ohne Output-Deklaration läuft der Task jedes Mal. Das ist auch nicht das gewünschte Verhalten, weil Gradle nicht weiß, welches Ergebnis der Task erzeugt.
