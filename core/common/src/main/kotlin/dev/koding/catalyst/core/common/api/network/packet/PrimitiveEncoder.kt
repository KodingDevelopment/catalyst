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
package dev.koding.catalyst.core.common.api.network.packet

import net.kyori.adventure.key.Key

/**
 * A packet encoder encodes and decodes primitive types into a byte array.
 * This is used by the packet header to encode and decode packet identifiers.
 *
 * @param T The type of primitive to encode and decode.
 */
interface PrimitiveEncoder<T> {
    /**
     * Encodes the given primitive into a byte array.
     *
     * @param primitive The primitive to encode.
     * @return The encoded primitive.
     */
    fun encode(primitive: T): ByteArray

    /**
     * Decodes the given byte array into a primitive.
     *
     * @param bytes The byte array to decode.
     * @return The decoded primitive.
     */
    fun decode(bytes: ByteArray): T
}

/**
 * Integer primitive encoder.
 */
object IntegerEncoder : PrimitiveEncoder<Int> {
    override fun encode(primitive: Int): ByteArray =
        byteArrayOf(
            (primitive shr 24).toByte(),
            (primitive shr 16).toByte(),
            (primitive shr 8).toByte(),
            primitive.toByte()
        )

    override fun decode(bytes: ByteArray): Int =
        (bytes[0].toInt() shl 24) or
            (bytes[1].toInt() shl 16) or
            (bytes[2].toInt() shl 8) or
            bytes[3].toInt()
}

/**
 * Short primitive encoder.
 */
object ShortEncoder : PrimitiveEncoder<Short> {
    override fun encode(primitive: Short): ByteArray =
        byteArrayOf(
            (primitive.toInt() shr 8).toByte(),
            primitive.toByte()
        )

    override fun decode(bytes: ByteArray): Short =
        ((bytes[0].toInt() shl 8) or bytes[1].toInt()).toShort()
}

/**
 * Byte primitive encoder.
 */
object ByteEncoder : PrimitiveEncoder<Byte> {
    override fun encode(primitive: Byte): ByteArray = byteArrayOf(primitive)
    override fun decode(bytes: ByteArray): Byte = bytes[0]
}

/**
 * String primitive encoder.
 */
object StringEncoder : PrimitiveEncoder<String> {
    override fun encode(primitive: String): ByteArray = primitive.toByteArray()
    override fun decode(bytes: ByteArray): String = String(bytes)
}

/**
 * Key primitive encoder
 */
object KeyEncoder : PrimitiveEncoder<Key> {
    override fun encode(primitive: Key): ByteArray = primitive.asString().toByteArray()
    override fun decode(bytes: ByteArray): Key = Key.key(String(bytes))
}
