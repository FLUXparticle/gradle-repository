plugins {
    id("java")
    id("maven-publish")
}

group = "com.example"
version = "1.0-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "intern"
            url = uri("http://localhost:8081/repository/maven-intern/")
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "admin123"
            }
        }
    }
}
