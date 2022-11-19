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

package dev.koding.catalyst.core.common.injection.bootstrap

import com.google.inject.Inject
import dev.koding.catalyst.core.common.injection.component.Bootstrap
import dev.koding.catalyst.core.common.injection.component.Injectable

/**
 * Base class for implementations to automatically register their platform specific components to
 * the server implementation.
 *
 * For example - registering command listeners on Bukkit.
 */
open class ComponentBootstrap @Inject constructor(val components: Set<Injectable>) : Bootstrap {
    override val priority = Int.MAX_VALUE

    override fun enable() {
        get<Bootstrap>().sortedByDescending { it.priority }.forEach { it.enable() }
    }

    override fun disable() {
        get<Bootstrap>().sortedByDescending { it.priority }.forEach { it.disable() }
    }

    /**
     * Runs a for loop with the given [apply] block on each component matching the given type.
     *
     * @param apply The block to run on each component.
     */
    inline fun <reified T> bootstrap(apply: (T) -> Unit) =
        components.filterIsInstance<T>().forEach(apply)

    /**
     * Returns a list of all components matching the given type.
     */
    inline fun <reified T> get(): List<T> = components.filterIsInstance<T>()
}