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

package dev.koding.catalyst.core.common.api.platform.sided

import dev.koding.catalyst.core.common.api.platform.InstanceBinding

/**
 * The platform server class provides server specific implementations of the API.
 * We try to keep as much in the commons module as possible, but some things are
 * only available on the server.
 *
 * @author Koding
 */
interface PlatformServer {
    companion object : InstanceBinding<PlatformServer>()
}

object PlatformServerImpl : PlatformServer by PlatformServer.instance!!