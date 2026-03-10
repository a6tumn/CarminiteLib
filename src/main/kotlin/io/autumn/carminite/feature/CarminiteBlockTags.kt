@file:Suppress("unused")

package io.autumn.carminite.feature

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object CarminiteBlockTags {
    val ROOT_TRACE_SKIP: TagKey<Block> = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("carminite", "root_trace_skip"))
    val WORLDGEN_REPLACEABLES: TagKey<Block> = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("carminite", "worldgen_replaceables"))
}