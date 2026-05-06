plugins {
    id("org.gradle.crypto.checksum") version "1.4.0" apply false
}

subprojects {
    repositories {
        mavenCentral()
    }

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
