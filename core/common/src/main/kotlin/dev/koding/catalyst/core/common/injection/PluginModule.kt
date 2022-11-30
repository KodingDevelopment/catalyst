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
package dev.koding.catalyst.core.common.injection

import dev.koding.catalyst.core.common.injection.component.Tags
import dev.koding.catalyst.core.common.loader.PlatformLoader
import mu.KLogger
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.delegate
import org.kodein.di.instance
import org.kodein.di.registerContextFinder
import org.kodein.di.singleton
import org.slf4j.Logger

object PluginModule {
    fun of(plugin: PlatformLoader) = DI.Module("PluginBase-${plugin::class.java.name}") {
        // Register context
        registerContextFinder { plugin }

        // Bind DI instance
        bind { singleton { plugin.di } }
        bind { instance(plugin) }

        // Logger
        bind { instance(plugin.logger) }
        delegate<Logger>().to<KLogger>()

        // Data directory
        bind(tag = Tags.DATA_DIRECTORY) { instance(plugin.dataDirectory) }
    }
}
