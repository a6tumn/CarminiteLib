@file:Suppress("unused")

package io.autumn.carminite.datagen

import io.autumn.carminite.tool.ToolSet
import io.autumn.carminite.tool.ToolType
import io.autumn.carminite.wood.WoodSet
import net.minecraft.data.BlockFamily
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import kotlin.collections.component1
import kotlin.collections.component2

fun RecipeProvider.createWoodSetRecipes(woodSet: WoodSet, blockFamily: BlockFamily, logTag: TagKey<Item>) {
    planksFromLog(woodSet.planks, logTag, 4)
    woodFromLogs(woodSet.wood, woodSet.log)
    woodFromLogs(woodSet.strippedWood, woodSet.strippedLog)
    hangingSign(woodSet.hangingSignItem, woodSet.strippedLog)
    generateRecipes(blockFamily, FeatureFlags.REGISTRY.allFlags())
}

fun RecipeProvider.createToolSetRecipes(toolSet: ToolSet, materialTag: TagKey<Item>, output: RecipeOutput) {
    val unlockName = "has_${toolSet.setName.replaceFirstChar { it.lowercase() }}"
    toolSet.mapOfToolsToTypes.forEach { (type, item) ->
        item?.let {
            when (type) {
                ToolType.SWORD -> shaped(RecipeCategory.COMBAT, item)
                    .pattern("X")
                    .pattern("X")
                    .pattern("#")
                    .define('#', Items.STICK)
                    .define('X', materialTag)
                    .unlockedBy(unlockName, this.has(materialTag))
                    .save(output)
                ToolType.SHOVEL -> shaped(RecipeCategory.TOOLS, item)
                    .pattern("X")
                    .pattern("#")
                    .pattern("#")
                    .define('#', Items.STICK)
                    .define('X', materialTag)
                    .unlockedBy(unlockName, this.has(materialTag))
                    .save(output)
                ToolType.PICKAXE -> shaped(RecipeCategory.TOOLS, item)
                    .pattern("XXX")
                    .pattern(" # ")
                    .pattern(" # ")
                    .define('#', Items.STICK)
                    .define('X', materialTag)
                    .unlockedBy(unlockName, this.has(materialTag))
                    .save(output)
                ToolType.AXE -> shaped(RecipeCategory.TOOLS, item)
                    .pattern("XX")
                    .pattern("X#")
                    .pattern(" #")
                    .define('#', Items.STICK)
                    .define('X', materialTag)
                    .unlockedBy(unlockName, this.has(materialTag))
                    .save(output)
                ToolType.HOE -> shaped(RecipeCategory.TOOLS, item)
                    .pattern("XX")
                    .pattern(" #")
                    .pattern(" #")
                    .define('#', Items.STICK)
                    .define('X', materialTag)
                    .unlockedBy(unlockName, this.has(materialTag))
                    .save(output)
            }
        }
    }
}