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
package dev.koding.catalyst.core.common.api.network.codec

import kotlinx.serialization.KSerializer

/**
 * A packet codec defines how to convert an object into a byte array and vice versa.
 *
 * @param T The type of the object to encode/decode.
 */
interface PacketCodec<T> {

    /**
     * Encodes the given object into a byte array.
     *
     * @param obj The object to encode.
     * @param serializer The serializer to use for encoding if applicable.
     *
     * @return The encoded byte array.
     * @throws CodecException If the object could not be encoded.
     */
    @Throws(CodecException::class)
    fun encode(obj: T, serializer: KSerializer<T>?): ByteArray

    /**
     * Decodes the given byte array into an object.
     *
     * @param bytes The byte array to decode.
     * @param clazz The class of the object to decode.
     * @param serializer The serializer to use for decoding if applicable.
     *
     * @return The decoded object.
     * @throws CodecException If the byte array could not be decoded.
     */
    @Throws(CodecException::class)
    fun decode(bytes: ByteArray, clazz: Class<T>, serializer: KSerializer<T>?): T
}

class CodecException(message: String) : Exception(message)
