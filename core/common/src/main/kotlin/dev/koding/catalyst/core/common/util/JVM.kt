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

import mu.KotlinLogging
import java.lang.management.ManagementFactory

object JVM {
    private val arguments = ManagementFactory.getRuntimeMXBean().inputArguments

    /**
     * Checks if the JVM has a given [argument], excluding those given to the main method.
     */
    fun hasArgument(argument: String): Boolean = arguments.contains(argument)
}

/**
 * Check if the JVM has [arguments] for specific features (such as the [FINAL_FIELD_ACCESSIBILITY] flag) as they may be
 * required for the program to work correctly.
 */
enum class OptionalFeature(private val arguments: Array<String>) {

    /**
     * Allows changing the value of final fields at runtime.
     */
    FINAL_FIELD_ACCESSIBILITY(arrayOf("--add-opens=java.base/java.lang.reflect=ALL-UNNAMED"));

    private val logger = KotlinLogging.logger { }
    private var warned = false

    /**
     * Checks if the feature is available.
     *
     * @return True if the feature is available, false otherwise.
     */
    fun isAvailable(): Boolean = arguments.all { JVM.hasArgument(it) }.also { if (!it) printWarning() }

    /**
     * Prints a warning if the feature is not available.
     */
    private fun printWarning() {
        if (warned) return
        warned = true

        val suggestedArguments = arguments.joinToString(" ")
        logger.warn {
            "Optional feature $this is not available. Please add the following arguments to your JVM: $suggestedArguments"
        }
    }
}
