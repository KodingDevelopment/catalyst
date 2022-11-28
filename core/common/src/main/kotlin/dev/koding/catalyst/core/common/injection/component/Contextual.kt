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

package dev.koding.catalyst.core.common.injection.component

import com.google.inject.Inject
import dev.misfitlabs.kotlinguice4.getInstance

open class Contextual : Injectable {

    @Inject
    lateinit var context: InjectionContext

    /**
     * Get an instance of the given type from the current context.
     */
    protected inline fun <reified T : Injectable> get(): T = context.plugin.injector.getInstance()

    /**
     * Get an instance of the given type from the given context.
     *
     * @param clazz the class to get an instance of
     */
    protected fun <T : Injectable> get(clazz: Class<T>): T = context.plugin.injector.getInstance(clazz)

    /**
     * Return a data file for the given name.
     *
     * @param name the name of the file
     * @return the data file
     */
    protected fun file(name: String) = lazy { context.fileManager[name] }

}