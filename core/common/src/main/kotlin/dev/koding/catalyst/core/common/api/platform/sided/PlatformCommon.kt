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
package dev.koding.catalyst.core.common.api.platform.sided

import dev.koding.catalyst.core.common.api.platform.PlatformBinding
import dev.koding.catalyst.core.common.util.UnsupportedPlatformException

/**
 * The platform server class provides server specific implementations of the API.
 * We try to keep as much in the commons module as possible, but some things are
 * only available on the server.
 *
 * @author Koding
 */
interface PlatformCommon {
    companion object : PlatformBinding<PlatformCommon>()

    /**
     * Register an event listener for the given event class.
     *
     * @param owner The owner of the listener, allows for unregistration.
     * @param clazz The event class.
     * @param priority The priority of the event.
     * @param ignoreCancelled Whether to ignore cancelled events.
     * @param listener The listener.
     */
    fun <T : Any> registerEventListener(
        owner: Any,
        clazz: Class<T>,
        priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false,
        listener: (T) -> Unit
    ): Unit = throw UnsupportedPlatformException()
}

/**
 * The priority of an event listener. Run order is from lowest to highest,
 * and MONITOR is always last.
 */
enum class EventPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST,
    MONITOR
}

object PlatformCommonImpl : PlatformCommon by PlatformCommon.instance!!
