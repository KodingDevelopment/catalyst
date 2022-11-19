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

package dev.koding.catalyst.core.paper.plugin

import com.google.inject.Inject
import com.google.inject.Singleton
import dev.koding.catalyst.core.common.injection.bootstrap.ComponentBootstrap
import dev.koding.catalyst.core.common.injection.component.Injectable
import org.bukkit.Bukkit
import org.bukkit.event.Listener

/**
 * Registers Listeners with the Bukkit API.
 */
@Singleton
class PaperComponentBootstrap @Inject constructor(
    components: Set<Injectable>,
    private val plugin: PaperPlugin
) : ComponentBootstrap(components) {

    override fun enable() {
        super.enable()
        bootstrap<Listener> { Bukkit.getPluginManager().registerEvents(it, plugin) }
    }
}