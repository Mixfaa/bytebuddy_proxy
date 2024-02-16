plugins {
    id("java")
    id("maven-publish")
}

group = "com.mixfa"
version = "1.3-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.bytebuddy:byte-buddy:1.14.12+")
}
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Mixfaa/bytebuddy_proxy")
            credentials {
                username = project.findProperty("github.username") as String
                password = project.findProperty("github.token") as String
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}



