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

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.kyori.adventure.platform.fabric.FabricClientAudiences
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance

/**
 * Root class for Fabric client mods which implement Catalyst.
 */
@Suppress("unused")
@Environment(EnvType.CLIENT)
open class FabricClientLoader(
    modId: String,
    modules: Array<DI.Module>
) : FabricLoaderBase(modId, modules), ClientModInitializer {

    companion object {
        /**
         * Create a module for the given plugin.
         *
         * @param plugin The plugin to create a module for.
         */
        @JvmStatic
        fun module(plugin: FabricClientLoader) =
            DI.Module("FabricClientLoader-${plugin::class.java.name}") {
                // Audiences
                bind { instance(FabricClientAudiences.of()) }
            }
    }

    /**
     * Secondary constructor to ease module creation.
     *
     * @param modId The mod ID of the plugin.
     * @param block The block to initialize the module.
     */
    constructor(modId: String, block: DI.Builder.() -> Unit) : this(
        modId,
        arrayOf(DI.Module("FabricModule-$modId", init = block))
    )

    override val rootModules by lazy { arrayOf(FabricLoaderBase.module(this), module(this)) }

    override fun onInitializeClient(): Unit = enable()
}
