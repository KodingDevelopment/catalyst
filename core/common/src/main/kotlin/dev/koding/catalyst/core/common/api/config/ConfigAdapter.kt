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
package dev.koding.catalyst.core.common.api.config

import java.io.InputStream
import java.io.OutputStream

/**
 * Encodes and decodes config files
 */
interface ConfigAdapter {

    /**
     * The extension of the config file
     */
    val extension: String

    /**
     * Encode the given [data] into the given [io] stream
     *
     * @param io The output stream to encode to
     * @param data The data to encode
     * @param clazz The class of the data
     */
    fun <T : Any> encode(io: OutputStream, data: T, clazz: Class<out T>)

    /**
     * Decode the given [io] stream into the given [clazz]
     *
     * @param io The input stream to decode
     * @param clazz The class to decode into
     */
    fun <T : Any> decode(io: InputStream, clazz: Class<T>): T

}