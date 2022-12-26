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
@file:Suppress("unused")

package dev.koding.catalyst.core.common.api.config

import dev.koding.catalyst.core.common.util.splitCamelCase
import kotlin.reflect.KProperty

/**
 * Abstraction for a config, which can be used to read data. This is abstracted
 * to enable config to be loaded from different sources, such as a file or
 * environment variables.
 *
 * @author Koding
 */
@Suppress("unused")
interface ConfigProvider {

    /**
     * The path to the config, if applicable. May be blank for
     * root sections.
     */
    val path: String

    /**
     * A list of subsections in this config.
     */
    val sections: List<ConfigProvider>
        get() = getKeys(false)
            .filter { isSection(it) }
            .map { getSection(it) ?: error("Section $it does not exist. This SHOULD NOT happen.") }

    /**
     * A list of keys in this config section. Not recursive.
     */
    val keys: Set<String>

    /**
     * Whether this section contains the given key.
     *
     * @param key The key to check for
     * @return Whether the key exists
     */
    operator fun contains(key: String): Boolean

    /**
     * Fetches an object from the config, or returns the default value if
     * the key does not exist.
     *
     * @param key The key to fetch
     * @param clazz The class of the object to fetch
     * @param default The default value to return if the key does not exist
     * @return The value, or the default value if the key does not exist
     */
    operator fun <T> get(key: String, default: T, clazz: Class<T>): T = get(key, clazz) ?: default

    /**
     * Fetches an object from the config, or null if the key does not exist.
     * Supports recursive fetching, e.g. `get("foo.bar")` will fetch the
     * value of `bar` in the section `foo`.
     *
     * @param key The key to fetch
     * @param clazz The class of the object to fetch
     *
     * @return The value, or null if the key does not exist
     */
    operator fun <T> get(key: String, clazz: Class<T>): T? {
        // Recurse sections split by '.'
        val parts = key.split('.')
        var current = this

        // Update the current section for each part
        for (part in parts.dropLast(1)) current = getSection(part) ?: return null

        // Return the exact value for the last part
        return current.getExact(parts.last(), clazz)
    }

    /**
     * Fetches a list from the config.
     *
     * @param key The key to fetch
     * @return The list
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getList(key: String): List<T> = get(key, List::class.java) as List<T>

    /**
     * Fetches an object from the config, or null if the key does not exist.
     * Does NOT support recursive fetching, e.g. `get("foo.bar")` will fetch
     * the value of `foo.bar` in the root section.
     *
     * @param key The key to fetch
     * @param clazz The class of the object to fetch
     */
    fun <T> getExact(key: String, clazz: Class<T>): T?

    /**
     * Sets a value in the config.
     *
     * @param key The key to set
     * @param value The value to set
     */
    operator fun set(key: String, value: Any?)

    /**
     * Fetches a section from the config.
     *
     * @param key The key to fetch
     * @return The section, or null if the key does not exist
     */
    fun getSection(key: String): ConfigProvider?

    /**
     * Checks whether the given key is a section.
     *
     * @param key The key to check
     * @return Whether the key is a section
     */
    fun isSection(key: String) = getSection(key) != null

    /**
     * Gets a set of all keys in this section, optionally recursively.
     *
     * @param recursive Whether to recursively fetch keys
     * @return The set of keys
     */
    fun getKeys(recursive: Boolean = false): Set<String> {
        if (!recursive) return keys
        return keys + sections.flatMap { it.getKeys(true) }
    }

    /**
     * Creates a property delegate to read a configuration value, optionally
     * processing it, which can be nullable.
     *
     * @param clazz The class of the value
     * @param name The name of the property, or null to use the property name
     * @param process The process to apply to the value, or null to not process
     * @return The property delegate
     */
    fun <T : Any> read(clazz: Class<T>, name: String? = null, process: (T?) -> T? = { it }) =
        ConfigurationPropertyDelegate(this, clazz, name, process)

    /**
     * Creates a property delegate to read a configuration value, optionally
     * processing it, and returning a default value if the value is null after
     * processing.
     *
     * @param clazz The class of the value
     * @param default The default value to return if the value is null after processing
     * @param name The name of the property, or null to use the property name
     * @param process The process to apply to the value, or null to not process
     * @return The property delegate
     */
    fun <T : Any> readOr(clazz: Class<T>, default: T, name: String? = null, process: (T) -> T = { it }) =
        ConfigurationFallbackPropertyDelegate(this, clazz, default, name, process)
}

/**
 * Inline extension to get a config value, or return the default value if the
 * key does not exist.
 *
 * @param key The key to fetch
 * @param default The default value to return if the key does not exist
 */
inline fun <reified T : Any> ConfigProvider.get(key: String, default: T) = get(key, default, T::class.java)

/**
 * Inline extension to get a config value, or return null if the key does not
 * exist.
 *
 * @param key The key to fetch
 */
inline fun <reified T : Any> ConfigProvider.get(key: String) = get(key, T::class.java)

/**
 * Inline extension to create a property delegate to read a configuration value,
 * optionally processing it, which can be nullable.
 *
 * @param name The name of the property, or null to use the property name
 * @param process The process to apply to the value, or null to not process
 */
inline fun <reified T : Any> ConfigProvider.read(name: String? = null, noinline process: (T?) -> T? = { it }) =
    read(T::class.java, name, process)

/**
 * Inline extension to create a property delegate to read a configuration value,
 * optionally processing it, and returning a default value if the value is null
 * after processing.
 *
 * @param default The default value to return if the value is null after processing
 * @param name The name of the property, or null to use the property name
 * @param process The process to apply to the value, or null to not process
 */
inline fun <reified T : Any> ConfigProvider.readOr(default: T, name: String? = null, noinline process: (T) -> T = { it }) =
    readOr(T::class.java, default, name, process)

/**
 * A property delegate to read a configuration value, optionally processing it, which can be
 * nullable.
 *
 * @author Koding
 */
class ConfigurationPropertyDelegate<T : Any>(
    private val config: ConfigProvider,
    private val clazz: Class<T>,
    private val name: String?,
    private val process: (T?) -> T? = { it }
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        process(config[name ?: property.configName, clazz])
}

/**
 * A property delegate to read a configuration value, optionally processing it, which is not
 * nullable, and falls back to a default value if it is not found.
 *
 * @author Koding
 */
class ConfigurationFallbackPropertyDelegate<T : Any>(
    private val config: ConfigProvider,
    private val clazz: Class<T>,
    private val default: T,
    private val name: String?,
    private val process: (T) -> T = { it }
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        process(config[name ?: property.configName, default, clazz])
}

private val KProperty<*>.configName
    get() = name.splitCamelCase().joinToString("-").lowercase()
