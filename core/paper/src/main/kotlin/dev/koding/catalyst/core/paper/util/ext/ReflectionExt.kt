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
package dev.koding.catalyst.core.paper.util.ext

import dev.koding.catalyst.core.common.util.ext.Reflector
import dev.koding.catalyst.core.paper.util.SpigotObf
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Resolves a field for the given path.
 * For example, if the path is "foo", "bar", "baz", it will return the field "baz" in the class
 * after resolving "foo" and "bar".
 *
 * @param names The path of the field to resolve.
 * @return The field if found
 * @throws NoSuchFieldException If the field could not be found.
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> Any.resolveReflectionFieldsAs(vararg names: String): T? {
    var currentObject: Any = this
    var currentField: Field?

    for (name in names) {
        // Re-obfuscate the field name if necessary
        val fieldName = SpigotObf.getClassSpigot(currentObject::class.java.name)
            ?.getFieldMojang(name)?.name?.spigot
            ?: name

        currentField = Reflector.getField(currentObject::class.java, fieldName)

        // Check if static
        currentObject = if (currentField.modifiers and Modifier.STATIC != 0) currentField.get(null)
        else currentField.get(currentObject)
    }

    return currentObject as? T
}

/**
 * Invokes a method using reflection.
 *
 * @param name The name of the method to invoke.
 * @param args The arguments of the method to invoke.
 * @return The result of the method invocation.
 */
@Suppress("UNCHECKED_CAST", "unused")
fun <T : Any> Any.invokeReflectionMethodAs(name: String, vararg args: Any): T? {
    val methodName = SpigotObf.getClassSpigot(this::class.java.name)
        ?.methods?.firstOrNull { it.name.mojang == name }?.name?.spigot
        ?: name

    val method = Reflector.getMethod(this::class.java, methodName)
    return method.invoke(this, args) as? T
}
