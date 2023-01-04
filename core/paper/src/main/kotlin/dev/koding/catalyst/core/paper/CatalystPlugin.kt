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
package dev.koding.catalyst.core.paper

import dev.koding.catalyst.core.common.injection.component.Bootstrap
import dev.koding.catalyst.core.paper.loader.PaperLoader
import dev.koding.catalyst.core.paper.util.SpigotObf
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

@Suppress("unused")
class CatalystPlugin : PaperLoader({
    bind { singleton { CoreBootstrap(instance()) } }
})

class CoreBootstrap(override val di: DI) : DIAware, Bootstrap {
    override fun enable() {
        SpigotObf.load()
    }
}
