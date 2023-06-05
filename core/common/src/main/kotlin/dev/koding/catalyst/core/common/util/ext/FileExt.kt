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
@file:OptIn(ExperimentalPathApi::class)

package dev.koding.catalyst.core.common.util.ext

import dev.koding.catalyst.core.common.loader.PlatformLoader
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.pathString

/**
 * Save resources from the plugin JAR to the data folder.
 *
 * @param resources The resources to save
 */
fun PlatformLoader.saveResources(vararg resources: String) {
    // Load the JAR as a file system
    val jarPath = File(javaClass.protectionDomain.codeSource.location.toURI()).toPath()
    FileSystems.newFileSystem(jarPath).use { fs ->
        resources.forEach { res ->
            // Check if the file exists
            val resource = fs.getPath(res)
            val hostFile = dataDirectory.resolve(resource.pathString)
            if (!resource.exists()) return@forEach

            // Create the parent dirs
            hostFile.parent.createDirectories()

            // If the resource is a directory, copy it recursively
            if (resource.isDirectory()) {
                resource.copyToRecursivelySafe(hostFile)
                return@forEach
            }

            // Otherwise, copy the file
            resource.copyTo(hostFile)
        }
    }
}

/**
 * Copy a directory recursively.
 *
 * We need to re-implement the copyToRecursively function since we
 * are copying between two different file systems, which will throw
 * an exception if we try to use the built-in function.
 *
 * We resolve this by calling the resolve function with the relative
 * file path as a string instead of a Path object.
 *
 * @param dest The destination directory
 */
internal fun Path.copyToRecursivelySafe(dest: Path) {
    listDirectoryEntries().forEach { file ->
        // Get the relative path of the file
        val relativePath = relativize(file).toString()
        val destFile = dest.resolve(relativePath)

        // Create the directories
        destFile.parent.createDirectories()

        if (file.isDirectory()) {
            file.copyToRecursivelySafe(destFile)
        } else {
            if (destFile.exists()) return@forEach
            file.copyTo(destFile)
        }
    }
}