/*
 * Catalyst - Minecraft plugin development toolkit
 * Copyright (C) 2023  Koding Development
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

import dev.koding.catalyst.core.common.loader.PlatformLoader
import mu.KotlinLogging
import net.fabricmc.loader.api.FabricLoader
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import java.nio.file.Path

/**
 * Base class for Fabric mods which implement Catalyst.
 */
abstract class FabricLoaderBase(
    modId: String,
    override val modules: Array<DI.Module>
) : PlatformLoader {

    companion object {
        /**
         * Create a new module for the plugin.
         *
         * @param plugin The plugin to create the module for.
         */
        @JvmStatic
        fun module(plugin: FabricLoaderBase) = DI.Module("FabricLoaderBase-${plugin.container.metadata.name}") {
            // Plugin binding
            bind { instance(plugin) }

            // Meta
            bind { instance(plugin.container) }
        }
    }

    /**
     * The container of the mod.
     */
    private val container = FabricLoader.getInstance().getModContainer(modId).get()

    override lateinit var di: DI

    override var logger = KotlinLogging.logger(container.metadata.name)
    override var dataDirectory: Path = FabricLoader.getInstance().configDir
}
