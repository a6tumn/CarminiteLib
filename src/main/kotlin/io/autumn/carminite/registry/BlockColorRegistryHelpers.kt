@file:Suppress("unused")

package io.autumn.carminite.registry

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.world.level.block.Block

fun registerGenericBlockColor(layers: List<BlockTintSource>, vararg blocks: Block) {
    BlockColorRegistry.register(layers, *blocks)
}