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

@file:OptIn(ExperimentalSerializationApi::class)

package dev.koding.catalyst.core.common.api.config.impl

import dev.koding.catalyst.core.common.api.config.ConfigProvider
import dev.koding.catalyst.core.common.util.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import java.io.File

/**
 * Adapts a [ConfigProvider] to support a [JsonObject].
 *
 * @author Koding
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class JsonConfigProvider(
    private var obj: JsonObject,
    override val path: String = ""
) : ConfigProvider {

    companion object {
        /**
         * Create a [JsonConfigProvider] from the given text.
         *
         * @param text The text to parse.
         */
        fun from(text: String) = JsonConfigProvider(json.parseToJsonElement(text).jsonObject)

        /**
         * Create a [JsonConfigProvider] from the given file.
         *
         * @param file The file to parse.
         */
        fun from(file: File) = from(file.readText())
    }

    override val keys: Set<String> = obj.keys

    override fun contains(key: String) = key in obj

    override fun set(key: String, value: Any?) {
        // We need to create a new json object, as the old one is immutable
        obj = JsonObject(obj.toMutableMap().apply { this[key] = json.encodeToJsonElement(value) })
    }

    override fun getSection(key: String): ConfigProvider? =
        obj[key]?.jsonObject?.let { JsonConfigProvider(it, "$path.$key".removePrefix(".")) }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getExact(key: String, clazz: Class<T>): T? =
        obj[key]?.let { json.decodeFromJsonElement(json.serializersModule.serializer(clazz), it) } as T?
}