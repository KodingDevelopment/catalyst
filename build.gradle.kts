@file:Suppress("UnstableApiUsage")

import gg.hubblemc.paper.PaperExtension

plugins {
    // Kotlin
    kotlin("jvm") version "1.7.10" apply false
    kotlin("kapt") version "1.7.10" apply false
    kotlin("plugin.lombok") version "1.7.10" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false

    // Other
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false

    // Custom gradle plugin
    id("gg.hubblemc.defaults") version "1.2.0"
    id("gg.hubblemc.linting") version "1.2.0"
    id("gg.hubblemc.paper") version "1.2.0" apply false
    id("gg.hubblemc.velocity") version "1.2.0" apply false

    // IDEA plugin
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.6"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    apply(plugin = "gg.hubblemc.defaults")
    apply(plugin = "gg.hubblemc.linting")

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    tasks {
        named<Test>("test") { useJUnitPlatform() }
    }

    repositories {
        mavenCentral()

        maven("https://jitpack.io")
        maven("https://libraries.minecraft.net")
        maven("https://repo.spongepowered.org/maven/")
        maven("https://maven.quiltmc.org/repository/release")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.rapture.pw/repository/maven-snapshots/")
    }

    dependencies {
        // Kotlin
        "api"(kotlin("stdlib-jdk8"))
        "testImplementation"(kotlin("test"))
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }

    pluginManager.withPlugin("gg.hubblemc.paper") {
        configure<PaperExtension> {
            mcVersion.set("1.19.2")
        }
    }
}
