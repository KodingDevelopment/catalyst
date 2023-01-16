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

import net.kyori.adventure.platform.fabric.FabricServerAudiences
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance

object FabricLoaderModule {
    @Suppress("DEPRECATION")
    fun of(plugin: FabricDedicatedServerLoader) = DI.Module("PluginFabric-${plugin::class.java.name}") {
        // Plugin binding
        bind { instance(plugin) }

        // Meta
        bind { instance(plugin.container) }

        // MinecraftServer instance
        bind { instance(plugin.server) }

        // Audiences
        bind { instance(FabricServerAudiences.of(plugin.server)) }

        // TODO: Components
    }
}
