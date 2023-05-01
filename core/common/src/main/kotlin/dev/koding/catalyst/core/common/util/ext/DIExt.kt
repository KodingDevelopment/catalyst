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
package dev.koding.catalyst.core.common.util.ext

import dev.koding.catalyst.core.common.api.platform.sided.EventPriority
import dev.koding.catalyst.core.common.api.platform.sided.PlatformCommonImpl
import org.kodein.di.DIAware

/**
 * Register an event listener for the given event class.
 *
 * @param owner The owner of the listener, allows for unregistration. Defaults to the current [DIAware].
 * @param clazz The event class.
 * @param priority The priority of the event.
 * @param ignoreCancelled Whether to ignore cancelled events.
 * @param listener The listener.
 */
fun <T : Any> DIAware.on(
    clazz: Class<T>,
    owner: Any = this,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    listener: (T) -> Unit
): Unit = PlatformCommonImpl.registerEventListener(owner, clazz, priority, ignoreCancelled, listener)

/**
 * Register an event listener for the given event class.
 *
 * @param owner The owner of the listener, allows for unregistration. Defaults to the current [DIAware].
 * @param priority The priority of the event.
 * @param ignoreCancelled Whether to ignore cancelled events.
 * @param listener The listener.
 */
inline fun <reified T : Any> DIAware.on(
    owner: Any = this,
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    noinline listener: (T) -> Unit
): Unit = on(T::class.java, owner, priority, ignoreCancelled, listener)
