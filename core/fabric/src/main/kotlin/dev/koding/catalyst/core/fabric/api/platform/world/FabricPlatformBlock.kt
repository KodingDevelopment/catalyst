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
import dev.koding.catalyst.core.common.api.platform.world.BlockPos
import dev.koding.catalyst.core.common.api.platform.world.PlatformBlock
import dev.koding.catalyst.core.common.api.platform.world.PlatformChunk
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import net.kyori.adventure.key.Key
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3i
import net.minecraft.core.BlockPos as MCBlockPos

class FabricPlatformBlock(
    private val ref: Block,
    override val position: BlockPos,
    override val chunk: PlatformChunk
) : PlatformBlock {
    override val type: BlockMetadata
        get() = ref.defaultBlockState().wrap()
}

fun Block.wrap(position: MCBlockPos, chunk: PlatformChunk) =
    FabricPlatformBlock(this, position.wrap(chunk.world), chunk)

fun MCBlockPos.wrap(world: PlatformWorld) = BlockPos(world, Vector3i(x, y, z))
fun BlockState.wrap(): BlockMetadata = BlockMetadata(Key.key(this.block.descriptionId))
