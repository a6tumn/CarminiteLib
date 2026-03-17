@file:Suppress("unused")

package io.autumn.carminite.registry

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

fun registerCreativeModeTab(
    namespaceAndPath: Identifier,
    icon: ItemLike,
    vararg entriesLists: List<ItemLike>
): CreativeModeTab {
    val allEntries = entriesLists.asIterable().flatten()
    val path = namespaceAndPath.path
    return Registry.register(
        BuiltInRegistries.CREATIVE_MODE_TAB,
        ResourceKey.create(Registries.CREATIVE_MODE_TAB, namespaceAndPath),
        FabricCreativeModeTab.builder()
            .title(Component.literal(path.replaceFirstChar { it.uppercase() }))
            .icon { ItemStack(icon) }
            .displayItems { _, output -> allEntries.forEach(output::accept) }
            .build()
    )
}