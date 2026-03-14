@file:Suppress("unused")

package io.autumn.carminite.datagen.generators

import io.autumn.carminite.tool.ToolSet
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ItemModelOutput
import net.minecraft.client.data.models.model.ModelInstance
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import java.util.function.BiConsumer

class CarminiteItemModelGenerators(
    itemModelOutput: ItemModelOutput,
    modelOutput: BiConsumer<Identifier, ModelInstance>
) : ItemModelGenerators(itemModelOutput, modelOutput) {
    fun createFlatItemModels(itemList: List<Item>) {
        for (item in itemList) {
            generateFlatItem(item, ModelTemplates.FLAT_ITEM)
        }
    }

    fun createFlatHandheldItemModels(itemList: List<Item>) {
        for (item in itemList) {
            generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
        }
    }

    fun createToolSetItemModels(toolSet: ToolSet) {
        for (item in toolSet.listOfTools) {
            createFlatHandheldItemModels( listOf(item))
        }
    }
}