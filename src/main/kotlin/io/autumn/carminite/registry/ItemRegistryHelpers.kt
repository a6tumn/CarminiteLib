@file:Suppress("unused")

package io.autumn.carminite.registry

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item

fun <T : Item> registerGenericItem(
    namespaceAndPath: Identifier,
    itemFactory: (Item.Properties) -> T,
    settings: Item.Properties
): T {
    val itemKey = keyOfItem(namespaceAndPath)
    val item = itemFactory(settings.setId(itemKey))

    Registry.register(BuiltInRegistries.ITEM, itemKey, item)

    return item
}