plugins {
    id("java")
    id("application")
}

repositories {
    maven {
        url = uri("http://localhost:8081/repository/intern/")
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
    implementation(project(":library")) // "com.example:library:1.0-SNAPSHOT"
}

application {
    mainClass = "com.example.gradle.project.GradleProject"
}
