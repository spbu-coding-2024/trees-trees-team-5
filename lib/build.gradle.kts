plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jetbrains.dokka") version "2.0.0"
}

group = "monke"
version = "1.0"

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.dokkaHtml {
    outputDirectory.set(file("$projectDir/docs"))
}

kotlin {
    jvmToolchain(23)
}