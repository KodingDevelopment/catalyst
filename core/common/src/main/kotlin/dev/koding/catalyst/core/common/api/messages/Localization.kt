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
package dev.koding.catalyst.core.common.api.messages

import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

/**
 * Localization provides a way to store translations for a plugin.
 * It is a collection of [Translation] objects, which are collections of [Message] objects.
 *
 * It is recommended to use the [loadFromFile] method to load translations from a YAML file,
 * but it is also possible to manually register translations. (Though we do not recommend this)
 *
 * @see Translation
 */
class Localization {

    companion object {
        /**
         * The default locale to use when no other locale is specified.
         */
        val DEFAULT_LOCALE: Locale = Locale.US
    }

    /**
     * The translations for this instance.
     */
    internal val translations = ConcurrentHashMap<String, Translation>()

    /**
     * Load the translations from a YAML file.
     *
     * @param locale The locale of the file
     * @param stream The input stream to read from
     */
    fun loadFromFile(locale: Locale, stream: InputStream) {
        val data = Yaml().load<Map<String, Any>>(stream)
        load(locale, data)
    }

    /**
     * Load the translations from a map.
     *
     * @param data The data to parse
     * @param path The path of the data, used for building message keys separated by a "."
     */
    @Suppress("UNCHECKED_CAST")
    private fun load(locale: Locale, data: Map<String, Any>, path: String = "") {
        data.forEach { (key, value) ->
            // If the value is a map, parse it recursively
            if (value is Map<*, *>) {
                load(locale, value as Map<String, Any>, "$path.$key")
            } else {
                // Build the message key
                val msgKey = "$path.$key".removePrefix(".")

                // Get the translation
                val translation = translations.computeIfAbsent(msgKey) { Translation(msgKey) }

                // Determine the message type
                val message = when (value) {
                    is String -> Message(arrayOf(value))
                    is Array<*> -> Message(value as Array<String>)
                    is List<*> -> Message((value as List<String>).toTypedArray())
                    else -> throw IllegalArgumentException("Invalid message type for key $msgKey")
                }
                translation.register(locale, message)
            }
        }
    }

    /**
     * Get a translation.
     *
     * @param key The key of the message
     * @return The message
     * @throws IllegalArgumentException If the message does not exist
     */
    operator fun get(key: String): Translation =
        translations[key] ?: throw IllegalArgumentException("No translation for key $key")

}