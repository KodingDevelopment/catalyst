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
package dev.koding.catalyst.core.common.api.scheduler

interface Schedulers {
    /**
     * The scheduler to use for synchronous operations.
     */
    val sync: Scheduler

    /**
     * The scheduler to use for asynchronous operations.
     */
    val async: Scheduler
}

/**
 * A scheduler that can be used to schedule tasks, with an optional
 * delay or interval.
 */
@Suppress("unused")
interface Scheduler {
    /**
     * Schedules a task to be executed, with an optional delay or interval.
     *
     * @param delay The delay in milliseconds.
     * @param interval The interval in milliseconds.
     * @param task The task to execute.
     */
    fun schedule(delay: Long? = null, interval: Long? = null, task: () -> Unit): Task

    /*
     * Java-friendly version of the above.
     * (@JvmOverloads doesn't work here because it's an interface)
     */
    fun schedule(delay: Long, interval: Long, task: Runnable): Task = schedule(delay, interval) { task.run() }
    fun schedule(delay: Long, task: Runnable): Task = schedule(delay) { task.run() }
    fun schedule(task: Runnable): Task = schedule { task.run() }
}

/**
 * A task that can be cancelled.
 */
interface Task {
    fun cancel()
}
