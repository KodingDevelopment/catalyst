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
import dev.koding.catalyst.core.common.api.platform.entity.PlatformPlayer
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import dev.koding.catalyst.core.common.util.UnsupportedPlatformException
import java.util.UUID

/**
 * The platform server class provides server specific implementations of the API.
 * We try to keep as much in the commons module as possible, but some things are
 * only available on the server.
 *
 * @author Koding
 */
interface PlatformServer {
    companion object : PlatformBinding<PlatformServer>()

    /**
     * A list of all worlds on the server.
     */
    val worlds: List<PlatformWorld> get() = throw UnsupportedPlatformException()

    /**
     * A list of all players on the server.
     */
    val players: List<PlatformPlayer> get() = throw UnsupportedPlatformException()

    /**
     * Get a player by its UUID.
     *
     * @param uuid The UUID of the player.
     * @return The player, or null if it doesn't exist.
     */
    fun getPlayer(uuid: UUID): PlatformPlayer? = throw UnsupportedPlatformException()

    /**
     * Get a player by its name, ignoring case.
     *
     * @param name The name of the player.
     * @return The player, or null if it doesn't exist.
     */
    fun getPlayer(name: String): PlatformPlayer? = throw UnsupportedPlatformException()

    /**
     * Get a world by its name.
     *
     * @param name The name of the world.
     * @return The world, or null if it doesn't exist.
     */
    fun getWorld(name: String): PlatformWorld? = throw UnsupportedPlatformException()
}

object PlatformServerImpl : PlatformServer by PlatformServer.instance!!
