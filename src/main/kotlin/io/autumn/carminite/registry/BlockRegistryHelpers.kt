@file:Suppress("unused")

package io.autumn.carminite.registry

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockBehaviour

fun registerGenericBlock(
    namespaceAndPath: Identifier,
    blockFactory: (BlockBehaviour.Properties) -> Block,
    settings: BlockBehaviour.Properties,
    shouldRegisterItem: Boolean = true
) : Block {
    val blockKey = keyOfBlock(namespaceAndPath)
    val block = blockFactory(settings.setId(blockKey)).also {
        if (shouldRegisterItem) {
            val itemKey = keyOfItem(namespaceAndPath)
            val item = BlockItem(it, Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
            Registry.register(BuiltInRegistries.ITEM, itemKey, item)
        }
    }
    return Registry.register(BuiltInRegistries.BLOCK, blockKey, block)
}

fun registerStair(
    namespaceAndPath: Identifier,
    base: Block
): Block =
    registerGenericBlock(namespaceAndPath, { p ->
        StairBlock(
            base.defaultBlockState(),
            p
        )
    }, BlockBehaviour.Properties.ofFullCopy(base))