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

import dev.koding.catalyst.core.common.api.platform.sided.PlatformServer
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import dev.koding.catalyst.core.paper.api.platform.world.wrap
import org.bukkit.Bukkit

/**
 * Implementation of the Paper API on top of the Catalyst API.
 */
class PaperPlatformServer : PlatformServer {
    override val worlds: List<PlatformWorld>
        get() = Bukkit.getWorlds().map { it.wrap() }
}
