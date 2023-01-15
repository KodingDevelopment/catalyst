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
package dev.koding.catalyst.core.common.api.platform

import dev.koding.catalyst.core.common.api.platform.sided.PlatformClient
import dev.koding.catalyst.core.common.api.platform.sided.PlatformCommon
import dev.koding.catalyst.core.common.api.platform.sided.PlatformServer
import dev.koding.catalyst.core.common.api.scheduler.Schedulers
import dev.koding.catalyst.core.common.util.InvalidPlatformException

/**
 * The platform class instantiates all the necessary APIs for the given platform.
 * The [init] function can be called once by the platform's core plugin to
 * bind the static bindings, such as a Server or Scheduler implementation, alongside storing
 * an instance of the platform class.
 *
 * @author Koding
 */
interface Platform {

    companion object : InstanceBinding<Platform>()

    /**
     * Static binding for the PlatformServer class, which is used to provide
     * server specific implementations of the API.
     */
    val server: PlatformServer get() = throw InvalidPlatformException()

    /**
     * Static binding for the PlatformClient class, which is used to provide
     * client specific implementations of the API.
     */
    val client: PlatformClient get() = throw InvalidPlatformException()

    /**
     * Platform commons, which are used to provide common implementations of the API.
     * This is used to provide implementations that are not specific to the server or client.
     */
    val common: PlatformCommon get() = throw InvalidPlatformException()

    /**
     * Static binding for the PlatformScheduler class, equivalent to the
     * BukkitScheduler class.
     */
    val schedulers: Schedulers

    /**
     * Binds the static bindings and stores an instance of the platform class.
     */
    fun init() {
        instance = this

        // Bind platform
        PlatformServer.instance = server
        PlatformClient.instance = client
        PlatformCommon.instance = common
    }
}

/**
 * Each platform-specific API will provide a relevant object class to
 * grab the instance of the API. This is a convenience feature to
 * avoid having to use the [Platform] class and get the instance each time.
 */
object PlatformImpl : Platform by Platform.instance!!

/**
 * Object class to access the bound schedulers.
 */
object SchedulersImpl : Schedulers by PlatformImpl.schedulers

/**
 * Simple utility which can be used to bind an instance of a class to a static field.
 * It ensures that the instance is only set once and that it is not null.
 *
 * This is useful for binding a platform's implementation of an API to the API class, or
 * allowing any of the static bindings to be accessed by a delegate object class.
 *
 * @author Koding
 */
open class InstanceBinding<T> {
    var instance: T? = null
        get() = field ?: throw IllegalStateException("Instance not bound")
        set(value) {
            if (field != null) throw IllegalStateException("Instance already set")
            field = value
        }
}
