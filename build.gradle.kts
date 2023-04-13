/*
 * Catalyst - Minecraft plugin development toolkit
 * Copyright (C) 2022  Koding Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:Suppress("UnstableApiUsage")

import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    // Kotlin
    kotlin("jvm") version "1.8.20" apply false
    kotlin("kapt") version "1.8.20" apply false
    kotlin("plugin.serialization") version "1.8.20" apply false

    // Other
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false

    // Custom gradle plugin
    id("gg.hubblemc.defaults") version "2.1.1"
    id("gg.hubblemc.linting") version "2.1.1"
    id("gg.hubblemc.paper") version "2.1.1" apply false
    id("gg.hubblemc.velocity") version "2.1.1" apply false

    // IDEA plugin
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.6"

    // Fabric
    id("dev.architectury.loom") version "1.1.329" apply false
    id("io.github.juuxel.loom-quiltflower") version "1.8.0" apply false
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    apply(plugin = "gg.hubblemc.defaults")
    apply(plugin = "gg.hubblemc.linting")

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    if (project.name != "core-fabric") {
        // Workaround for "fabric-loom must be applied BEFORE kapt in the plugins { } block."
        apply(plugin = "org.jetbrains.kotlin.kapt")
    }

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

    configure<SpotlessExtension> {
        isEnforceCheck = false
    }
}
