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

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.kyori.adventure.platform.fabric.FabricServerAudiences
import net.minecraft.server.MinecraftServer
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance

/**
 * Root class for Fabric server mods which implement Catalyst.
 */
@Suppress("unused")
@Environment(EnvType.SERVER)
open class FabricDedicatedServerLoader(
    modId: String,
    modules: Array<DI.Module>
) : FabricLoaderBase(modId, modules), DedicatedServerModInitializer {

    companion object {
        /**
         * Create a module for the given plugin.
         *
         * @param plugin The plugin to create a module for.
         */
        @JvmStatic
        fun module(plugin: FabricDedicatedServerLoader) =
            DI.Module("FabricDedicatedServerLoader-${plugin::class.java.name}") {
                // MinecraftServer instance
                bind { instance(plugin.server) }

                // Audiences
                bind { instance(FabricServerAudiences.of(plugin.server)) }
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

    lateinit var server: MinecraftServer
        private set

    override val rootModules by lazy { arrayOf(FabricLoaderBase.module(this), module(this)) }

    override fun onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTING.register {
            server = it
            enable()
        }
    }
}
