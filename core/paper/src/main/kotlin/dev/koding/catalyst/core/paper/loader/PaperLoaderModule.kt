/*
 * Catalyst - Minecraft plugin development toolkit
 * Copyright (C) 2022  Koding Development
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
package dev.koding.catalyst.core.paper.loader

import cloud.commandframework.bukkit.CloudBukkitCapabilities
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.paper.PaperCommandManager
import dev.koding.catalyst.core.common.api.command.CatalystCommandManager
import dev.koding.catalyst.core.common.api.command.CommandSource
import dev.koding.catalyst.core.common.api.command.ConsoleCommandSource
import dev.koding.catalyst.core.common.api.platform.entity.PlatformPlayer
import dev.koding.catalyst.core.paper.api.platform.entity.unwrap
import dev.koding.catalyst.core.paper.api.platform.entity.wrap
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.delegate
import org.kodein.di.instance
import org.kodein.di.singleton

object PaperLoaderModule {
    fun of(plugin: PaperLoader) = DI.Module("PluginPaper-${plugin::class.java.name}") {
        // Plugin binding
        bind { instance(plugin) }
        delegate<JavaPlugin>().to<PaperLoader>()

        // Config
        bind { instance(plugin.config) }

        // Server
        bind { instance(plugin.server) }

        // Components
        bind { singleton { PaperComponentBootstrap(instance()) } }

        // Commands
        bind<CatalystCommandManager> {
            singleton {
                val commandManager = PaperCommandManager(
                    plugin,
                    AsynchronousCommandExecutionCoordinator.builder<CommandSource>().build(),
                    {
                        // Map from a Bukkit sender to a Catalyst CommandSource
                        val player = it as? Player ?: return@PaperCommandManager ConsoleCommandSource
                        player.wrap()
                    },
                    {
                        // Map from a Catalyst CommandSource to a Bukkit sender
                        when (it) {
                            is PlatformPlayer -> it.unwrap()
                            is ConsoleCommandSource -> plugin.server.consoleSender
                            else -> throw IllegalArgumentException("Unknown command source type: ${it::class.java.name}")
                        }
                    }
                )

                // Register with Brigadier
                commandManager.registerBrigadier()

                // Register capabilities
                if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                    commandManager.registerAsynchronousCompletions()
                }

                commandManager
            }
        }
    }
}
