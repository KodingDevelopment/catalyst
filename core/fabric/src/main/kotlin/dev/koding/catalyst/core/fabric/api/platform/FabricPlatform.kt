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
package dev.koding.catalyst.core.fabric.api.platform

import dev.koding.catalyst.core.common.api.platform.Platform
import dev.koding.catalyst.core.common.api.scheduler.DefaultSchedulers
import dev.koding.catalyst.core.common.api.scheduler.Schedulers
import dev.koding.catalyst.core.fabric.api.platform.sided.FabricPlatformClient
import dev.koding.catalyst.core.fabric.api.platform.sided.FabricPlatformServer
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import org.kodein.di.DI

/**
 * The Fabric implementation of the [Platform] interface.
 * Fabric supports both Client and Server, so this class is used for both.
 *
 * @param di The dependency injection container.
 */
class FabricPlatform(di: DI) : Platform {

    /**
     * Creates a [FabricPlatformClient] if the current environment is a client, otherwise null.
     */
    override val client by lazy {
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            FabricPlatformClient(di)
        } else null
    }

    /**
     * Creates a [FabricPlatformServer] if the current environment is a server, otherwise null.
     */
    override val server by lazy {
        if (FabricLoader.getInstance().environmentType == EnvType.SERVER) {
            FabricPlatformServer(di)
        } else null
    }

    override val schedulers: Schedulers = DefaultSchedulers
}
