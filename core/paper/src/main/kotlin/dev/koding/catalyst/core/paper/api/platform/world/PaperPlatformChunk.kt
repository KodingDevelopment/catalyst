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
package dev.koding.catalyst.core.paper.api.platform.world

import dev.koding.catalyst.core.common.api.platform.world.BlockMetadata
import dev.koding.catalyst.core.common.api.platform.world.PlatformBlock
import dev.koding.catalyst.core.common.api.platform.world.PlatformChunk
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import org.bukkit.Chunk

/**
 * Implementation of a Paper chunk on top of the Catalyst API.
 */
class PaperPlatformChunk(override val world: PlatformWorld, val ref: Chunk) : PlatformChunk {
    override val x: Int get() = ref.x
    override val z: Int get() = ref.z

    override fun getBlock(x: Int, y: Int, z: Int): PlatformBlock {
        checkPos(x, y, z)
        return ref.getBlock(x, y, z).wrap(this)
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: BlockMetadata) {
        checkPos(x, y, z)
        ref.getBlock(x, y, z).blockData = block.unwrap()
    }
}

/**
 * Wraps a Paper chunk into a Catalyst chunk.
 *
 * @param world Optional world to pass, allows reusing the world instance.
 */
fun Chunk.wrap(world: PlatformWorld? = null): PaperPlatformChunk = PaperPlatformChunk(world ?: this.world.wrap(), this)

/**
 * Unwraps a Catalyst chunk into a Paper chunk.
 */
fun PlatformChunk.unwrap(): Chunk = (this as PaperPlatformChunk).ref
