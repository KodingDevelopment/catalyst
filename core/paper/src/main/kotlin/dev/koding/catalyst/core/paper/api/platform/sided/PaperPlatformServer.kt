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

import dev.koding.catalyst.core.common.api.platform.entity.PlatformPlayer
import dev.koding.catalyst.core.common.api.platform.sided.PlatformServer
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import dev.koding.catalyst.core.paper.api.platform.entity.wrap
import dev.koding.catalyst.core.paper.api.platform.world.wrap
import net.kyori.adventure.audience.Audience
import org.bukkit.Bukkit
import java.util.UUID

/**
 * Implementation of the Paper API on top of the Catalyst API.
 */
class PaperPlatformServer : PlatformServer {
    override val worlds: List<PlatformWorld>
        get() = Bukkit.getWorlds().map { it.wrap() }
    override val players: List<PlatformPlayer>
        get() = Bukkit.getOnlinePlayers().map { it.wrap() }

    override fun getPlayer(name: String): PlatformPlayer? = Bukkit.getPlayer(name)?.wrap()
    override fun getPlayer(uuid: UUID): PlatformPlayer? = Bukkit.getPlayer(uuid)?.wrap()
    override fun getWorld(name: String): PlatformWorld? = Bukkit.getWorld(name)?.wrap()

    override fun audiences(): MutableIterable<Audience> = mutableListOf(Bukkit.getConsoleSender())
}
