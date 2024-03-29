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
package dev.koding.catalyst.core.paper.util.ext

import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import dev.koding.catalyst.core.paper.api.platform.world.unwrap
import org.bukkit.Location
import org.joml.Vector2f
import org.joml.Vector3d

/**
 * Converts a [Vector3d] to a [Location] in the given [PlatformWorld].
 *
 * @param world The world to convert to
 * @param look the look vector to use for the location
 */
fun Vector3d.asLocation(world: PlatformWorld, look: Vector2f? = null): Location = Location(world.unwrap(), x, y, z, look?.x ?: 0f, look?.y ?: 0f)
