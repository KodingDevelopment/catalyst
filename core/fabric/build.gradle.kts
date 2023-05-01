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

import net.fabricmc.loom.api.LoomGradleExtensionAPI

apply(plugin = "quiet-fabric-loom")

repositories {
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")

    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    // Fabric
    "minecraft"("com.mojang:minecraft:${rootProject.property("fabric.minecraft_version")}")
    "mappings"(project.the<LoomGradleExtensionAPI>().officialMojangMappings())

    // Fabric API
    "modImplementation"("net.fabricmc:fabric-loader:${rootProject.property("fabric.loader_version")}")
    "modApi"("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric.api_version")}")

    // Catalyst
    "api"(project(":core-common"))
    "include"(project(":core-common"))

    // Kotlin
    "modImplementation"("net.fabricmc:fabric-language-kotlin:1.8.6+kotlin.1.7.21")

    // Adventure API
    "modImplementation"("include"("net.kyori:adventure-platform-fabric:5.5.1")!!)

    // Mods
    "modRuntimeOnly"("me.djtheredstoner:DevAuth-fabric:1.1.0")
    "modRuntimeOnly"("maven.modrinth:modmenu:4.1.1")
}

configure<LoomGradleExtensionAPI> {
    runConfigs {
        all {
            isIdeConfigGenerated = true
        }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }
}
