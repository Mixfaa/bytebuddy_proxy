plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.Mixfaa"
version = "1.4-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.bytebuddy:byte-buddy:1.14.12+")
}