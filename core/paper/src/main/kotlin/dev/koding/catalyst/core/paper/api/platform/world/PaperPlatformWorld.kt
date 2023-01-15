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

import dev.koding.catalyst.core.common.api.platform.world.PlatformChunk
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import org.bukkit.World

/**
 * Implementation for a Paper world.
 */
class PaperPlatformWorld(val ref: World) : PlatformWorld {

    override val name: String get() = ref.name
    override val minY: Int get() = ref.minHeight
    override val maxY: Int get() = ref.maxHeight

    /**
     * Get a chunk at the given chunk coordinates, this will never
     * return null as Paper generates chunks on demand.
     */
    override fun getChunk(x: Int, z: Int): PlatformChunk =
        ref.getChunkAt(x, z).wrap(this)
}

/**
 * Wraps a Paper world into a Catalyst world.
 */
fun World.wrap(): PaperPlatformWorld = PaperPlatformWorld(this)

/**
 * Unwraps a Catalyst world into a Paper world.
 */
fun PlatformWorld.unwrap(): World = (this as PaperPlatformWorld).ref
