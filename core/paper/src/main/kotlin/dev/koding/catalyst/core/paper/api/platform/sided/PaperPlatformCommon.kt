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
package dev.koding.catalyst.core.paper.api.platform.sided

import dev.koding.catalyst.core.common.api.platform.sided.EventPriority
import dev.koding.catalyst.core.common.api.platform.sided.PlatformCommon
import dev.koding.catalyst.core.paper.CatalystPlugin
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.kodein.di.DI
import org.kodein.di.instance
import org.bukkit.event.EventPriority as BukkitEventPriority

/**
 * Implementation of the Paper API on top of the Catalyst API.
 */
class PaperPlatformCommon(di: DI) : PlatformCommon {

    private val plugin by di.instance<CatalystPlugin>()

    /**
     * Stores the [WrappedListener] for each owner.
     */
    private val listenerRegistry = mutableMapOf<Any, WrappedListener>()

    override fun <T : Any> registerEventListener(
        owner: Any,
        clazz: Class<T>,
        priority: EventPriority,
        ignoreCancelled: Boolean,
        listener: (T) -> Unit
    ) {
        // We need to wrap the listener into a Bukkit listener so that we can unregister it later.
        val listenerImpl = listenerRegistry.getOrPut(owner) { WrappedListener(owner) }

        @Suppress("UNCHECKED_CAST")
        Bukkit.getServer().pluginManager.registerEvent(
            clazz as Class<Event>, listenerImpl, priority.asBukkit(),
            { _, event -> listener(event as T) },
            plugin, ignoreCancelled
        )
    }
}

/**
 * We need to wrap a specific class into a listener so that we can unregister it later. Bukkit
 * requires all parents to inherit the [Listener] class, whereas other platforms don't.
 *
 * We create a cache of classes wrapped into listeners so that, when that parent is asked to
 * unregister events, we can unregister it through a lookup.
 *
 * @author Koding
 */
data class WrappedListener(val parent: Any) : Listener

private fun EventPriority.asBukkit(): org.bukkit.event.EventPriority =
    when (this) {
        EventPriority.LOWEST -> BukkitEventPriority.LOWEST
        EventPriority.LOW -> BukkitEventPriority.LOW
        EventPriority.NORMAL -> BukkitEventPriority.NORMAL
        EventPriority.HIGH -> BukkitEventPriority.HIGH
        EventPriority.HIGHEST -> BukkitEventPriority.HIGHEST
        EventPriority.MONITOR -> BukkitEventPriority.MONITOR
    }
