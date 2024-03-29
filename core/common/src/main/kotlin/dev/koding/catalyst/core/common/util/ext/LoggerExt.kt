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
package dev.koding.catalyst.core.common.util.ext

import mu.KLogger
import kotlin.system.measureTimeMillis

/**
 * Logs the time taken to execute a block of code.
 *
 * @param log Executes after the block of code has been executed and is
 *            passed the time taken to execute the block of code.
 * @param block The block of code to execute.
 */
fun KLogger.logExecutionTime(log: KLogger.(Long) -> Unit, block: () -> Unit): Unit = log(measureTimeMillis(block))
