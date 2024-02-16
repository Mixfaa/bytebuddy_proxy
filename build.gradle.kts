plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.Mixfaa"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.bytebuddy:byte-buddy:1.14.12+")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {

    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}



