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
package dev.koding.catalyst.core.paper.api.platform.entity

import dev.koding.catalyst.core.common.api.platform.entity.PlatformEntity
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import dev.koding.catalyst.core.paper.api.platform.world.wrap
import dev.koding.catalyst.core.paper.util.ext.asLocation
import net.kyori.adventure.key.Key
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.joml.Vector3d
import java.util.UUID

class PaperPlatformEntity(val ref: Entity) : PlatformEntity {
    override val type: Key get() = ref.type.key
    override val uuid: UUID get() = ref.uniqueId
    override val id: Int get() = ref.entityId

    override val position: Vector3d get() = Vector3d(ref.location.x, ref.location.y, ref.location.z)
    override val world: PlatformWorld get() = ref.world.wrap()

    override fun teleport(position: Vector3d, world: PlatformWorld?): Boolean =
        ref.teleport(position.asLocation(world ?: this.world))
}

internal fun Entity.wrapRaw(): PaperPlatformEntity = PaperPlatformEntity(this)
fun PlatformEntity.unwrap(): Entity = (this as PaperPlatformEntity).ref

/**
 * Performs smart wrapping of a Bukkit entity into a Catalyst entity
 * of the correct type.
 */
fun Entity.wrap(): PlatformEntity = when (this) {
    is Player -> wrap()
    is LivingEntity -> wrap()
    else -> wrapRaw()
}
