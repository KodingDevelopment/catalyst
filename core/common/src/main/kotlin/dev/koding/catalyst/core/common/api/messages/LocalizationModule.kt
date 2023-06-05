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

import dev.koding.catalyst.core.common.loader.PlatformLoader
import dev.koding.catalyst.core.common.util.ext.saveResources
import net.kyori.adventure.translation.Translator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension
import kotlin.math.round

object LocalizationModule {
    fun of(plugin: PlatformLoader) = DI.Module("Localization-${plugin::class.java.name}") {
        // Parse the messages from the lang folder
        val langFolder = plugin.dataDirectory.resolve("lang")
        plugin.saveResources("lang")

        // Create a localization instance
        val localization = Localization()

        if (langFolder.exists()) {
            // Load the message files from the lang folder
            langFolder.listDirectoryEntries("*.yml")
                .mapNotNull { it to (Translator.parseLocale(it.nameWithoutExtension) ?: return@mapNotNull null) }
                .forEach { (path, locale) -> localization.loadFromFile(locale, path.inputStream()) }

            // Sum the amount of total localizations and group them by locale
            val localizations = localization.translations.values
                .flatMap { it.formats.keys }
                .groupingBy { it }
                .eachCount()

            val total = localization.translations.size
            plugin.logger.info(
                "Loaded $total translations: ${
                    localizations.entries.joinToString {
                        "${it.key.displayName} (${round(it.value / total.toDouble() * 100.0).toInt()}%)"
                    }
                }"
            )
        }

        // Bind the message files to the DI
        bind<Localization> { singleton { localization } }
    }
}