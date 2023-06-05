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

import dev.koding.catalyst.core.common.util.Components
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

/**
 * A message object that can be used to parse a message with placeholders.
 * The messages are stored in a map with the locale as the key and the message as the value.
 */
class Message(private val messages: Array<String>) {

    /**
     * Pre-parsed messages to lookup when no placeholders are supplied.
     */
    private val processed = messages.map { Components.parse(it) }

    /**
     * Get a message from this message object.
     *
     * @param index The index of the message to get
     * @param placeholders The placeholders to replace in the message
     * @return The message
     */
    fun get(index: Int, vararg placeholders: TagResolver): Component {
        val message = messages.getOrNull(index) ?: return Component.empty()

        return if (placeholders.isEmpty()) Components.parse(message)
        else Components.parse(message, *placeholders)
    }

    /**
     * Get the first message from this message object.
     *
     * @param placeholders The placeholders to replace in the message
     * @return The message
     */
    fun first(vararg placeholders: TagResolver): Component = get(0, *placeholders)

    /**
     * Get all the components from this message object.
     *
     * @param placeholders The placeholders to replace in the message
     * @return The components
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun all(vararg placeholders: TagResolver): List<Component> =
        if (placeholders.isEmpty()) processed
        else messages.map { Components.parse(it, *placeholders) }

    /**
     * Send the message to an audience.
     *
     * @param audience The audience to send the message to
     * @param placeholders The placeholders to replace in the message
     */
    @Suppress("unused")
    fun send(audience: Audience, vararg placeholders: TagResolver): Unit =
        all(*placeholders).forEach { audience.sendMessage(it) }

}