plugins {
    id("java")
}

group = "com.mixfa"
version = "1.4-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.bytebuddy:byte-buddy:1.14.12+")
}