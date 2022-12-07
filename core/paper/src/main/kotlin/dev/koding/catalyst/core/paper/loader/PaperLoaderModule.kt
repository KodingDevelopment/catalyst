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

import dev.koding.catalyst.core.common.api.scheduler.Schedulers
import dev.koding.catalyst.core.paper.api.schedulers.PaperSchedulers
import org.bukkit.plugin.java.JavaPlugin
import org.kodein.di.*

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

        // API
        bind<Schedulers>(overrides = true) { singleton { PaperSchedulers(instance()) } }
    }
}
