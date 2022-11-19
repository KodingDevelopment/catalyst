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

package dev.koding.catalyst.core.paper.plugin

import dev.koding.catalyst.core.common.injection.module.Module
import org.bukkit.Server
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class PaperPluginModule(private val plugin: PaperPlugin) : Module() {
    override fun configure() {
        bind<JavaPlugin>().toInstance(plugin)
        bind<PaperPlugin>().toInstance(plugin)

        bind<FileConfiguration>().toInstance(plugin.config)
        bind<Server>().toInstance(plugin.server)
    }
}