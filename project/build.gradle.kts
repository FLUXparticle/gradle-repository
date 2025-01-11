plugins {
    id("java")
    id("application")
}

repositories {
    maven {
        url = uri("http://localhost:8081/repository/maven-intern/")
        isAllowInsecureProtocol = true
        mavenContent {
            snapshotsOnly()
        }
        credentials {
            username = "user"
            password = "user123"
        }
    }
}

dependencies {
    implementation("com.example:library:1.0-SNAPSHOT")
}

application {
    mainClass = "com.example.maven.project.MavenProject"
}