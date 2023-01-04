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

    fun hasArgument(argument: String): Boolean = arguments.contains(argument)
}

enum class OptionalFeature(private val arguments: Array<String>) {
    FINAL_FIELD_ACCESSIBILITY(arrayOf("--add-opens=java.base/java.lang.reflect=ALL-UNNAMED"));

    private val logger = KotlinLogging.logger { }
    private var warned = false

    fun isAvailable(): Boolean = arguments.all { JVM.hasArgument(it) }.also { if (!it) printWarning() }

    private fun printWarning() {
        if (warned) return
        warned = true

        logger.warn {
            "Optional feature $this is not available. Please add the following arguments to your JVM: ${
            arguments.joinToString(
                " "
            )
            }"
        }
    }
}
