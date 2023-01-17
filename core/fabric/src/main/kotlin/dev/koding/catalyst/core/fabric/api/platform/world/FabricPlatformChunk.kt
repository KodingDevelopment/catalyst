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

import dev.koding.catalyst.core.common.api.platform.world.BlockMetadata
import dev.koding.catalyst.core.common.api.platform.world.PlatformBlock
import dev.koding.catalyst.core.common.api.platform.world.PlatformChunk
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.chunk.LevelChunk

class FabricPlatformChunk(
    private val ref: LevelChunk,
    override val world: PlatformWorld
) : PlatformChunk {
    override val x: Int
        get() = ref.pos.x

    override val z: Int
        get() = ref.pos.z

    override fun getBlock(x: Int, y: Int, z: Int): PlatformBlock {
        val position = BlockPos(x, y, z)
        return ref.getBlockState(position).block.wrap(position, this)
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: BlockMetadata) {
//        val position = BlockPos(x, y, z)
//        val state = Block.stateById(block.type)
//
//        ref.setBlockState(position)

        throw NotImplementedError()
    }
}

@Environment(EnvType.SERVER)
fun LevelChunk.wrap(world: PlatformWorld? = null) =
    FabricPlatformChunk(this, world ?: (this.level as ServerLevel).wrap())
