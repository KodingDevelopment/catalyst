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
package dev.koding.catalyst.core.fabric.api.platform.entity.player

import dev.koding.catalyst.core.common.api.platform.entity.PlatformEntity
import dev.koding.catalyst.core.common.api.platform.world.PlatformWorld
import net.kyori.adventure.key.Key
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import org.joml.Vector2f
import org.joml.Vector3d
import java.util.UUID

class FabricPlatformEntity(val ref: Entity) : PlatformEntity {
    private val entityKey = EntityType.getKey(ref.type)
    override val type = Key.key(entityKey.namespace, entityKey.path)

    override val uuid: UUID = ref.uuid
    override val id: Int = ref.id

    override val position: Vector3d
        get() = Vector3d(ref.position().x, ref.position().y, ref.position().z)

    override val look: Vector2f
        get() = Vector2f(ref.lookAngle.x.toFloat(), ref.lookAngle.y.toFloat())

    override val world: PlatformWorld get() = TODO() // ref.level.wrap()

    override fun teleport(position: Vector3d, look: Vector2f?, world: PlatformWorld?): Boolean {
        // TODO: Use world and look
        ref.teleportTo(position.x, position.y, position.z)
        return true
    }
}

internal fun Entity.wrapRaw(): FabricPlatformEntity = FabricPlatformEntity(this)
fun PlatformEntity.unwrap(): Entity = (this as FabricPlatformEntity).ref

/**
 * Performs smart wrapping of a entity into a Catalyst entity of the correct type.
 */
fun Entity.wrap(): PlatformEntity = when (this) {
    is Player -> wrap()
    is LivingEntity -> wrap()
    else -> wrapRaw()
}
