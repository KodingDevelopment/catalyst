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

import dev.koding.catalyst.core.common.api.platform.entity.PlatformLivingEntity
import dev.koding.catalyst.core.common.api.platform.entity.PlatformPlayer
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.TriState
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class PaperPlatformPlayer(val ref: Player) : PlatformLivingEntity by (ref as LivingEntity).wrap(), PlatformPlayer {

    override val name: String get() = ref.name
    override val displayName: Component get() = ref.displayName()

    /* == ForwardingAudience == */
    override fun audiences(): MutableIterable<Audience> = mutableListOf(ref)

    /* == PermissionChecker == */
    override fun value(permission: String?): TriState {
        if (permission == null || !ref.isPermissionSet(permission)) return TriState.NOT_SET
        return TriState.byBoolean(ref.hasPermission(permission))
    }
}

fun Player.wrap() = PaperPlatformPlayer(this)
fun PlatformPlayer.unwrap() = (this as PaperPlatformPlayer).ref
