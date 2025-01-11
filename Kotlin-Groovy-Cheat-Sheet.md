# Kotlin -> Groovy - Cheat Sheet

## Plugins
Kotlin:
```kotlin
plugins {
    id("java")
    id("application")
}
```

Groovy:
```groovy
plugins {
    id 'java'
    id 'application'
}
```

## Repositories
Kotlin:
```kotlin
repositories {
    mavenCentral()
    maven { url = uri("http://localhost:8081/repository/maven-intern/") }
}
```

Groovy:
```groovy
repositories {
    mavenCentral()
    maven { url = uri('http://localhost:8081/repository/maven-intern/') }
}
```

## Dependencies
Kotlin:
```kotlin
dependencies {
    implementation("org.slf4j:slf4j-api:1.7.32")
    api("org.apache.commons:commons-lang3:3.12.0")
    runtimeOnly("com.google.guava:guava:30.1-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}
```

Kotlin (map-Variante, unüblich):
```kotlin
dependencies {
    implementation(mapOf("group" to "org.slf4j", "name" to "slf4j-api", "version" to "1.7.32")) 
}
```

Groovy:
```groovy
dependencies {
    implementation 'org.slf4j:slf4j-api:1.7.32'
    api 'org.apache.commons:commons-lang3:3.12.0'
    runtimeOnly 'com.google.guava:guava:30.1-jre'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}
```

Groovy (benannte Parameter):
```groovy
dependencies {
    implementation group: 'org.slf4j', name: 'slf4j-api', version: '1.7.32'
    api group: 'org.apache.commons', name: 'commons-lang3', version: '3.12.0'
    runtimeOnly group: 'com.google.guava', name: 'guava', version: '30.1-jre'
    testImplementation group: 'org.junit.jupiter', name: 'junit-jupiter', version: '5.10.0'
}
```

## Tasks erstellen
Kotlin:
```kotlin
tasks.register("custom") {
    doFirst { println("Anfang!") }
    doLast { println("Ende!") }
}
```

Groovy:
```groovy
tasks.register('custom') {
    doFirst { println 'Anfang!' }
    doLast { println 'Ende!' }
}
```

## Tasks verändern
Kotlin:
```kotlin
tasks.named("jar") {
    doLast { println("Jar gebaut") }
}
```

Groovy:
```groovy
tasks.named('jar') {
    doLast { println 'Jar gebaut' }
}
```

## Task-Abhängigkeiten (mit Variablen)
Kotlin:
```kotlin
val a by tasks.registering
val b by tasks.registering {
    dependsOn(a)
    mustRunAfter(a)
    finalizedBy(a)
}
```

Groovy:
```groovy
def a = tasks.register('a')
def b = tasks.register('b') {
    dependsOn a
    mustRunAfter a
    finalizedBy a
}
```

## Application Plugin (Main-Class)
Kotlin:
```kotlin
application {
    mainClass = "com.example.Main"
}
```

Groovy:
```groovy
application {
    mainClass = 'com.example.Main'
}
```

## Jar Manifest
Kotlin:
```kotlin
tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.example.Main",
            "Class-Path" to "lib/commons-text-1.13.0.jar"
        )
    }
}
```

Groovy:
```groovy
jar {
    manifest {
        attributes(
            'Main-Class': 'com.example.Main',
            'Class-Path': 'lib/commons-text-1.13.0.jar'
        )
    }
}
```

## Project Properties / `gradle.properties`
Kotlin:
```kotlin
val env = project.findProperty("env")?.toString() ?: "development"
```

Groovy:
```groovy
def env = project.findProperty('env') ?: 'development'
```

## Extra Properties
Kotlin:
```kotlin
extra["myVar"] = "value"
val v = extra["myVar"]
```

Groovy:
```groovy
ext.myVar = 'value'
def v = myVar
```

## Konfigurationen (custom)
Kotlin:
```kotlin
configurations {
    create("codegen") {
        isCanBeConsumed = false
        isCanBeResolved = true
    }
}
```

Groovy:
```groovy
configurations {
    codegen {
        canBeConsumed = false
        canBeResolved = true
    }
}
```
