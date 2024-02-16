plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.Mixfaa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.bytebuddy:byte-buddy:1.14.12")
}

java {
    withSourcesJar()
}

publishing {

    publications {
        register<MavenPublication>("bytebuddy_proxy") {
            from(components["java"])
        }
    }
}



