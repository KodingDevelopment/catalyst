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

import dev.koding.catalyst.core.common.loader.PlatformLoader
import mu.KotlinLogging
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.kodein.di.DI
import java.nio.file.Path

/**
 * Root class for Fabric mods which implement Catalyst.
 */
@Suppress("unused")
open class FabricLoader(
    modId: String,
    override val modules: Array<DI.Module>
) : PlatformLoader, ModInitializer {

    constructor(modId: String, block: DI.Builder.() -> Unit) : this(
        modId,
        arrayOf(DI.Module("FabricModule-$modId", init = block))
    )

    val container = FabricLoader.getInstance().getModContainer(modId).get()

    override lateinit var di: DI

    override val rootModules by lazy { arrayOf(FabricLoaderModule.of(this)) }
    override var logger = KotlinLogging.logger(container.metadata.name)
    override var dataDirectory: Path = FabricLoader.getInstance().configDir

    override fun onInitialize(): Unit = enable()
}
