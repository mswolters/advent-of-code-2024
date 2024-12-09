plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "nl.drbreakalot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(22)
}

application {
    mainClass.set("MainKt")
}