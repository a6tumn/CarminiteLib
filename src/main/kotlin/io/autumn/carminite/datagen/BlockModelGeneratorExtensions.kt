@file:Suppress("unused")

package io.autumn.carminite.datagen

import io.autumn.carminite.wood.WoodSet
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.BlockModelGenerators.PlantType
import net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock
import net.minecraft.client.data.models.BlockModelGenerators.plainVariant
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.BlockFamily
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block

fun BlockModelGenerators.createWoodSetModels(woodSet: WoodSet, blockFamily: BlockFamily, leavesTintColor: Int) {
    woodProvider(woodSet.log).logWithHorizontal(woodSet.log).wood(woodSet.wood)
    woodProvider(woodSet.strippedLog).logWithHorizontal(woodSet.strippedLog).wood(woodSet.strippedWood)
    createTintedLeaves(woodSet.leaves, TexturedModel.LEAVES, leavesTintColor)
    createPlantWithDefaultItem(woodSet.sapling, woodSet.pottedSapling, PlantType.NOT_TINTED)
    createHangingSign(woodSet.strippedLog, woodSet.hangingSign, woodSet.wallHangingSign)
    family(woodSet.planks).generateFor(blockFamily)
}

fun BlockModelGenerators.createPlantWithAltPotted(name: String, standAlone: Block, potted: Block, pottedTexture: Identifier, plantType: PlantType) {
    registerSimpleItemModel(
        standAlone.asItem(),
        plantType.createItemModel(this, standAlone)
    )
    createPlant(name, standAlone, potted, pottedTexture, plantType)
}

private fun BlockModelGenerators.createPlant(name: String, standAlone: Block, potted: Block, pottedTexture: Identifier, plantType: PlantType) {
    createCrossBlock(standAlone, plantType)

    val textures = plantType.getPlantTextureMapping(standAlone)

    val pottedTexture = Material(pottedTexture)

    textures.put(TextureSlot.PLANT, pottedTexture)

    val model = plainVariant(
        plantType.crossPot.create(potted, textures, modelOutput)
    )

    blockStateOutput.accept(createSimpleBlock(potted, model))
}