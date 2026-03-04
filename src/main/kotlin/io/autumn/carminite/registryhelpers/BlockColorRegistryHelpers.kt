@file:Suppress("unused")

package io.autumn.carminite.registryhelpers

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.world.level.block.Block

/**
 * Collection of helper methods for streamlining block color registration while
 * using and copying some logic from vanilla systems.
 *
 * Defaults parameter values are not provided in most cases as the arguments
 * will need meaningful values for production code.
 **/

/**
 * Registers a standard stair block(not the deprecated legacy stair).
 *
 * Intended for generic stair blocks.
 *
 * @param layers A list of BlockTintSource(s) that will be applied to the registered block(s) (no default set).
 * @param blocks The block(s) which the tint sources will be applied to (no default set).
 **/
fun registerGenericBlockColor(layers: List<BlockTintSource>, vararg blocks: Block) {
    BlockColorRegistry.register(layers, *blocks)
}