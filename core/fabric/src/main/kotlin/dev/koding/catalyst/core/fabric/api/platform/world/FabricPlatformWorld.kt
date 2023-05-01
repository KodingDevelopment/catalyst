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
package dev.koding.catalyst.core.fabric.api.platform.world

import dev.koding.catalyst.core.common.api.platform.entity.PlatformEntity
import dev.koding.catalyst.core.common.api.platform.entity.PlatformPlayer
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import dev.koding.catalyst.core.fabric.api.platform.entity.player.wrap
import dev.koding.catalyst.core.fabric.api.platform.entity.player.wrapSmart
import dev.koding.catalyst.core.fabric.mixin.ServerLevelAccessor
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level

class FabricPlatformWorld(private val ref: ServerLevel) : PlatformWorld {
    override val name: String
        get() = ref.accessor().serverLevelData.levelName

    override val minY: Int
        get() = ref.minBuildHeight

    override val maxY: Int
        get() = ref.maxBuildHeight

    override val players: List<PlatformPlayer>
        get() = ref.players().map { it.wrap() }

    override val entities: List<PlatformEntity>
        get() = ref.allEntities.map { it.wrapSmart() }

    override fun getChunk(x: Int, z: Int) = ref.getChunkAt(BlockPos(x, 0, z)).wrap()
}

fun Level.wrap() = when (this) {
    is ServerLevel -> this.wrap()
    else -> throw NotImplementedError("Standard levels have not been implemented")
}

fun ServerLevel.wrap() = FabricPlatformWorld(this)

private fun ServerLevel.accessor() = this as ServerLevelAccessor
