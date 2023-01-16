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
package dev.koding.catalyst.core.fabric.api.platform.sided

import dev.koding.catalyst.core.common.api.platform.entity.PlatformPlayer
import dev.koding.catalyst.core.common.api.platform.sided.PlatformServer
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import dev.koding.catalyst.core.fabric.api.platform.entity.player.wrap
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.kyori.adventure.platform.fabric.FabricServerAudiences
import net.minecraft.server.MinecraftServer
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.util.UUID

/**
 * The Fabric implementation of the [PlatformServer] interface.
 */
@Environment(EnvType.SERVER)
class FabricPlatformServer(override val di: DI) : DIAware, PlatformServer {

    companion object {
        @JvmStatic
        val instance: FabricPlatformServer by lazy { PlatformServer.instance as FabricPlatformServer }
    }

    /*
     * Injected dependencies
     */
    private val server by instance<MinecraftServer>()

    val audiences by instance<FabricServerAudiences>()

    override val worlds: List<PlatformWorld>
        get() = TODO("Worlds are not implemented on Fabric yet")

    override val players: List<PlatformPlayer>
        get() = runCatching { server.playerList.players.map { it.wrap() } }
            .getOrDefault(emptyList()) // Sometimes the playerList can be null

    override fun getPlayer(uuid: UUID) = players.firstOrNull { it.uuid == uuid }

    override fun getPlayer(name: String) =
        server.playerList.players
            .firstOrNull { it.name.string == name }
            ?.wrap()

    override fun getWorld(name: String): PlatformWorld? {
        TODO("Worlds are not implemented on Fabric yet")
    }
}
