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
package dev.koding.catalyst.core.fabric.loader

import net.fabricmc.api.EnvType
import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import net.fabricmc.loader.api.FabricLoader as FabricLoaderAPI

object FabricLoaderModule {

    @Suppress("DEPRECATION")
    fun of(plugin: FabricLoader) = DI.Module("PluginFabric-${plugin::class.java.name}") {
        // Plugin binding
        bind { instance(plugin) }

        // Meta
        bind { instance(plugin.container) }

        // Instances
        when (FabricLoaderAPI.getInstance().environmentType) {
            EnvType.CLIENT -> bind { instance(Minecraft.getInstance()) }
            EnvType.SERVER -> bind { instance(FabricLoaderAPI.getInstance().gameInstance as MinecraftServer) }
            else -> error("Unknown environment type")
        }

        // TODO: Components
    }
}
