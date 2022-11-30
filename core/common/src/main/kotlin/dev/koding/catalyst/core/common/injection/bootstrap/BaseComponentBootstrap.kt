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

import dev.koding.catalyst.core.common.injection.component.Bootstrap
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.allInstances
import org.kodein.di.direct

/**
 * Base class for implementations to automatically register their platform specific components to
 * the server implementation.
 *
 * For example - registering command listeners on Bukkit.
 */
class BaseComponentBootstrap(override val di: DI) : DIAware, ComponentBootstrap {
    override fun bind() {
        di.direct.allInstances<Bootstrap>().sortedByDescending { it.priority }.forEach { it.enable() }
    }

    override fun unbind() {
        di.direct.allInstances<Bootstrap>().sortedBy { it.priority }.forEach { it.disable() }
    }
}

interface ComponentBootstrap {
    fun bind()
    fun unbind()
}