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

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.pointer.Pointered
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

class Translation(val key: String) {

    /**
     * A map of all the messages for this translation.
     */
    internal val formats = ConcurrentHashMap<Locale, Message>()

    /**
     * The default message for this translation.
     */
    val default get() = get(Localization.DEFAULT_LOCALE)

    /**
     * Register a message for this translation.
     *
     * @param locale The locale of the message
     * @param message The message
     */
    fun register(locale: Locale, message: Message) {
        formats[locale] = message
    }

    /**
     * Get a message for this translation.
     *
     * @param locale The locale to translate for
     * @throws IllegalArgumentException If no message is found for the given locales
     */
    operator fun get(locale: Locale): Message =
        formats[locale]
            ?: formats[Locale(locale.language)] // try without country
            ?: formats[Localization.DEFAULT_LOCALE] // try local default locale
            ?: throw IllegalArgumentException("No message found for key $key in locale ${locale.toLanguageTag()} or default locale ${Localization.DEFAULT_LOCALE.toLanguageTag()}")

    /**
     * Get a message for this translation based on the current locale
     * of the target.
     *
     * @param target The target to get the locale from
     */
    @JvmName("getFromPointered")
    operator fun get(target: Pointered): Message =
        get(target.get(Identity.LOCALE).orElse(Localization.DEFAULT_LOCALE))

    /**
     * Send a message to the audience based on their locales.
     *
     * @param audience The audience to send the message to
     */
    @Suppress("unused")
    fun send(audience: Audience, vararg placeholders: TagResolver): Unit =
        audience.forEachAudience { get(it).send(it, *placeholders) }

}