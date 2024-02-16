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

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    version = JavaVersion.VERSION_17
}

publishing {

    publications {
        register<MavenPublication>("bytebuddy_proxy") {
            from(components["java"])
        }
    }
}



