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
package dev.koding.catalyst.core.common.api.command

import cloud.commandframework.CommandManager
import cloud.commandframework.exceptions.InvalidCommandSenderException
import dev.koding.catalyst.core.common.api.platform.entity.PlatformPlayer
import dev.koding.catalyst.core.common.api.platform.sided.PlatformServerImpl
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.util.TriState

/**
 * Represents a source of a command. This can be a player or console.
 */
interface CommandSource : PermissionChecker, ForwardingAudience {
    /**
     * The name of the command source.
     */
    val name: String
}

/**
 * Represents the console as a command source.
 */
object ConsoleCommandSource : CommandSource,
    PermissionChecker by PermissionChecker.always(TriState.TRUE),
    ForwardingAudience by PlatformServerImpl {
    override val name: String = "CONSOLE"
}

/**
 * Get the source as a player, throwing an exception if the source is not a player.
 *
 * @throws IllegalStateException if the source is not a player.
 * @return The source as a player.
 */
@Suppress("unused")
fun CommandSource.asPlayer(): PlatformPlayer = this as? PlatformPlayer
    ?: throw InvalidCommandSenderException(this, PlatformPlayer::class.java, emptyList())

/**
 * Type-alias for a command manager that uses [CommandSource] as the sender type.
 */
typealias CatalystCommandManager = CommandManager<CommandSource>