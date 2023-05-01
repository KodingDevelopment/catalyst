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
package dev.koding.catalyst.core.common.api.platform.world

import dev.koding.catalyst.core.common.api.platform.entity.PlatformEntity
import dev.koding.catalyst.core.common.api.platform.entity.PlatformEntityType
import dev.koding.catalyst.core.common.api.platform.entity.PlatformPlayer
import dev.koding.catalyst.core.common.util.UnsupportedPlatformException

/**
 * Abstracts away an instance of a single "world" from a platform. This is
 * equivalent to something like the Bukkit "World" object or Minestom's
 * "Instance" object.
 *
 * A world can hold a set of entities, alongside perform lookups for chunk
 * data and read specific properties of the world.
 *
 * These properties are not guaranteed to be available on all platforms.
 *
 * @author Koding
 */
interface PlatformWorld {

    /**
     * The name of the world.
     */
    val name: String?

    /**
     * The minimum y coordinate of the world. Defaults to 0.
     */
    val minY: Int get() = 0

    /**
     * The maximum y coordinate of the world. Defaults to 255.
     */
    val maxY: Int get() = 255

    /**
     * A list of all the entities in the world.
     */
    val entities: List<PlatformEntity> get() = throw UnsupportedPlatformException()

    /**
     * A list of all the players in the world.
     */
    val players: List<PlatformPlayer>
        get() = entities
            .filter { it.type == PlatformEntityType.PLAYER }
            .map { it as PlatformPlayer }

    /**
     * Retrieves a chunk at the given position in the world.
     * The X and Z position are in chunk coordinates.
     *
     * @param x The X position of the chunk.
     * @param z The Z position of the chunk.
     * @return The chunk at the given position, or null if it has not been generated yet.
     */
    fun getChunk(x: Int, z: Int): PlatformChunk?

    /**
     * Gets the block at the given position in the world.
     *
     * @param x The X position of the block.
     * @param y The Y position of the block.
     * @param z The Z position of the block.
     * @return The block at the given position, or null if it is outside the world bounds.
     */
    fun getBlock(x: Int, y: Int, z: Int): PlatformBlock? =
        getChunk(x shr 4, z shr 4)?.getBlock(x and 0xF, y, z and 0xF)

    /**
     * Sets the block type at the given position in the world.
     *
     * @param x The X position of the block.
     * @param y The Y position of the block.
     * @param z The Z position of the block.
     * @param metadata The metadata of the block.
     */
    fun setBlock(x: Int, y: Int, z: Int, metadata: BlockMetadata) =
        getChunk(x shr 4, z shr 4)?.setBlock(x and 0xF, y, z and 0xF, metadata)
}
