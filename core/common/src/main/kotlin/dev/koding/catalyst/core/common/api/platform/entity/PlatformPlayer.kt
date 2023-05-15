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

import dev.koding.catalyst.core.common.api.command.CommandSource
import dev.koding.catalyst.core.common.util.UnsupportedPlatformException
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.TriState

/**
 * Abstraction for a player, representing a human entity in the world.
 * Builds on top of [PlatformLivingEntity] to provide additional functionality.
 * Implements the [Audience] interface to allow for sending messages to the player.
 */
interface PlatformPlayer : PlatformLivingEntity, ForwardingAudience, CommandSource {

    /**
     * The player's name
     */
    override val name: String

    /**
     * The player's display name as a component
     */
    val displayName: Component get() = Component.text(name)

    //region CommandSource
    override fun value(permission: String?): TriState = throw UnsupportedPlatformException()
    //endregion

}
