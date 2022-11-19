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

package dev.koding.catalyst.core.common.injection.module

import com.google.inject.TypeLiteral
import dev.koding.catalyst.core.common.injection.util.WrappedMultibinder
import dev.misfitlabs.kotlinguice4.KotlinModule
import kotlin.reflect.KClass

/**
 * A base module for all modules to extend. Provides a few utilities for
 * binding and multibinding.
 */
@Suppress("unused")
open class Module : KotlinModule() {
    /**
     * Creates a new multibinder for the given type.
     */
    inline fun <reified T : Any> multibinder() = WrappedMultibinder.of<T>(`access$binder`())

    /**
     * Binds the given classes to the multibinder for the given type.
     */
    inline fun <reified T : Any> multibind(vararg classes: Class<out T>) =
        multibinder<T>().bind(*classes)

    /**
     * Binds the given classes to the multibinder for the given type.
     */
    inline fun <reified T : Any> multibind(vararg classes: KClass<out T>) =
        multibinder<T>().bind(*classes)

    /**
     * Binds the given instances to the multibinder for the given type.
     */
    inline fun <reified T : Any> multibind(vararg instances: T) = multibinder<T>().bind(*instances)

    /**
     * Binds the given types to the multibinder for the given type.
     */
    inline fun <reified T : Any> multibind(vararg types: TypeLiteral<out T>) =
        multibinder<T>().bind(*types)

    /**
     * Accesses the binder for this module.
     */
    @Suppress("FunctionName")
    @PublishedApi
    internal fun `access$binder`() = binder()
}
