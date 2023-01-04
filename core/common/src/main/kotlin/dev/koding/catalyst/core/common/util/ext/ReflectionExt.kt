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

import dev.koding.catalyst.core.common.util.OptionalFeature
import java.lang.invoke.MethodHandles
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Finds a given field in the class hierarchy.
 *
 * @param name The name of the field to find.
 * @return The field if found, null otherwise.
 */
fun Class<*>.findField(name: String): Field? =
    runCatching { getDeclaredField(name).also { it.isAccessible = true } }.getOrNull()
        ?: runCatching { superclass?.findField(name) }.getOrNull()

/**
 * Finds a given method in the class hierarchy.
 *
 * @param name The name of the method to find.
 * @param parameterTypes The parameter types of the method to find.
 * @return The method if found, null otherwise.
 */
fun Class<*>.findMethod(name: String, vararg parameterTypes: Class<*>): Method? =
    runCatching { getDeclaredMethod(name, *parameterTypes).also { it.isAccessible = true } }.getOrNull()
        ?: runCatching { superclass?.findMethod(name, *parameterTypes) }.getOrNull()

private val modifiersField by lazy {
    if (!OptionalFeature.FINAL_FIELD_ACCESSIBILITY.isAvailable()) return@lazy null
    val lookup = MethodHandles.privateLookupIn(Field::class.java, MethodHandles.lookup())
    lookup.findVarHandle(Field::class.java, "modifiers", Int::class.javaPrimitiveType)
}

@Suppress("unused")
var Field.final: Boolean
    get() = Modifier.isFinal(modifiers)
    set(value) {
        if (value) modifiersField?.set(this, modifiers or Modifier.FINAL)
        else modifiersField?.set(this, modifiers and Modifier.FINAL.inv())
    }

/**
 * Acts as a cache for reflection fields or methods.
 * This is useful for when you need to access a field or method multiple times.
 *
 * @author Koding
 */
object Reflector {
    private val fields = mutableMapOf<FieldMeta, Field>()
    private val methods = mutableMapOf<MethodMeta, Method>()

    data class FieldMeta(val clazz: Class<*>, val name: String)
    data class MethodMeta(val clazz: Class<*>, val name: String, val parameterTypes: List<Class<*>>)

    /**
     * Gets a field from the cache or adds it to the cache if not already present.
     *
     * @param clazz The class to get the field from.
     * @param name The name of the field.
     * @return The field.
     */
    fun getField(clazz: Class<*>, name: String): Field {
        val meta = FieldMeta(clazz, name)
        return fields.getOrPut(meta) {
            clazz.findField(name)
                ?: throw NoSuchFieldException("Field $name not found in ${clazz.name}")
        }
    }

    /**
     * Gets a method from the cache or adds it to the cache if not already present.
     *
     * @param clazz The class to get the method from.
     * @param name The name of the method.
     * @param parameterTypes The parameter types of the method.
     * @return The method.
     */
    fun getMethod(clazz: Class<*>, name: String, vararg parameterTypes: Class<*>): Method {
        val meta = MethodMeta(clazz, name, parameterTypes.toList())
        return methods.getOrPut(meta) {
            clazz.findMethod(name, *parameterTypes)
                ?: throw NoSuchMethodException("Method $name not found in ${clazz.name}")
        }
    }
}
