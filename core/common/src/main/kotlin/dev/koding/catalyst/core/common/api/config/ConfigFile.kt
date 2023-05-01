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

import dev.koding.catalyst.core.common.loader.PlatformLoader
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

/**
 * Represents a config file
 *
 * @param adapter The adapter to use for encoding and decoding the config file
 */
open class ConfigFile<T : Any>(
    private val file: Path,
    private val adapter: ConfigAdapter,
    data: T
) {

    /**
     * The data of the config file
     */
    var data: T = data
        private set

    /**
     * Load the config file from the given [file]
     */
    fun load() {
        // Create the file if it doesn't exist
        if (!file.exists()) save()

        // Load the config
        this.data = adapter.decode(file.inputStream(), data::class.java)
    }

    /**
     * Save the config file to the given [file]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun save() {
        // Create the file if it doesn't exist
        if (!file.exists()) {
            file.parent.createDirectories()
            file.createFile()
        }

        // Save the config
        adapter.encode(file.outputStream(), data, data::class.java)
    }

}

/**
 * Get a config file for the given platform loader
 *
 * @param name The name of the config file
 * @param data The data of the config file (used for default values)
 * @param adapter The adapter to use for encoding and decoding the config file
 */
fun <T : Any> PlatformLoader.config(name: String, data: T, adapter: ConfigAdapter): ConfigFile<T> =
    ConfigFile(dataDirectory.resolve("$name.${adapter.extension}"), adapter, data).also { it.load() }

/**
 * Lazily get a config file for the given DIAware.
 *
 * @param name The name of the config file
 * @param data The data of the config file (used for default values)
 * @param adapter The adapter to use for encoding and decoding the config file
 */
@Suppress("unused")
fun <T : Any> DIAware.config(name: String, data: T, adapter: ConfigAdapter): Lazy<ConfigFile<T>> {
    val loader by instance<PlatformLoader>()
    return lazy { loader.config(name, data, adapter) }
}