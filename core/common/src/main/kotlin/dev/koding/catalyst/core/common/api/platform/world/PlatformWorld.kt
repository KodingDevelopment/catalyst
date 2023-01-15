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
     * The minimum y coordinate of the world. Defaults to 0.
     */
    val minY: Int get() = 0

    /**
     * The maximum y coordinate of the world. Defaults to 255.
     */
    val maxY: Int get() = 255

    /**
     * Retrieves a chunk at the given position in the world.
     * The X and Z position are in chunk coordinates.
     *
     * @param x The X position of the chunk.
     * @param z The Z position of the chunk.
     * @return The chunk at the given position, or null if it has not been generated yet.
     */
    fun getChunk(x: Int, z: Int): PlatformChunk?

}