@file:Suppress("unused")

package io.autumn.carminite.datagen.providers.sub

import io.autumn.carminite.datagen.providers.CarminiteRecipeProvider
import io.autumn.carminite.tool.ToolSet
import io.autumn.carminite.tool.ToolType
import io.autumn.carminite.wood.WoodSet
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

open class CarminiteRecipeSubProvider(
    registries: HolderLookup.Provider,
    output: RecipeOutput,
    private val parent: CarminiteRecipeProvider
) : RecipeProvider(registries, output) {

    fun createWoodSetRecipes(woodSet: WoodSet, logTag: TagKey<Item>, chestBlock: Block, trappedChestBlock: Block) {
        planksFromLog(woodSet.planks, logTag, 4)
        woodFromLogs(woodSet.wood, woodSet.log)
        woodFromLogs(woodSet.strippedWood, woodSet.strippedLog)
        hangingSign(woodSet.hangingSignItem, woodSet.strippedLog)
        generateRecipes(woodSet.blockFamily, FeatureFlags.REGISTRY.allFlags())
        shaped(RecipeCategory.DECORATIONS, chestBlock)
            .pattern("ppp")
            .pattern("pcp")
            .pattern("ppp")
            .define('p', woodSet.planks)
            .define('c', Blocks.CHEST)
            .unlockedBy(getHasName(woodSet.planks), has(woodSet.planks))
            .save(this.output)
        shaped(RecipeCategory.REDSTONE, trappedChestBlock)
            .pattern("ppp")
            .pattern("ptp")
            .pattern("ppp")
            .define('p', woodSet.planks)
            .define('t', Blocks.TRAPPED_CHEST)
            .unlockedBy(getHasName(woodSet.planks), has(woodSet.planks))
            .save(this.output)
    }

    fun createToolSetRecipes(toolSet: ToolSet, materialTag: TagKey<Item>) {
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
                        .save(this.output)
                    ToolType.SHOVEL -> shaped(RecipeCategory.TOOLS, item)
                        .pattern("X")
                        .pattern("#")
                        .pattern("#")
                        .define('#', Items.STICK)
                        .define('X', materialTag)
                        .unlockedBy(unlockName, this.has(materialTag))
                        .save(this.output)
                    ToolType.PICKAXE -> shaped(RecipeCategory.TOOLS, item)
                        .pattern("XXX")
                        .pattern(" # ")
                        .pattern(" # ")
                        .define('#', Items.STICK)
                        .define('X', materialTag)
                        .unlockedBy(unlockName, this.has(materialTag))
                        .save(this.output)
                    ToolType.AXE -> shaped(RecipeCategory.TOOLS, item)
                        .pattern("XX")
                        .pattern("X#")
                        .pattern(" #")
                        .define('#', Items.STICK)
                        .define('X', materialTag)
                        .unlockedBy(unlockName, this.has(materialTag))
                        .save(this.output)
                    ToolType.HOE -> shaped(RecipeCategory.TOOLS, item)
                        .pattern("XX")
                        .pattern(" #")
                        .pattern(" #")
                        .define('#', Items.STICK)
                        .define('X', materialTag)
                        .unlockedBy(unlockName, this.has(materialTag))
                        .save(this.output)
                }
            }
        }
    }

    override fun buildRecipes() {
        parent.run { buildRecipes() }
    }
}