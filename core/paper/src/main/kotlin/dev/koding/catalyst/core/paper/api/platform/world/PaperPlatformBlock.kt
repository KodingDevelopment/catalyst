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
package dev.koding.catalyst.core.paper.api.platform.world

import dev.koding.catalyst.core.common.api.platform.world.BlockMetadata
import dev.koding.catalyst.core.common.api.platform.world.BlockPos
import dev.koding.catalyst.core.common.api.platform.world.PlatformBlock
import dev.koding.catalyst.core.common.api.platform.world.PlatformChunk
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData

class PaperPlatformBlock(override val chunk: PlatformChunk, val ref: Block) : PlatformBlock {
    override val type: BlockMetadata get() = ref.type.wrap()
    override val position: BlockPos by lazy { BlockPos(chunk.world, ref.x, ref.y, ref.z) }
}

/**
 * Wrap a Block into a PlatformBlock.
 *
 * @param chunk The chunk this block is in.
 */
fun Block.wrap(chunk: PlatformChunk): PaperPlatformBlock = PaperPlatformBlock(chunk, this)

fun PlatformBlock.unwrap(): Block = (this as PaperPlatformBlock).ref

/**
 * Wrap the Bukkit Material into a BlockMetadata.
 *
 * TODO: Rewrite this to be more efficient.
 */
fun Material.wrap(): BlockMetadata = BlockMetadata(
    key,

    // We parse the string here to avoid reflection, however we should do this later
    createBlockData().getAsString(false)
        .split("[", "]")
        .getOrNull(1)
        ?.split(",")
        ?.map { it.split("=") }
        ?.associate { it[0] to it[1] } ?: emptyMap()
)

/**
 * Unwrap the BlockMetadata into Bukkit BlockData.
 */
fun BlockMetadata.unwrap(): BlockData =
    Bukkit.createBlockData("${type.asString()}[${properties.entries.joinToString(",") { "${it.key}=${it.value}" }}]")
