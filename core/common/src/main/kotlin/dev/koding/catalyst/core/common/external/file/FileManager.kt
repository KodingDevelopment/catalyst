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

package dev.koding.catalyst.core.common.external.file

import com.google.inject.Inject
import com.google.inject.Singleton

@Singleton
class FileManager @Inject constructor(
    private val factory: ExternalFileFactory
) {

    /**
     * Returns a file with the given name.
     *
     * @param name The name of the file.
     * @return The file.
     */
    operator fun get(name: String) = factory.create(name)

}