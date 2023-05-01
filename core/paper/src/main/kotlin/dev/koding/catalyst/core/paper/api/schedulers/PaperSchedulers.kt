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
package dev.koding.catalyst.core.paper.api.schedulers

import dev.koding.catalyst.core.common.api.scheduler.Scheduler
import dev.koding.catalyst.core.common.api.scheduler.Schedulers
import dev.koding.catalyst.core.common.api.scheduler.Task
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

/**
 * Implements [Schedulers] for Paper.
 */
class PaperSchedulers(override val di: DI) : Schedulers, DIAware {

    private val plugin by instance<JavaPlugin>()

    override val sync: Scheduler = PaperSyncScheduler(plugin)
    override val async: Scheduler = PaperAsyncScheduler(plugin)
}

class PaperSyncScheduler(private val plugin: JavaPlugin) : Scheduler {
    override fun schedule(delay: Long?, interval: Long?, task: () -> Unit): Task = when {
        delay != null && interval != null -> plugin.server.scheduler.runTaskTimer(plugin, task, delay / 50, interval / 50).wrap()
        delay != null -> plugin.server.scheduler.runTaskLater(plugin, task, delay / 50).wrap()
        else -> plugin.server.scheduler.runTask(plugin, task).wrap()
    }
}

class PaperAsyncScheduler(private val plugin: JavaPlugin) : Scheduler {
    override fun schedule(delay: Long?, interval: Long?, task: () -> Unit): Task = when {
        delay != null && interval != null -> plugin.server.scheduler.runTaskTimerAsynchronously(
            plugin,
            task,
            delay / 50,
            interval / 50
        ).wrap()

        delay != null -> plugin.server.scheduler.runTaskLaterAsynchronously(plugin, task, delay / 50).wrap()
        else -> plugin.server.scheduler.runTaskAsynchronously(plugin, task).wrap()
    }
}

/**
 * Wrapper around a Bukkit task.
 */
class PaperTask(private val parent: BukkitTask) : Task {
    override fun cancel(): Unit = parent.cancel()
}

private fun BukkitTask.wrap(): Task = PaperTask(this)
