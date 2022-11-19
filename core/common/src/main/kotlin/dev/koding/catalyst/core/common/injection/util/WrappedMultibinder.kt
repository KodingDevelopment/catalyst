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

package dev.koding.catalyst.core.common.injection.util

import com.google.inject.Binder
import com.google.inject.TypeLiteral
import com.google.inject.multibindings.Multibinder
import kotlin.reflect.KClass

/**
 * Wraps a [Multibinder] with a given type literal, using DSL-style methods. Provides shortcuts to
 * bind [KClass]es, [Class]es, and objects.
 *
 * @author Koding
 */
class WrappedMultibinder<T : Any>(binder: Binder, type: TypeLiteral<T>) {

    companion object {
        inline fun <reified T : Any> of(binder: Binder) =
            WrappedMultibinder(binder, object : TypeLiteral<T>() {})
    }

    private val binder = Multibinder.newSetBinder(binder, type)

    /**
     * Binds a set of objects to the binder.
     *
     * @param bindings The objects to bind.
     */
    fun bind(vararg bindings: T) = bindings.forEach { binder.addBinding().toInstance(it) }

    /**
     * Binds a list of classes to the binder.
     *
     * @param bindings The classes to bind.
     */
    fun bind(vararg bindings: Class<out T>) = bindings.forEach { binder.addBinding().to(it) }

    /**
     * Binds a list of classes to the binder.
     *
     * @param bindings The classes to bind.
     */
    fun bind(vararg bindings: KClass<out T>) = bindings.forEach { binder.addBinding().to(it.java) }

    /**
     * Binds a list of type literals to the binder.
     *
     * @param bindings The classes to bind.
     */
    fun bind(vararg bindings: TypeLiteral<out T>) = bindings.forEach { binder.addBinding().to(it) }
}
