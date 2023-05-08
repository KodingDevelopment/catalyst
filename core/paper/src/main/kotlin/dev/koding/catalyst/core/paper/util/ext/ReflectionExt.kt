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
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Resolve a field in the given class.
 *
 * @param name The name of the field to resolve.
 * @return The field if found.
 */
fun <T : Any> Class<T>.findObfuscatedField(name: String): Field? {
    // Re-obfuscate the field name if necessary
    val fieldName = SpigotObf.getClassSpigot(this.name)?.getFieldMojang(name)?.name?.spigot ?: name
    return runCatching { Reflector.getField(this, fieldName) }.getOrNull()
}

/**
 * Resolve a method in the given class.
 *
 * @param name The name of the method to resolve.
 * @param args The arguments of the method to resolve.
 * @return The method if found.
 *
 * FIXME: This method doesn't account for the arguments matching in the case of
 *        methods with the same name but different arguments. We need to find a
 *        way to resolve the correct method using the descriptor.
 */
fun <T : Any> Class<T>.findObfuscatedMethod(name: String, vararg args: Class<*>): Method? {
    val methodName = SpigotObf.getClassSpigot(this::class.java.name)
        ?.methods?.firstOrNull { it.name.mojang == name }?.name?.spigot
        ?: name

    return runCatching {
        Reflector.getMethod(
            this::class.java,
            methodName,
            *args.map { it::class.java }.toTypedArray()
        )
    }.getOrNull()
}

/**
 * Resolves a field for the given path.
 * For example, if the path is "foo", "bar", "baz", it will return the field "baz" in the class
 * after resolving "foo" and "bar".
 *
 * @param names The path of the field to resolve.
 * @return The field if found
 * @throws NoSuchFieldException If the field could not be found.
 */
@Suppress("UNCHECKED_CAST", "unused")
fun <T : Any> Any.resolveObfuscatedFieldAs(vararg names: String): T? {
    var currentObject: Any = this
    var currentField: Field?

    for (name in names) {
        currentField = currentObject::class.java.findObfuscatedField(name)
            ?: throw NoSuchFieldException("Could not find field $name in ${currentObject::class.java.name}")

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
fun <T : Any> Any.invokeObfuscatedMethod(name: String, vararg args: Any): T? {
    val method = this::class.java.findObfuscatedMethod(name, *args.map { it::class.java }.toTypedArray())
        ?: throw NoSuchMethodException("Could not find method $name in ${this::class.java.name}")
    return method.invoke(this, args) as? T
}
