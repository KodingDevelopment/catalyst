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

package dev.koding.catalyst.core.common.injection.module

import dev.koding.catalyst.core.common.external.file.FileModule
import dev.koding.catalyst.core.common.injection.component.DataDirectory
import dev.koding.catalyst.core.common.injection.component.PluginLogger
import dev.koding.catalyst.core.common.plugin.PlatformPlugin
import mu.KLogger
import org.slf4j.Logger
import java.nio.file.Path

/**
 * Bind the plugin & logger to Guice.
 */
class PlatformModule(private val plugin: PlatformPlugin) : Module() {
    override fun configure() {
        bind<PlatformPlugin>().toInstance(plugin)
        bind<Logger>().annotatedWith<PluginLogger>().toInstance(plugin.logger)
        bind<KLogger>().annotatedWith<PluginLogger>().toInstance(plugin.logger)
        bind<Path>().annotatedWith<DataDirectory>().toInstance(plugin.dataDirectory)

        // Default functionality
        install(FileModule())
    }
}
