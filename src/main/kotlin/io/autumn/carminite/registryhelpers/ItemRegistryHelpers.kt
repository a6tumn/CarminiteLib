@file:Suppress("unused")

package io.autumn.carminite.registryhelpers

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item

/**
 * Collection of helper methods for streamlining item registration while
 * using and copying some logic from vanilla systems.
 *
 * Some of these functions mirror private vanilla implementations while others
 * simply streamline registration for mods with lots of repeating registration
 * patterns. Defaults parameter values are not provided in most cases as the
 * arguments will need meaningful values for production code.
 **/

/**
 * Registers a generic item.
 *
 * Intended for generic items.
 *
 * @param namespaceAndPath An identifier containing your project namepsace and the name of the item (no default set).
 * @param itemFactory An instance of the type of item you are creating (no default set).
 * @param settings The settings which you want your item to be registered with (no default set).
 **/
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