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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

object DefaultSchedulers : Schedulers {
    override val sync: Scheduler = DefaultSyncScheduler
    override val async: Scheduler = DefaultAsyncScheduler
}

object DefaultSyncScheduler : Scheduler {
    override fun schedule(delay: Long?, interval: Long?, task: () -> Unit): Task {
        // Create a timer
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                task()
            }
        }

        // Schedule the task
        when {
            delay != null && interval != null -> timer.scheduleAtFixedRate(timerTask, delay, interval)
            delay != null -> timer.schedule(timerTask, delay)
            else -> timer.schedule(timerTask, 0)
        }

        // Return a task that can be cancelled
        return object : Task {
            override fun cancel() {
                timerTask.cancel()
                timer.cancel()
            }
        }
    }
}

object DefaultAsyncScheduler : Scheduler {

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun schedule(delay: Long?, interval: Long?, task: () -> Unit): Task {
        // Create a task using coroutines
        val job = scope.launch {
            // Delay if necessary
            if (delay != null) delay(delay)

            if (interval != null) {
                while (true) {
                    task()
                    delay(interval)
                }
            } else {
                task()
            }
        }

        // Return a task that can be cancelled
        return object : Task {
            override fun cancel() {
                job.cancel()
            }
        }
    }
}
