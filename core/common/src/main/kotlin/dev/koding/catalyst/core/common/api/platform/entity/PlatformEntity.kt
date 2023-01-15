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
package dev.koding.catalyst.core.common.api.platform.entity

import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import dev.koding.catalyst.core.common.util.InvalidPlatformException
import net.kyori.adventure.identity.Identified
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.key.Key
import org.joml.Vector3d
import java.util.UUID

/**
 * Abstraction for an entity, representing a living or non-living object in the world.
 * This should assume the entity is in the world in most cases, however there is a
 * field to check if the entity is valid.
 *
 * TODO: Implement
 *
 * @author Koding
 */
interface PlatformEntity : Identified {
    /**
     * The type of the entity (e.g. minecraft:player)
     */
    val type: Key

    /**
     * The unique ID of the entity
     */
    val uuid: UUID

    /**
     * The entity's ID
     */
    val id: Int

    /**
     * Current position of the entity
     */
    val position: Vector3d

    /**
     * Current world of the entity
     */
    val world: PlatformWorld

    /**
     * Teleports the entity to the given position
     *
     * @param position The position to teleport to
     * @param world The world to teleport to
     * @return True if the teleport was successful
     */
    fun teleport(position: Vector3d, world: PlatformWorld? = null): Boolean = throw InvalidPlatformException()

    /*
     * == Identified ==
     */
    override fun identity(): Identity = Identity.identity(uuid)

    class Type {
        companion object {
            @JvmStatic
            val PLAYER = Key.key("minecraft", "player")
        }
    }
}
