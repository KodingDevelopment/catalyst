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

import dev.koding.catalyst.core.common.api.network.codec.PacketCodec
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

/**
 * A packet registry is used to associate packets with their appropriate
 * identifier, such as a Key or [Int].
 *
 * @param TYPE The type of the packet object.
 * @param ID The type of the packet identifier.
 */
class PacketRegistry<TYPE : Any, ID : Any>(
    private val codec: PacketCodec<TYPE>,
    private val idEncoder: PrimitiveEncoder<ID>
) {

    /**
     * A map of packet classes to their identifiers.
     */
    private val packets = hashMapOf<Class<out TYPE>, ID>()

    /**
     * A map of packet identifiers to their classes.
     */
    private val identifiers = hashMapOf<ID, Class<out TYPE>>()

    /**
     * A map of packet identifiers to their serializers. Not all packets
     * are based on KotlinX serialization, so this is optional and may not
     * be required for the specific packet codec.
     */
    private val serializers = hashMapOf<ID, KSerializer<out TYPE>>()

    /**
     * Registers a packet with the given ID, packet class, and serializer.
     * The serializer is optional and may not be required for the specific
     * packet codec, and is nullable.
     *
     * @param id The identifier of the packet.
     * @param packet The class of the packet.
     * @param serializer The serializer of the packet.
     */
    @JvmOverloads
    fun <T : TYPE> register(id: ID, packet: Class<T>, serializer: KSerializer<T>? = null) {
        packets[packet] = id
        identifiers[id] = packet
        serializer?.also { serializers[id] = it }
    }

    /**
     * Registers a packet with the given ID and packet class.
     * This is a convenience method to automatically infer the
     * serializer from the packet class.
     *
     * @param id The identifier of the packet.
     */
    inline fun <reified T : TYPE> register(id: ID): Unit =
        register(id, T::class.java, serializer())

    /**
     * Encodes the given packet object into a byte array.
     *
     * @param packet The packet object to encode.
     * @return The encoded packet.
     */
    @Suppress("UNCHECKED_CAST")
    fun encode(packet: TYPE): ByteArray {
        // Fetch the packet's identifier
        val id = packets[packet::class.java]
            ?: throw IllegalArgumentException("Packet ${packet::class.java} is not registered")

        // Encode the packet
        val encoded = codec.encode(packet, serializers[id] as KSerializer<TYPE>?)

        // Pack the packet identifier
        val packed = idEncoder.encode(id)

        // Pack the packet
        // 1 byte for ID length, 2 bytes for data length, (UNKNOWN) bytes for ID, (UNKNOWN) bytes for packet
        val packedPacket = ByteArray(3 + packed.size + encoded.size)

        // Write the length of the header
        packedPacket[0] = packed.size.toByte()

        // Write the length of the packet as a short
        packedPacket[1] = (encoded.size ushr 8).toByte()
        packedPacket[2] = encoded.size.toByte()

        // Write the packet data
        packed.copyInto(packedPacket, 3)
        encoded.copyInto(packedPacket, 3 + packed.size)

        return packedPacket
    }

    /**
     * Decodes the given packet data into a packet object.
     *
     * @param data The packet data to decode.
     * @return The decoded packet.
     */
    @Suppress("UNCHECKED_CAST")
    fun decode(data: ByteArray): TYPE {
        // Read the lengths of the packet
        val headerLength = data[0].toInt()
        val packetLength = (data[1].toInt() shl 8) or (data[2].toInt() and 0xFF)

        // Read the packet identifier
        val id = idEncoder.decode(data.copyOfRange(3, 3 + headerLength))

        // Read the packet data
        val packetData = data.copyOfRange(3 + headerLength, 3 + headerLength + packetLength)

        // Get the packet class
        val packetClass = identifiers[id]
            ?: throw IllegalArgumentException("Received packet with unknown ID $id")

        // Decode the packet
        return codec.decode(packetData, packetClass as Class<TYPE>, serializers[id] as KSerializer<TYPE>?)
    }
}
