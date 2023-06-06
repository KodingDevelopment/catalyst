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
package dev.koding.catalyst.core.common.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.regex.Pattern

/**
 * Utility for creating / manipulating Adventure components.
 */
object Components {

    /**
     * The pattern used to match URLs.
     */
    private val URL_PATTERN =
        Pattern.compile("(http(s)?://)?(www\\.)?[a-zA-Z0-9]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)*(/[a-zA-Z0-9]+)*")

    /**
     * The pattern used to match legacy hex color codes.
     */
    private val LEGACY_HEX_PATTERN = Regex("&#([\\da-fA-F]{6})|&#([\\da-fA-F]{3})")

    /**
     * Parses a string to an Adventure component.
     *
     * @param text The text to parse.
     * @param resolvers The placeholders to use.
     *
     * @param prepare Whether to prepare the text for parsing.
     * @param replaceUrls Whether to replace URLs with clickable links.
     */
    fun parse(
        text: String,
        vararg resolvers: TagResolver,
        prepare: Boolean = true,
        replaceUrls: Boolean = true
    ): Component {
        val target = if (prepare) prepareMiniMessageString(text) else text
        val component = MiniMessage.miniMessage().deserialize(target, *resolvers)

        // Replace URLs
        return if (replaceUrls) component.replaceText {
            it.match(URL_PATTERN)
            it.replacement { match, tag ->
                @Suppress("HttpUrlsUsage")
                val hasProtocol = match.group().startsWith("http://") || match.group().startsWith("https://")
                val url = if (hasProtocol) match.group() else "https://${match.group()}"

                tag
                    .decoration(TextDecoration.UNDERLINED, true)
                    .hoverEvent(Component.text("Click to open link").color(NamedTextColor.GRAY))
                    .clickEvent(ClickEvent.openUrl(url))
            }
        } else component
    }

    /**
     * Prepares a component string for parsing to MiniMessage, supporting
     * some legacy-formatting codes.
     *
     * @param text The text to prepare.
     * @return The prepared text.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun prepareMiniMessageString(text: String): String {
        // Replace legacy hex color codes
        var result = LEGACY_HEX_PATTERN.replace(text) { "<${it.value.substring(1)}>" }

        // Replace legacy color codes
        LegacyColors.LEGACY_COLORS.forEach { (key, value) ->
            result = result.replace("&$key", "<$value>")
        }

        // Replace legacy formatting codes
        LegacyColors.LEGACY_FORMATTING.forEach { (key, value) ->
            result = result.replace("&$key", "<$value>")
        }

        // We ignore the reset code
        return result
    }

}

object LegacyColors {
    /**
     * The legacy color codes.
     */
    val LEGACY_COLORS = mapOf(
        '0' to NamedTextColor.BLACK,
        '1' to NamedTextColor.DARK_BLUE,
        '2' to NamedTextColor.DARK_GREEN,
        '3' to NamedTextColor.DARK_AQUA,
        '4' to NamedTextColor.DARK_RED,
        '5' to NamedTextColor.DARK_PURPLE,
        '6' to NamedTextColor.GOLD,
        '7' to NamedTextColor.GRAY,
        '8' to NamedTextColor.DARK_GRAY,
        '9' to NamedTextColor.BLUE,
        'a' to NamedTextColor.GREEN,
        'b' to NamedTextColor.AQUA,
        'c' to NamedTextColor.RED,
        'd' to NamedTextColor.LIGHT_PURPLE,
        'e' to NamedTextColor.YELLOW,
        'f' to NamedTextColor.WHITE
    )

    /**
     * The legacy-formatting codes.
     */
    val LEGACY_FORMATTING = mapOf(
        'k' to TextDecoration.OBFUSCATED,
        'l' to TextDecoration.BOLD,
        'm' to TextDecoration.STRIKETHROUGH,
        'n' to TextDecoration.UNDERLINED,
        'o' to TextDecoration.ITALIC,
    )
}