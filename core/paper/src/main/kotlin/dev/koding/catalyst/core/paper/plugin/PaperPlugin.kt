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

package dev.koding.catalyst.core.paper.plugin

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Injector
import dev.koding.catalyst.core.common.injection.module.PlatformModule
import dev.koding.catalyst.core.common.plugin.PlatformPlugin
import mu.KotlinLogging
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path

/**
 * Root plugin class for Paper plugins.
 */
open class PaperPlugin(override val modules: Array<AbstractModule>) : PlatformPlugin, JavaPlugin() {

    override lateinit var injector: Injector

    override val rootModules by lazy { arrayOf<AbstractModule>(PlatformModule(this), PaperPluginModule(this)) }
    override var logger = KotlinLogging.logger(slF4JLogger)
    override var dataDirectory: Path = dataFolder.toPath()

    @Inject
    lateinit var bootstrap: PaperComponentBootstrap

    override fun onEnable() {
        enable()
        bootstrap.enable()
    }

    override fun onDisable() {
        bootstrap.disable()
        disable()
    }
}
