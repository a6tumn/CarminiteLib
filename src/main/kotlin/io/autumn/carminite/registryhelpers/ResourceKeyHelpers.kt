@file:Suppress("unused")

package io.autumn.carminite.registryhelpers

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * Collection of helper methods for creating ResourceKeys.
 *
 * These functions mirror vanilla behavior. Defaults parameter
 * values are not provided in most cases as the arguments will
 * need meaningful values for production code.
 **/

/**
 * Creates a ResourceKey for a block.
 *
 * Intended for blocks or objects inheriting from Block.
 *
 * @param namespaceAndPath An identifier containing your project namepsace and the name of the block (no default set).
 **/
fun keyOfBlock(
    namespaceAndPath: Identifier
) : ResourceKey<Block> =
    ResourceKey.create(Registries.BLOCK, namespaceAndPath)

/**
 * Creates a ResourceKey for an item.
 *
 * Intended for items or objects inheriting from Item.
 *
 * @param namespaceAndPath An identifier containing your project namepsace and the name of the item (no default set).
 **/
fun keyOfItem(
    namespaceAndPath: Identifier
) : ResourceKey<Item> =
    ResourceKey.create(Registries.ITEM, namespaceAndPath)