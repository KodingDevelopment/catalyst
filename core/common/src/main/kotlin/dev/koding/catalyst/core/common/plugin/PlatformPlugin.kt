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

package dev.koding.catalyst.core.common.plugin

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import dev.koding.catalyst.core.common.injection.component.Bootstrap
import dev.koding.catalyst.core.common.injection.module.DummyModule
import mu.KLogger
import java.nio.file.Path
import kotlin.system.measureTimeMillis

interface PlatformPlugin : Bootstrap {

    /**
     * Root modules are used to provide the base bindings for the injector.
     * They are always loaded first, and should only be overridden by the platform
     * implementation and not by the plugin.
     */
    val rootModules get() = arrayOf<AbstractModule>()

    /**
     * Plugin-provided modules are loaded after the root modules, and are used to
     * provide the plugin's bindings.
     */
    val modules: Array<AbstractModule>

    // TODO: Config and data directory

    /**
     * The injector used to provide the plugin's bindings. Since we're in an interface,
     * we can't use lateinit, so we have to use a getter and setter.
     */
    var injector: Injector

    /**
     * The logger used by the plugin.
     */
    var logger: KLogger

    /**
     * The data directory used by the plugin.
     */
    var dataDirectory: Path

    // TODO: Injection context
    override fun enable() {
        try {
            logger.info { "Starting injection" }
            val duration = measureTimeMillis {
                injector = Guice.createInjector(*modules, *rootModules, DummyModule)
                injector.injectMembers(this)
            }

            // TODO: Register injection context using ClassloaderLocal (tm)
            logger.info { "Injection complete in ${duration}ms" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to inject" }
        }
    }

    // TODO: Disabling
}
