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
package dev.koding.catalyst.core.fabric

import dev.koding.catalyst.core.common.injection.component.Bootstrap
import dev.koding.catalyst.core.common.util.ext.logExecutionTime
import dev.koding.catalyst.core.fabric.api.platform.FabricPlatform
import dev.koding.catalyst.core.fabric.loader.FabricClientLoader
import dev.koding.catalyst.core.fabric.loader.FabricDedicatedServerLoader
import mu.KLogger
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

/**
 * Base module for both the Fabric Client and Server
 */
private val module = DI.Module("CatalystCoreModule") {
    bind { singleton { CoreBootstrap(instance()) } }
}

@Suppress("unused")
object CatalystModClient : FabricClientLoader("catalyst", arrayOf(module))

@Suppress("unused")
object CatalystModServer : FabricDedicatedServerLoader("catalyst", arrayOf(module))

/**
 * Initializes the Fabric platform.
 */
class CoreBootstrap(override val di: DI) : DIAware, Bootstrap {
    override val priority: Int = Int.MAX_VALUE
    private val logger by instance<KLogger>()

    override fun enable() {
        logger.logExecutionTime({ info { "Loaded platform in ${it}ms" } }) { FabricPlatform(di).init() }
    }
}
