plugins {
    id("java-library")
    id("maven-publish")
}

group = "com.example"
version = "1.0-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("javaLibrary") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "intern"
            url = uri("http://localhost:8081/repository/intern/")
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "admin123"
            }
        }
    }
}

dependencies {
    api(project(":tools"))
    implementation("org.apache.commons:commons-collections4:4.4")
}

val writeCompileClasspath by tasks.registering {
    val compileClasspath = configurations.named("compileClasspath")
    val outputFile = layout.buildDirectory.file("reports/compile-classpath.txt")

    inputs.files(compileClasspath)
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
