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

/**
 * A chunk represents a range of blocks from the world's floor to the world's ceiling.
 * Depending on the world settings, the height may vary.
 *
 * It ensures mutable access to the blocks within the chunk.
 *
 * @author Koding
 */
interface PlatformChunk {

    /**
     * The x coordinate of the chunk.
     */
    val x: Int

    /**
     * The z coordinate of the chunk.
     */
    val z: Int

    /**
     * The world this chunk belongs to.
     */
    val world: PlatformWorld

    /**
     * Validates whether the requested block is within the bounds of the chunk.
     *
     * This includes:
     *  - A range of 0 to 15 for the x coordinate.
     *  - A range of the world floor to the world ceiling for the y coordinate.
     *  - A range of 0 to 15 for the z coordinate.
     *
     * @param x The x coordinate of the block.
     * @param y The y coordinate of the block.
     * @param z The z coordinate of the block.
     * @throws IllegalArgumentException If the block is not within the bounds of the chunk.
     */
    fun checkPos(x: Int, y: Int, z: Int) {
        require(x in 0..15) { "X coordinate must be between 0 and 15" }
        require(y in world.minY..world.maxY) { "Y coordinate must be between ${world.minY} and ${world.maxY}" }
        require(z in 0..15) { "Z coordinate must be between 0 and 15" }
    }

    /**
     * Retrieves the block at the given position.
     *
     * @param x The x coordinate of the block.
     * @param y The y coordinate of the block.
     * @param z The z coordinate of the block.
     * @return The block at the given position.
     * @throws IllegalArgumentException If the block is not within the bounds of the chunk.
     */
    fun getBlock(x: Int, y: Int, z: Int): PlatformBlock

    /**
     * Sets the block at the given position.
     *
     * @param x The x coordinate of the block.
     * @param y The y coordinate of the block.
     * @param z The z coordinate of the block.
     * @param block The block to set.
     * @throws IllegalArgumentException If the block is not within the bounds of the chunk.
     */
    fun setBlock(x: Int, y: Int, z: Int, block: PlatformBlock)
}
