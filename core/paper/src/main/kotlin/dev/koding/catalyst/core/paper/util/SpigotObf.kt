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
package dev.koding.catalyst.core.paper.util

import io.papermc.paper.util.ObfHelper
import net.fabricmc.mappingio.MappingReader
import net.fabricmc.mappingio.format.MappingFormat
import net.fabricmc.mappingio.tree.MemoryMappingTree

/**
 * Allows for obfuscation and deobfuscation of mappings between
 * Mojang and Spigot mappings.
 *
 * @author Koding
 */
object SpigotObf {

    /**
     * Constants.
     */
    private const val SPIGOT_NAMESPACE = "spigot"

    private var mappings: List<ClassMapping>? = null

    /**
     * Initializes the obfuscation mappings from the jar if they
     * have not been initialized already.
     */
    fun load() {
        if (mappings != null) {
            throw IllegalStateException("Already initialized")
        }

        // Read mappings
        val tree = MemoryMappingTree()
        ObfHelper::class.java.classLoader.getResourceAsStream("META-INF/mappings/reobf.tiny")
            ?.use { MappingReader.read(it.reader(), MappingFormat.TINY_2, tree) }
            ?: return

        // Load mappings
        mappings = tree.classes.map {
            // Read class name
            val name = Mapping(
                it.srcName,
                it.getName(SPIGOT_NAMESPACE)
            )

            // Read class fields
            val fields = it.fields.map { field ->
                val fieldName = Mapping(
                    field.srcName,
                    field.getName(SPIGOT_NAMESPACE)
                )

                val fieldDesc = Mapping(
                    field.srcDesc,
                    field.getDesc(SPIGOT_NAMESPACE)
                )

                DescMapping(fieldName, fieldDesc)
            }

            // Read class methods
            val methods = it.methods.map { method ->
                val methodName = Mapping(
                    method.srcName,
                    method.getName(SPIGOT_NAMESPACE)
                )

                val methodDesc = Mapping(
                    method.srcDesc,
                    method.getDesc(SPIGOT_NAMESPACE)
                )

                DescMapping(methodName, methodDesc)
            }

            ClassMapping(name, fields, methods)
        }
    }

    /**
     * Returns the [ClassMapping] field for the given class
     * name in Mojang mappings.
     *
     * @param name The class name in Mojang mappings.
     * @return The [ClassMapping] field for the given class name.
     */
    @Suppress("unused")
    fun getClassMojang(name: String): ClassMapping? {
        val formatted = name.classToDesc()
        return mappings?.firstOrNull { it.name.mojang == formatted }
    }

    /**
     * Returns the [ClassMapping] field for the given class
     * name in Spigot mappings.
     *
     * @param name The class name in Spigot mappings.
     * @return The [ClassMapping] field for the given class name.
     */
    fun getClassSpigot(name: String): ClassMapping? {
        val formatted = name.classToDesc()
        return mappings?.firstOrNull { it.name.spigot == formatted }
    }

    private fun String.classToDesc(): String = replace(".", "/")
}

/**
 * A named pair of mojang and spigot mappings.
 */
data class Mapping(val mojang: String, val spigot: String)

@Suppress("unused")
data class ClassMapping(
    val name: Mapping,
    val fields: List<DescMapping>,
    val methods: List<DescMapping>
) {
    /**
     * Returns a field mapping for the given name in Mojang mappings.
     *
     * @param name The field name in Mojang mappings.
     * @return The field mapping for the given name, or null if not found.
     */
    fun getFieldMojang(name: String): DescMapping? = fields.firstOrNull { it.name.mojang == name }

    /**
     * Returns a field mapping for the given name in Spigot mappings.
     *
     * @param name The field name in Spigot mappings.
     * @return The field mapping for the given name, or null if not found.
     */
    fun getFieldSpigot(name: String): DescMapping? = fields.firstOrNull { it.name.spigot == name }

    /**
     * Returns a method mapping for the given name and description in Mojang mappings.
     *
     * @param name The method name in Mojang mappings.
     * @param desc The method description in Mojang mappings.
     * @return The method mapping for the given name and description, or null if not found.
     */
    fun getMethodMojang(name: String, desc: String): DescMapping? =
        methods.firstOrNull { it.name.mojang == name && it.desc.mojang == desc }

    /**
     * Returns a method mapping for the given name and description in Spigot mappings.
     *
     * @param name The method name in Spigot mappings.
     * @param desc The method description in Spigot mappings.
     * @return The method mapping for the given name and description, or null if not found.
     */
    fun getMethodSpigot(name: String, desc: String): DescMapping? =
        methods.firstOrNull { it.name.spigot == name && it.desc.spigot == desc }
}

/**
 * Used for mapping fields and methods.
 */
data class DescMapping(
    val name: Mapping,
    val desc: Mapping
)
