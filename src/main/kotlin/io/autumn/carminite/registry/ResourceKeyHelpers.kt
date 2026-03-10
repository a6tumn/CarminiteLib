@file:Suppress("unused")

package io.autumn.carminite.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

fun keyOfBlock(
    namespaceAndPath: Identifier
) : ResourceKey<Block> =
    ResourceKey.create(Registries.BLOCK, namespaceAndPath)

fun keyOfItem(
    namespaceAndPath: Identifier
) : ResourceKey<Item> =
    ResourceKey.create(Registries.ITEM, namespaceAndPath)