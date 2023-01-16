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
@file:OptIn(ExperimentalSerializationApi::class)

package dev.koding.catalyst.core.common.api.network.codec

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * Encodes and decodes packet data using the Kotlinx Protobuf library.
 *
 * @param T The type of base object to encode and decode.
 */
class KotlinxProtobufCodec<T : Any>(private val protobuf: ProtoBuf = ProtoBuf) : PacketCodec<T> {
    override fun encode(obj: T, serializer: KSerializer<T>?): ByteArray =
        protobuf.encodeToByteArray(serializer ?: throw CodecException("No serializer provided"), obj)

    override fun decode(bytes: ByteArray, clazz: Class<T>, serializer: KSerializer<T>?): T =
        protobuf.decodeFromByteArray(serializer ?: throw CodecException("No serializer provided"), bytes)
}
