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
package dev.koding.catalyst.core.common.api.config.adapter

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import dev.koding.catalyst.core.common.api.config.ConfigAdapter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import java.io.InputStream
import java.io.OutputStream

/**
 * A config adapter that uses Kaml
 */
@Suppress("unused")
object KamlAdapter : ConfigAdapter {

    private val yaml = Yaml(configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property))

    override val extension: String = "yml"

    override fun <T : Any> encode(io: OutputStream, data: T, clazz: Class<out T>) =
        yaml.encodeToStream(serializer(clazz), data, io)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> decode(io: InputStream, clazz: Class<T>): T =
        yaml.decodeFromStream(serializer(clazz) as KSerializer<T>, io)
}