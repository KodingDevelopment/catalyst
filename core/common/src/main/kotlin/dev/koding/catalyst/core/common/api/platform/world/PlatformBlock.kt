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

import net.kyori.adventure.key.Key
import org.joml.Vector3i

/**
 * Stores information about a block within the world.
 * Its type is a "BlockMetadata" object which contains its type and properties.
 */
interface PlatformBlock {

    /**
     * The type of the block.
     */
    val type: BlockMetadata

    /**
     * The position of the block.
     */
    val position: BlockPos

}

/**
 * Stores the position of a block within the world.
 *
 * @param world The world the block is in.
 * @param pos The position of the block as a vector.
 */
data class BlockPos(val world: PlatformWorld, val pos: Vector3i) {
    constructor(world: PlatformWorld, x: Int, y: Int, z: Int) : this(world, Vector3i(x, y, z))
}

/**
 * Stores information about a block type.
 * This includes the type of the block and its properties as a map.
 *
 * TODO: In Bukkit allow wrapping a material as block metadata
 *
 * @param type The type of the block.
 * @param properties The properties of the block.
 */
data class BlockMetadata(
    val type: Key,
    val properties: Map<String, String>
)