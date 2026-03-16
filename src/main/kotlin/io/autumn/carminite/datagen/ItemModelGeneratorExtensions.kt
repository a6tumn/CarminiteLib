@file:Suppress("unused")

package io.autumn.carminite.datagen

import io.autumn.carminite.tool.ToolSet
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.world.item.Item

fun ItemModelGenerators.createToolSetModels(toolSet: ToolSet) {
    for(item in toolSet.listOfTools) {
        generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
    }
}

fun ItemModelGenerators.createFlatItemModels(itemList: List<Item>) {
    for (item in itemList) {
        generateFlatItem(item, ModelTemplates.FLAT_ITEM)
    }
}

fun ItemModelGenerators.createFlatHandheldItemModels(itemList: List<Item>) {
    for (item in itemList) {
        generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
    }
}