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
import dev.koding.catalyst.core.common.util.UnsupportedPlatformException
import net.kyori.adventure.identity.Identified
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.key.Key
import org.joml.Vector2f
import org.joml.Vector3d
import java.util.UUID

/**
 * Abstraction for an entity, representing a living or non-living object in the world.
 * This should assume the entity is in the world in most cases, however there is a
 * field to check if the entity is valid.
 *
 * @author Koding
 */
interface PlatformEntity : Identified {
    /**
     * The type of the entity (e.g. minecraft:player)
     */
    val typeId: Key

    /**
     * The enum type of the entity
     */
    val type: PlatformEntityType
        get() = PlatformEntityType.fromKey(typeId) ?: throw IllegalArgumentException("Unknown entity type $typeId")

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
     * Current look vector of the entity
     * The X component is the yaw, and the Y component is the pitch
     */
    val look: Vector2f

    /**
     * Current world of the entity
     */
    val world: PlatformWorld

    /**
     * Teleports the entity to the given position
     *
     * @param position The position to teleport to
     * @param look The look vector to teleport to, if null the entity's current look vector will be used
     * @param world The world to teleport to, if null the entity's current world will be used
     * @return True if the teleport was successful
     */
    fun teleport(position: Vector3d, look: Vector2f? = null, world: PlatformWorld? = null): Boolean =
        throw UnsupportedPlatformException()

    /*
     * == Identified ==
     */
    override fun identity(): Identity = Identity.identity(uuid)
}

/**
 * Represents a type of entity
 */
enum class PlatformEntityType(val key: Key) {
    PLAYER(Key.key("player"));

    companion object {
        /**
         * Get a [PlatformEntityType] from its [Key]
         *
         * @param key The key of the entity type
         * @return The entity type, or null if it doesn't exist
         */
        fun fromKey(key: Key): PlatformEntityType? = values().find { it.key == key }
    }
}