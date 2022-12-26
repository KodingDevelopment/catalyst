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

package dev.koding.catalyst.core.common.util

private val camelCaseRegex = "(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])".toRegex()

/**
 * Splits a string from camelCase to hyphenated lowercase. Used to get a config name for a property
 * so that it can be inferred from the property name.
 *
 * @returns A list of strings.
 */
fun String.splitCamelCase(): List<String> = camelCaseRegex.split(this).map { it.lowercase() }