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
package dev.koding.catalyst.core.common.loader

import dev.koding.catalyst.core.common.injection.PluginModule
import dev.koding.catalyst.core.common.injection.bootstrap.ComponentBootstrap
import dev.koding.catalyst.core.common.util.ext.logExecutionTime
import mu.KLogger
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.allInstances
import org.kodein.di.direct
import org.kodein.di.jxinject.jxInjectorModule
import java.nio.file.Path

interface PlatformLoader : DIAware {

    /**
     * Root modules are used to provide the base bindings for the injector.
     * They are always loaded first, and should only be overridden by the platform
     * implementation and not by the plugin.
     */
    val rootModules get() = arrayOf<DI.Module>()

    /**
     * Plugin-provided modules are loaded after the root modules, and are used to
     * provide the plugin's bindings.
     */
    val modules: Array<DI.Module>

    // TODO: Config and data directory

    /**
     * The injector used to provide the plugin's bindings. Since we're in an interface,
     * we can't use lateinit, so we have to use a getter and setter.
     */
    override var di: DI

    /**
     * The logger used by the plugin.
     */
    var logger: KLogger

    /**
     * The data directory used by the plugin.
     */
    var dataDirectory: Path

    /**
     * Creates the dependency injector for the plugin, and runs all
     * component bootstraps.
     */
    fun enable() {
        try {
            logger.info { "Starting injection" }

            logger.logExecutionTime({ info { "Injection complete in ${it}ms" } }) {
                di = DI {
                    // Backwards compat
                    import(jxInjectorModule)

                    // Install modules
                    import(PluginModule.of(this@PlatformLoader), allowOverride = true)
                    importAll(*rootModules, *modules, allowOverride = true)
                }

                di.direct.allInstances<ComponentBootstrap>().forEach { it.bind() }
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to inject" }
        }
    }

    /**
     * Disables the plugin, and runs all component shutdowns.
     */
    fun disable() {
        di.direct.allInstances<ComponentBootstrap>().forEach { it.unbind() }
    }
}
