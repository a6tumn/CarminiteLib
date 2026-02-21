package io.autumn.carminite.api.registry

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockBehaviour

/**
 * Collection of helper methods for streamlining block registration while
 * using and copying some logic from vanilla systems.
 *
 * Some of these functions mirror private vanilla implementations while others
 * simply streamline registration for mods with lots of repeating registration
 * patterns. Defaults parameter values are not provided in most cases as the
 * arguments will need meaningfulvalues for production code.
 **/

/**
 * Registers a standard stair block(not the deprecated legacy stair).
 *
 * Intended for generic stair blocks.
 *
 * @param namespaceAndPath An identifier containing your project namepsace and the name of the block (no default set).
 * @param base The base block that the stair will copy properties from (no default set).
 **/
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

/**
 * Registers a generic block.
 *
 * Intended for generic blocks.
 *
 * @param namespaceAndPath An identifier containing your project namepsace and the name of the block (no default set).
 * @param blockFactory An instance of the type of block you are creating (no default set).
 * @param settings The settings which you want your block to be registered with (no default set).
 * @param shouldRegisterItem A boolean which is used to determine whether or not a BlockItem will be registered with the block (defaults to true)
 **/
fun registerGenericBlock(namespaceAndPath: Identifier, blockFactory: (BlockBehaviour.Properties) -> Block, settings: BlockBehaviour.Properties, shouldRegisterItem: Boolean = true) : Block {
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