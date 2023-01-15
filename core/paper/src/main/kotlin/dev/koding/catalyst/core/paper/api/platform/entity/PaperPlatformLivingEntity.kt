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
import dev.koding.catalyst.core.common.api.platform.entity.PlatformLivingEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class PaperPlatformLivingEntity(val ref: LivingEntity) :
    PlatformLivingEntity,
    PlatformEntity by (ref as Entity).wrapRaw() {
    override val health: Double get() = ref.health
}

fun LivingEntity.wrap() = PaperPlatformLivingEntity(this)
fun PlatformLivingEntity.unwrap() = (this as PaperPlatformLivingEntity).ref
