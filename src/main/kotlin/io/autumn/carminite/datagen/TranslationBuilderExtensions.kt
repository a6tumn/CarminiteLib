@file:Suppress("unused")

package io.autumn.carminite.datagen

import io.autumn.carminite.tool.ToolSet
import io.autumn.carminite.wood.WoodSet
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

fun TranslationBuilder.addWoodSet(woodSet: WoodSet) {
    add(woodSet.log, "${woodSet.woodName} Log")
    add(woodSet.strippedLog, "Stripped ${woodSet.woodName} Log")
    add(woodSet.wood, "${woodSet.woodName} Wood")
    add(woodSet.strippedWood, "Stripped ${woodSet.woodName} Wood")
    add(woodSet.leaves, "${woodSet.woodName} Leaves")
    add(woodSet.sapling, "${woodSet.woodName} Sapling")
    add(woodSet.planks, "${woodSet.woodName} Planks")
    add(woodSet.door, "${woodSet.woodName} Door")
    add(woodSet.trapdoor, "${woodSet.woodName} Trapdoor")
    add(woodSet.fence, "${woodSet.woodName} Fence")
    add(woodSet.fenceGate, "${woodSet.woodName} Fence Gate")
    add(woodSet.stairs, "${woodSet.woodName} Stairs")
    add(woodSet.slab, "${woodSet.woodName} Slab")
    add(woodSet.button, "${woodSet.woodName} Button")
    add(woodSet.pressurePlate, "${woodSet.woodName} Pressure Plate")
    add(woodSet.signItem, "${woodSet.woodName} Sign")
    add(woodSet.hangingSignItem, "${woodSet.woodName} Hanging Sign")
}

fun TranslationBuilder.addToolSet(toolSet: ToolSet) {
    toolSet.mapOfToolsToTypes.forEach { (type, item) ->
        item?.let { it: Item ->
            val displayName = "${toolSet.setName} ${type.langSuffix}"
            add(it, displayName)
        }
    }
}

fun TranslationBuilder.addBlocks(blocks: List<Block>) {
    blocks.forEach { block ->
        add(block, toLangCase(BuiltInRegistries.BLOCK.getKey(block).path))
    }
}

fun TranslationBuilder.addItems(items: List<Item>) {
    items.forEach { item ->
        add(item, toLangCase(BuiltInRegistries.ITEM.getKey(item).path))
    }
}

fun TranslationBuilder.addItemTags(itemTags: List<TagKey<Item>>) {
    itemTags.forEach { itemTag ->
        add(itemTag, toLangCase(itemTag.translationKey))
    }
}

private fun toLangCase(name: String): String {
    return name
        .substringAfterLast('.')
        .replace('_', ' ')
        .split(' ')
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
}