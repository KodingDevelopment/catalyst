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
package dev.koding.catalyst.core.paper.loader

import dev.koding.catalyst.core.common.injection.bootstrap.ComponentBootstrap
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.allInstances
import org.kodein.di.direct
import org.kodein.di.instance

/**
 * Registers Listeners with the Bukkit API.
 */
class PaperComponentBootstrap(override val di: DI) : DIAware, ComponentBootstrap {

    private val plugin: PaperLoader by di.instance()

    override fun bind() {
        di.direct.allInstances<Listener>().forEach { Bukkit.getPluginManager().registerEvents(it, plugin) }
    }

    override fun unbind() {
        di.direct.allInstances<Listener>().forEach { HandlerList.unregisterAll(it) }
    }
}
