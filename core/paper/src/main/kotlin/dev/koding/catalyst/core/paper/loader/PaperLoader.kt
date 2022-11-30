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
package dev.koding.catalyst.core.paper.loader

import dev.koding.catalyst.core.common.loader.PlatformLoader
import mu.KotlinLogging
import org.bukkit.plugin.java.JavaPlugin
import org.kodein.di.DI
import java.nio.file.Path

/**
 * Root plugin class for Paper plugins.
 */
open class PaperLoader(override val modules: Array<DI.Module>) : PlatformLoader, JavaPlugin() {

    constructor(block: DI.Builder.() -> Unit) : this(arrayOf(DI.Module("PaperPlugin", init = block)))

    override lateinit var di: DI

    override val rootModules by lazy { arrayOf(PaperLoaderModule.of(this)) }
    override var logger = KotlinLogging.logger(slF4JLogger)
    override var dataDirectory: Path = dataFolder.toPath()

    override fun onEnable(): Unit = enable()
    override fun onDisable(): Unit = disable()
}
